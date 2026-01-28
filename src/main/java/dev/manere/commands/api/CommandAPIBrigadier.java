package dev.manere.commands.api;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.manere.commands.CommandContext;
import dev.manere.commands.CommandNode;
import dev.manere.commands.argument.Argument;
import dev.manere.commands.argument.ArgumentResult;
import dev.manere.commands.argument.CommandArgument;
import dev.manere.commands.completion.AsyncSuggestions;
import dev.manere.commands.completion.Suggestions;
import dev.manere.commands.completion.SyncSuggestions;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Predicate;

@SuppressWarnings({"UnstableApiUsage", "unchecked"})
public final class CommandAPIBrigadier {

    @NotNull
    public static <S extends CommandSender> CommandContext<S> buildContext(
        final @NotNull CommandNode node,
        final @NotNull List<CommandNode> ancestors,
        final @NotNull com.mojang.brigadier.context.CommandContext<CommandSourceStack> stack,
        final boolean strict
    ) throws CommandSyntaxException {
        final var context = new CommandContext<>(
            (S) stack.getSource().getSender(),
            node,
            stack,
            stack.getInput()
        );

        final List<CommandNode> allNodes = new ArrayList<>(ancestors);
        allNodes.add(node);

        for (final CommandNode contextNode : allNodes) {
            for (final CommandArgument<?> argument : contextNode.arguments()) {
                final String key = argument.getKey();
                final Argument<Object, Object> argumentParser = (Argument<Object, Object>) argument.getArgument().get();

                try {
                    final Object nativeValue = stack.getArgument(key, Object.class);
                    final Object value = argumentParser.convert(stack.getSource(), nativeValue);
                    context.registerArgumentResult(ArgumentResult.result(key, value, (CommandArgument<? extends Argument<Object, Object>>) argument));
                } catch (final IllegalArgumentException ignored) {
                    // argument not found in the context, expected for optional args or during suggestions
                }
            }
        }

        if (strict) {
            for (final CommandNode contextNode : allNodes) {
                for (final CommandArgument<?> argument : contextNode.arguments()) {
                    if (argument.isRequired()) {
                        final Optional<ArgumentResult> result = context.findArgumentResult(argument.getKey());
                        if (result.isEmpty() || result.get().result().isEmpty()) {
                            // shouldn't really ever happen tbh, if it does then it's a bug with CommandContext or this class.
                            throw new SimpleCommandExceptionType(() -> "Missing required argument: " + argument.getKey()).create();
                        }
                    }
                }
            }
        }

        return context;
    }

    @NotNull
    public static LiteralCommandNode<CommandSourceStack> convert(final @NotNull CommandNode node) {
        return convert(node, Collections.emptyList());
    }

    private static LiteralCommandNode<CommandSourceStack> convert(final @NotNull CommandNode node, final @NotNull List<CommandNode> ancestors) {
        final LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal(node.literal())
            .requires(commandSourceStack -> checkRequirements(node, commandSourceStack));

        build(builder, node.arguments(), node.children(), node, ancestors);

        return builder.build();
    }

    private static void build(
        final ArgumentBuilder<CommandSourceStack, ?> parent,
        final List<? extends CommandArgument<?>> arguments,
        final @NotNull List<CommandNode> children,
        final CommandNode node,
        final @NotNull List<CommandNode> ancestors
    ) {
        // base case: no more arguments for this node. attach executors and children.
        if (arguments.isEmpty()) {
            if (!node.executors().isEmpty()) {
                parent.executes(cmd -> execute(node, ancestors, cmd));
            }

            // for each child, the current node becomes an ancestor.
            final List<CommandNode> childAncestors = new ArrayList<>(ancestors);
            childAncestors.add(node);

            for (final CommandNode child : children) {
                final LiteralCommandNode<CommandSourceStack> childNode = convert(child, childAncestors);
                parent.then(childNode);

                for (final String alias : child.aliases()) {
                    final LiteralArgumentBuilder<CommandSourceStack> aliasBuilder = Commands.literal(alias)
                        .requires(commandSourceStack -> checkRequirements(child, commandSourceStack));

                    // rebuild using the same CommandNode
                    build(aliasBuilder, child.arguments(), child.children(), child, childAncestors);

                    parent.then(aliasBuilder);
                }
            }
            return;
        }

        // recursive step: process the head of the argument list.
        final CommandArgument<?> head = arguments.getFirst();
        final List<? extends CommandArgument<?>> tail = arguments.subList(1, arguments.size());

        // the ancestors for this argument are the same as for the node itself.
        final RequiredArgumentBuilder<CommandSourceStack, ?> argumentBuilder = createArgumentBuilder(node, head, ancestors);

        // recursively build the rest of the argument chain and children on top of the current argument.
        // the ancestor list remains the same because we are still building for the same node.
        final List<CommandNode> newAncestors = new ArrayList<>(ancestors);
        newAncestors.add(node);

        build(argumentBuilder, tail, children, node, newAncestors);

        parent.then(argumentBuilder);

        // if the argument is optional, we must also create a path that skips it.
        // this path is built on the original parent, not the new argumentBuilder
        if (head.isOptional()) {
            build(parent, tail, children, node, ancestors);
        }
    }

