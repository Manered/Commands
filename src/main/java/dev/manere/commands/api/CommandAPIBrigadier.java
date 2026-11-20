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
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Predicate;

@SuppressWarnings("unchecked")
public final class CommandAPIBrigadier {

    private static final java.lang.reflect.Field ARGUMENTS_FIELD;
    static {
        try {
            ARGUMENTS_FIELD = com.mojang.brigadier.context.CommandContext.class.getDeclaredField("arguments");
            ARGUMENTS_FIELD.setAccessible(true);
        } catch (final NoSuchFieldException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

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
                if (!getArguments(stack).containsKey(key)) continue;

                final Argument<Object, Object> parser =
                    (Argument<Object, Object>) argument.argumentInstance();

                try {
                    final Object nativeValue = stack.getArgument(key, Object.class);
                    final Object value = parser.convert(stack.getSource(), nativeValue);

                    context.registerArgumentResult(
                        ArgumentResult.result(
                            key,
                            value,
                            (CommandArgument<? extends Argument<Object, Object>>) argument
                        )
                    );
                } catch (final Exception e) {
                    if (strict) {
                        if (e instanceof CommandSyntaxException syntaxException) {
                            throw syntaxException;
                        }
                        throw new SimpleCommandExceptionType(() -> "Invalid argument " + key + ": " + e.getMessage()).create();
                    }
                }
            }
        }

        if (strict) {
            final Set<String> parsedArguments = getArguments(stack).keySet();

            for (final CommandNode contextNode : allNodes) {
                for (final CommandArgument<?> argument : contextNode.arguments()) {
                    if (!argument.isRequired()) continue;

                    if (!parsedArguments.contains(argument.getKey())) continue;

                    final Optional<ArgumentResult> result =
                        context.findArgumentResult(argument.getKey());

                    if (result.isEmpty() || result.get().result().isEmpty()) {
                        throw new SimpleCommandExceptionType(
                            () -> "Invalid command usage: Argument " + argument.getKey() + " can not be empty."
                        ).create();
                    }
                }
            }
        }

