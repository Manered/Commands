package dev.manere.commands.api;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.ParsedArgument;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.manere.commands.CommandContext;
import dev.manere.commands.CommandNode;
import dev.manere.commands.argument.Argument;
import dev.manere.commands.argument.ArgumentResult;
import dev.manere.commands.argument.CommandArgument;
import dev.manere.commands.argument.SingleCommandArgument;
import dev.manere.commands.completion.AsyncCompletionProvider;
import dev.manere.commands.completion.CompletionProvider;
import dev.manere.commands.completion.EmptyCompletionProvider;
import dev.manere.commands.completion.SyncCompletionProvider;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Predicate;

@SuppressWarnings("UnstableApiUsage")
public final class CommandAPIBrigadier {
    @NotNull
    @SuppressWarnings("unchecked")
    public static <S extends CommandSender> CommandContext<S> buildContext(final @NotNull CommandNode node, final @NotNull com.mojang.brigadier.context.CommandContext<CommandSourceStack> stack) throws CommandSyntaxException {
        final var context = new CommandContext<>(
            (S) stack.getSource().getSender(),
            node,
            stack,
            stack.getInput()
        );

        try {
            final Field field = stack.getClass().getDeclaredField("arguments");
            field.setAccessible(true);

            final Map<String, ParsedArgument<S, ?>> parsedArgumentMap = (Map<String, ParsedArgument<S, ?>>) field.get(stack);
            for (final Map.Entry<String, ParsedArgument<S, ?>> entry : parsedArgumentMap.entrySet()) {
                final String key = entry.getKey();
                final Object result = entry.getValue().getResult();

                context.getCommandArgument(key).ifPresent(argument -> context.registerArgumentResult(
                    ArgumentResult.result(key, Optional.ofNullable(result), argument)
                ));
            }

            for (final CommandArgument argument : node.arguments()) {
                if (argument.isRequired()) {
                    final Optional<ArgumentResult> result = context.findArgumentResult(argument.getKey());
                    if (result.isEmpty() || result.get().result().isEmpty()) {
                        throw CommandSyntaxException.BUILT_IN_EXCEPTIONS
                            .dispatcherParseException()
                            .create("Missing required argument: " + argument.getKey());
                    }
                }
            }
        } catch (final Exception e) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().create(e.getMessage());
        }