    private static RequiredArgumentBuilder<CommandSourceStack, ?> createArgumentBuilder(
        final CommandNode node,
        final CommandArgument<?> argument,
        final @NotNull List<CommandNode> ancestors
    ) {
        final String key = argument.getKey();
        final Argument<?, ?> argumentParser = argument.getArgument().get();
        final ArgumentType<?> nativeType = argumentParser.getNativeType();

        final RequiredArgumentBuilder<CommandSourceStack, ?> builder = Commands.argument(key, nativeType);

        final Suggestions<?> suggestions = argument.getCompletions().orElse(null);
        if (suggestions != null && !suggestions.isEmpty()) {
            // for suggestions, the context needs the current node as well as its ancestors.
            final List<CommandNode> suggestionAncestors = new ArrayList<>(ancestors);
            suggestionAncestors.add(node);
            builder.suggests((context, suggestionsBuilder) -> suggest(node, suggestionAncestors, suggestions, context, suggestionsBuilder));
        }

        builder.requires(stack -> {
            final CommandSender sender = stack.getSender();
            for (final Predicate<CommandSender> filter : argument.filters()) {
                if (filter.test(sender)) return false;
            }
            return true;
        });

        return builder;
    }

    private static boolean checkRequirements(final CommandNode node, final CommandSourceStack stack) {
        final CommandSender sender = stack.getSender();
        for (final Predicate<CommandSender> filter : node.filters()) {
            if (filter.test(sender)) return false;
        }
        return true;
    }

    private static int execute(final CommandNode node, final @NotNull List<CommandNode> ancestors, final com.mojang.brigadier.context.CommandContext<CommandSourceStack> cmd) throws CommandSyntaxException {
        final CommandSender cmdSender = cmd.getSource().getSender();
        final Map<Class<? extends CommandSender>, Consumer<CommandContext<? extends CommandSender>>> executors = node.executors();
        final var consumerFound = findExecutor(executors, cmdSender.getClass());

        // the context for execution needs the current node and all its ancestors.
        final CommandContext<CommandSender> context = buildContext(node, ancestors, cmd, true);

        if (consumerFound.isPresent()) {
            consumerFound.get().accept(context);
        } else {
            executors.getOrDefault(CommandSender.class, c -> {

            }).accept(context);
        }

        return Command.SINGLE_SUCCESS;
    }

    @NotNull
    private static Optional<Consumer<CommandContext<? extends CommandSender>>> findExecutor(
        final @NotNull Map<Class<? extends CommandSender>, Consumer<CommandContext<? extends CommandSender>>> executors,
        final @NotNull Class<? extends CommandSender> cmdSenderType
    ) {
        return executors.entrySet().stream()
            .filter(e -> e.getKey().isAssignableFrom(cmdSenderType))
            .map(Map.Entry::getValue)
            .findFirst();
    }

    @Nullable
    @ApiStatus.Internal
    private static CompletableFuture<com.mojang.brigadier.suggestion.Suggestions> suggest(
        final CommandNode node,
        final @NotNull List<CommandNode> ancestors,
        final @NotNull Suggestions<?> customCompletions,
        final @NotNull com.mojang.brigadier.context.CommandContext<CommandSourceStack> stackCtx,
        final @NotNull SuggestionsBuilder suggestionsBuilder
    ) throws CommandSyntaxException {
        // we use strict=false here to avoid errors when required arguments are missing (which is expected during typing...)
        final CommandContext<CommandSender> context = buildContext(node, ancestors, stackCtx, false);
        final String lastArg = suggestionsBuilder.getRemainingLowerCase();

        if (customCompletions instanceof AsyncSuggestions asyncProvider) {
            return asyncProvider.suggests(context).thenApply(completions -> {
                completions.stream()
                    .filter(c -> c.getText().toLowerCase().startsWith(lastArg))
                    .forEach(completion -> completion.getTooltip().ifPresentOrElse(
                        tooltip -> suggestionsBuilder.suggest(completion.getText(), MessageComponentSerializer.message().serialize(tooltip)),
                        () -> suggestionsBuilder.suggest(completion.getText())
                    ));
                return suggestionsBuilder.build();
            });
        } else if (customCompletions instanceof SyncSuggestions syncProvider) {
            syncProvider.suggests(context).stream()
                .filter(c -> c.getText().toLowerCase().startsWith(lastArg))
                .forEach(completion -> completion.getTooltip().ifPresentOrElse(
                    tooltip -> suggestionsBuilder.suggest(completion.getText(), MessageComponentSerializer.message().serialize(tooltip)),
                    () -> suggestionsBuilder.suggest(completion.getText())
                ));
            return suggestionsBuilder.buildFuture();
        }

        return suggestionsBuilder.buildFuture();
    }
}