        return context;
    }

    @NotNull
    private static Map<String, com.mojang.brigadier.context.ParsedArgument<CommandSourceStack, ?>> getArguments(
        final @NotNull com.mojang.brigadier.context.CommandContext<CommandSourceStack> stack
    ) {
        try {
            return (Map<String, com.mojang.brigadier.context.ParsedArgument<CommandSourceStack, ?>>) ARGUMENTS_FIELD.get(stack);
        } catch (final ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public static LiteralCommandNode<CommandSourceStack> convert(
        final @NotNull CommandNode node
    ) {
        return convert(node, List.of());
    }

    private static LiteralCommandNode<CommandSourceStack> convert(
        final @NotNull CommandNode node,
        final @NotNull List<CommandNode> ancestors
    ) {
        final LiteralArgumentBuilder<CommandSourceStack> builder =
            Commands.literal(node.literal())
                .requires(stack -> checkRequirements(node, ancestors, stack));

        buildArguments(builder, node.arguments(), node, ancestors);

        attachChildren(builder, node.children(), node, ancestors);

        return builder.build();
    }

    private static void buildArguments(
        final ArgumentBuilder<CommandSourceStack, ?> parent,
        final List<? extends CommandArgument<?>> arguments,
        final CommandNode node,
        final List<CommandNode> ancestors
    ) {
        if (arguments.isEmpty()) {
            if (!node.executors().isEmpty()) {
                parent.executes(cmd -> {
                    execute(node, ancestors, cmd);
                    return Command.SINGLE_SUCCESS;
                });
            }
            return;
        }

        final CommandArgument<?> head = arguments.getFirst();
        final List<? extends CommandArgument<?>> tail =
            arguments.subList(1, arguments.size());

        final RequiredArgumentBuilder<CommandSourceStack, ?> argBuilder =
            createArgumentBuilder(node, head, ancestors);

        buildArguments(argBuilder, tail, node, ancestors);
        parent.then(argBuilder);

        if (head.isOptional()) {
            if (!node.executors().isEmpty()) {
                parent.executes(cmd -> {
                    execute(node, ancestors, cmd);
                    return Command.SINGLE_SUCCESS;
                });
            }
        }
    }

    private static void attachChildren(
        final ArgumentBuilder<CommandSourceStack, ?> parent,
        final List<CommandNode> children,
        final CommandNode node,
        final List<CommandNode> ancestors
    ) {
        final List<CommandNode> childAncestors = new ArrayList<>(ancestors);
        childAncestors.add(node);

        for (final CommandNode child : children) {
            final LiteralCommandNode<CommandSourceStack> childNode =
                convert(child, childAncestors);

            parent.then(childNode);

            for (final String alias : child.aliases()) {
                final LiteralArgumentBuilder<CommandSourceStack> aliasBuilder =
                    Commands.literal(alias)
                        .requires(stack -> checkRequirements(child, childAncestors, stack));

                buildArguments(aliasBuilder, child.arguments(), child, childAncestors);
                attachChildren(aliasBuilder, child.children(), child, childAncestors);
                parent.then(aliasBuilder);
            }
        }
    }

    private static RequiredArgumentBuilder<CommandSourceStack, ?> createArgumentBuilder(
        final CommandNode node,
        final CommandArgument<?> argument,
        final List<CommandNode> ancestors
    ) {
        final String key = argument.getKey();
        final Argument<?, ?> parser = argument.argumentInstance();
        final ArgumentType<?> nativeType = parser.getNativeType();

        final RequiredArgumentBuilder<CommandSourceStack, ?> builder =
            Commands.argument(key, nativeType);

        final Suggestions<?> suggestions = argument.getCompletions().orElse(null);
        if (suggestions != null && !suggestions.isEmpty()) {
            builder.suggests((ctx, sb) ->
                suggest(node, ancestors, suggestions, ctx, sb)
            );
        }

        builder.requires(stack -> {
            final CommandSender sender = stack.getSender();
            for (final CommandNode ancestor : ancestors) {
                for (final Predicate<CommandSender> req : ancestor.requirements()) {
                    if (!req.test(sender)) return false;
                }
            }
            for (final Predicate<CommandSender> req : node.requirements()) {
                if (!req.test(sender)) return false;
            }
            for (final Predicate<CommandSender> req : argument.requirements()) {
                if (!req.test(sender)) return false;
            }
            return true;
        });

        return builder;
    }

    private static void execute(
        final CommandNode node,
        final List<CommandNode> ancestors,
        final com.mojang.brigadier.context.CommandContext<CommandSourceStack> cmd
    ) throws CommandSyntaxException {
        final CommandSender sender = cmd.getSource().getSender();
        final var executors = node.executors();

        final CommandContext<CommandSender> context =
            buildContext(node, ancestors, cmd, true);

        final Consumer<CommandContext<? extends CommandSender>> executor =
            findExecutor(executors, sender.getClass())
                .orElseGet(() -> executors.get(CommandSender.class));

        if (executor == null) {
            if (!(sender instanceof Player) && executors.containsKey(Player.class)) {
                CommandAPI.getInstance().flatMap(api -> api.getConfig().getOption(CommandAPIOptions.PLAYER_ONLY_COMMAND_MESSAGE)).ifPresent(sender::sendMessage);
                return;
            }
            CommandAPI.getInstance().flatMap(api -> api.getConfig().getOption(CommandAPIOptions.EXECUTOR_MISMATCH_MESSAGE)).ifPresent(sender::sendMessage);
            return;
        }

        executor.accept(context);
    }

    private static Optional<Consumer<CommandContext<? extends CommandSender>>> findExecutor(
        final Map<Class<? extends CommandSender>, Consumer<CommandContext<? extends CommandSender>>> executors,
        final Class<? extends CommandSender> senderType
    ) {
        return executors.entrySet().stream()
            .filter(e -> e.getKey().isAssignableFrom(senderType))
            .map(Map.Entry::getValue)
            .findFirst();
    }

    @Nullable
    @ApiStatus.Internal
    private static CompletableFuture<com.mojang.brigadier.suggestion.Suggestions> suggest(
        final CommandNode node,
        final List<CommandNode> ancestors,
        final Suggestions<?> provider,
        final com.mojang.brigadier.context.CommandContext<CommandSourceStack> ctx,
        final SuggestionsBuilder builder
    ) {
        final CommandContext<CommandSender> context;
        try {
            context = buildContext(node, ancestors, ctx, false);
        } catch (final CommandSyntaxException e) {
            return builder.buildFuture();
        }

        final String remaining = builder.getRemainingLowerCase();

        if (provider instanceof AsyncSuggestions async) {
            return async.suggests(context).thenApply(list -> {
                list.stream()
                    .filter(c -> c.getText().toLowerCase().startsWith(remaining) || formatSuggestion(c.getText()).toLowerCase().startsWith(remaining))
                    .forEach(c ->
                        c.getTooltip().ifPresentOrElse(
                            t -> builder.suggest(formatSuggestion(c.getText()), MessageComponentSerializer.message().serialize(t)),
                            () -> builder.suggest(formatSuggestion(c.getText()))
                        )
                    );
                return builder.build();
            }).exceptionally(ex -> builder.build());
        }

        if (provider instanceof SyncSuggestions sync) {
            sync.suggests(context).stream()
                .filter(c -> c.getText().toLowerCase().startsWith(remaining) || formatSuggestion(c.getText()).toLowerCase().startsWith(remaining))
                .forEach(c ->
                    c.getTooltip().ifPresentOrElse(
                        t -> builder.suggest(formatSuggestion(c.getText()), MessageComponentSerializer.message().serialize(t)),
                        () -> builder.suggest(formatSuggestion(c.getText()))
                    )
                );
            return builder.buildFuture();
        }

        return builder.buildFuture();
    }

    private static String formatSuggestion(final String text) {
        if (text.contains(" ") && !text.startsWith("\"")) {
            return "\"" + text + "\"";
        }
        return text;
    }

    private static boolean checkRequirements(
        final CommandNode node,
        final List<CommandNode> ancestors,
        final CommandSourceStack stack
    ) {
        final CommandSender sender = stack.getSender();
        for (final CommandNode ancestor : ancestors) {
            for (final Predicate<CommandSender> req : ancestor.requirements()) {
                if (!req.test(sender)) return false;
            }
        }
        for (final Predicate<CommandSender> req : node.requirements()) {
            if (!req.test(sender)) return false;
        }
        return true;
    }
}