        return context;
    }

    @NotNull
    @SuppressWarnings("UnstableApiUsage")
    public static LiteralCommandNode<CommandSourceStack> convert(final @NotNull CommandNode node) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal(node.literal())
            .requires(commandSourceStack -> {
                final CommandSender sender = commandSourceStack.getSender();

                final List<Predicate<CommandSender>> filters = node.filters();

                for (final Predicate<CommandSender> commandContextPredicate : filters) {
                    if (commandContextPredicate.test(sender)) return false;
                }

                return true;
            });

        if (!node.executors().isEmpty() && (node.arguments().isEmpty() || !node.arguments().getFirst().isRequired())) {
            builder.executes(cmd -> {
                final CommandSender cmdSender = cmd.getSource().getSender();

                final Map<Class<? extends CommandSender>, Consumer<CommandContext<? extends CommandSender>>> executors = node.executors();

                final Class<? extends CommandSender> cmdSenderType = cmdSender.getClass();

                final var consumerFound = executors.entrySet().stream()
                    .filter(e -> e.getKey().isAssignableFrom(cmdSenderType))
                    .map(Map.Entry::getValue)
                    .findFirst();

                if (consumerFound.isPresent()) {
                    consumerFound.get().accept(buildContext(node, cmd));
                } else {
                    executors.getOrDefault(CommandSender.class, commandContext -> {}).accept(buildContext(node, cmd));
                }

                return Command.SINGLE_SUCCESS;
            });
        }

        final List<? extends CommandArgument> arguments = node.arguments();
        if (!arguments.isEmpty()) {
            ArgumentCommandNode<CommandSourceStack, ?> firstNode = null;
            ArgumentCommandNode<CommandSourceStack, ?> previousNode = null;

            for (int i = 0; i < arguments.size(); i++) {
                final CommandArgument argument = arguments.get(i);
                if (argument instanceof SingleCommandArgument<?> sca) {
                    final String key = sca.getKey();
                    final Argument<?, ?> argumentParser = sca.getArgument().get();
                    final ArgumentType<?> nativeType = argumentParser.getNativeType();

                    final RequiredArgumentBuilder<CommandSourceStack, ?> argumentBuilder = Commands.argument(key, nativeType);

                    if (!argument.isRequired() || getLastRequiredArgumentIndex(arguments) == i) {
                        argumentBuilder.executes(cmd -> {
                            final CommandSender cmdSender = cmd.getSource().getSender();

                            final Map<Class<? extends CommandSender>, Consumer<CommandContext<? extends CommandSender>>> executors = node.executors();

                            final Class<? extends CommandSender> cmdSenderType = cmdSender.getClass();

                            final var consumerFound = executors.entrySet().stream()
                                .filter(e -> e.getKey().isAssignableFrom(cmdSenderType))
                                .map(Map.Entry::getValue)
                                .findFirst();

                            if (consumerFound.isPresent()) {
                                consumerFound.get().accept(buildContext(node, cmd));
                            } else {
                                executors.getOrDefault(CommandSender.class, commandContext -> {}).accept(buildContext(node, cmd));
                            }

                            return Command.SINGLE_SUCCESS;
                        });
                    }

                    if (argument.getCompletions().isPresent()) {
                        argumentBuilder.suggests((context, suggestionsBuilder) -> convert(node, argument.getCompletions().get(), context, suggestionsBuilder));
                    } else {
                        if (argumentParser.getDefaultCompletions() instanceof EmptyCompletionProvider) {
                            argumentBuilder.suggests(nativeType::listSuggestions);
                        } else {
                            argumentBuilder.suggests((context, suggestionsBuilder) -> convert(node, argumentParser.getDefaultCompletions(), context, suggestionsBuilder));
                        }
                    }

                    ArgumentCommandNode<CommandSourceStack, ?> currentNode = argumentBuilder.build();

                    if (previousNode == null) {
                        firstNode = currentNode;
                    } else {
                        previousNode.addChild(currentNode);
                    }

                    previousNode = currentNode;
                }
            }

            if (firstNode != null) {
                builder.then(firstNode);
            }
        }

        for (final CommandNode child : node.children()) {
            builder.then(convert(child));
        }

        return builder.build();
    }

    private static int getLastRequiredArgumentIndex(final @NotNull List<? extends CommandArgument> arguments) {
        for (int i = arguments.size() - 1; i >= 0; i--) {
            if (arguments.get(i).isRequired()) {
                return i;
            }
        }

        return -1;
    }

    @NotNull
    @ApiStatus.Internal
    private static CompletableFuture<Suggestions> convert(
        final CommandNode node,
        final @NotNull CompletionProvider<?> customCompletions,
        final @NotNull com.mojang.brigadier.context.CommandContext<CommandSourceStack> stackCtx,
        final @NotNull SuggestionsBuilder suggestionsBuilder
    ) throws CommandSyntaxException {
        switch (customCompletions) {
            case AsyncCompletionProvider asyncCompletionProvider -> {
                return asyncCompletionProvider.completes(buildContext(node, stackCtx))
                    .thenApply(completions -> {
                        completions.forEach(completion -> suggestionsBuilder.suggest(
                            completion.getText(),
                            MessageComponentSerializer.message().serialize(completion.getTooltip())
                        ));
                        return suggestionsBuilder.build();
                    });
            }
            case SyncCompletionProvider syncCompletionProvider -> {
                syncCompletionProvider.completes(buildContext(node, stackCtx))
                    .forEach(completion -> suggestionsBuilder.suggest(
                        completion.getText(),
                        MessageComponentSerializer.message().serialize(completion.getTooltip())
                    ));

                return suggestionsBuilder.buildFuture();
            }
            default -> {
                return Suggestions.empty();
            }
        }
    }
}
