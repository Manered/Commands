package dev.manere.commands.api;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.manere.commands.CommandContext;
import dev.manere.commands.CommandNode;
import dev.manere.commands.argument.Argument;
import dev.manere.commands.argument.CommandArgument;
import dev.manere.commands.argument.SingleCommandArgument;
import dev.manere.commands.completion.AsyncCompletionProvider;
import dev.manere.commands.completion.CompletionProvider;
import dev.manere.commands.completion.SyncCompletionProvider;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class CommandAPIBrigadier {
    @NotNull
    @SuppressWarnings("UnstableApiUsage")
    public static <S extends CommandSender> CommandContext<S> buildContext(final @NotNull CommandNode node, final @NotNull com.mojang.brigadier.context.CommandContext<CommandSourceStack> stack) {
        return new CommandContext<>(
            (S) stack.getSource().getSender(),
            node,
            stack,
            stack.getInput()
        );
    }

    @NotNull
    @SuppressWarnings("UnstableApiUsage")
    public static LiteralCommandNode<CommandSourceStack> convert(final @NotNull CommandNode node) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal(node.literal())
            .executes(cmd -> {
                final CommandSender cmdSender = cmd.getSource().getSender();

                final Map<Class<? extends CommandSender>, Consumer<CommandContext<? extends CommandSender>>> executors = node.executors();

                final Class<? extends CommandSender> cmdSenderType = cmdSender.getClass();

                final Optional<Consumer<CommandContext<? extends CommandSender>>> consumerFound = Optional.ofNullable(executors.get(cmdSenderType));

                consumerFound.ifPresentOrElse(
                    commandContextConsumer -> commandContextConsumer.accept(buildContext(node, cmd)),
                    () -> executors.getOrDefault(CommandSender.class, commandContext -> {}).accept(buildContext(node, cmd))
                );

                return Command.SINGLE_SUCCESS;
            })
            .requires(commandSourceStack -> {
                final CommandSender sender = commandSourceStack.getSender();

                final List<Predicate<CommandSender>> filters = node.filters();

                for (final Predicate<CommandSender> commandContextPredicate : filters) {
                    if (commandContextPredicate.test(sender)) return false;
                }

                return true;
            });

        for (final CommandNode child : node.children()) {
            builder.then(convert(child));
        }

        final List<? extends CommandArgument> arguments = node.arguments();
        if (!arguments.isEmpty()) {
            ArgumentCommandNode<CommandSourceStack, ?> previousNode = null;

            // Iterate through arguments in reverse to build the chain from the end
            for (int i = arguments.size() - 1; i >= 0; i--) {
                final CommandArgument currentArg = arguments.get(i);
                ArgumentCommandNode<CommandSourceStack, ?> currentNode = convert(node, currentArg);

                if (previousNode != null) {
                    // If we have a previous node, create a new node with the previous one as a child
                    final RequiredArgumentBuilder<CommandSourceStack, ?> tempBuilder = Commands.argument(
                        currentNode.getName(),
                        currentNode.getType()
                    );

                    // Add the executable if this is the last required argument
                    if (currentArg.isRequired() && i == getLastRequiredArgumentIndex(arguments)) {
                        tempBuilder.executes(cmd -> {
                            final CommandSender cmdSender = cmd.getSource().getSender();
                            final Map<Class<? extends CommandSender>, Consumer<CommandContext<? extends CommandSender>>> executors = node.executors();
                            final Class<? extends CommandSender> cmdSenderType = cmdSender.getClass();
                            final Optional<Consumer<CommandContext<? extends CommandSender>>> consumerFound = Optional.ofNullable(executors.get(cmdSenderType));

                            consumerFound.ifPresentOrElse(
                                commandContextConsumer -> commandContextConsumer.accept(buildContext(node, cmd)),
                                () -> executors.getOrDefault(CommandSender.class, commandContext -> {}).accept(buildContext(node, cmd))
                            );

                            return Command.SINGLE_SUCCESS;
                        });
                    }

                    currentNode = tempBuilder.then(previousNode).build();
                }

                previousNode = currentNode;
            }

            if (previousNode != null) {
                builder.then(previousNode);
            }
        }

        return builder.build();
    }

    @NotNull
    @ApiStatus.Internal
    public static Optional<SingleCommandArgument<? extends Argument<?, ?>>> getLastRequiredArgument(final @NotNull CommandNode node) {
        final List<? extends CommandArgument> arguments = node.arguments();
        if (arguments.isEmpty()) return Optional.empty();

        for (int i = arguments.size() - 1; i >= 0; i--) {
            final CommandArgument argument = arguments.get(i);
            if (argument.isRequired()) {
                if (argument instanceof SingleCommandArgument<? extends Argument<?, ?>> single) {
                    return Optional.of(single);
                }
            }
        }

        return Optional.empty();
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
    @SuppressWarnings("UnstableApiUsage")
    public static ArgumentCommandNode<CommandSourceStack, ?> convert(final @NotNull CommandNode node, final @NotNull CommandArgument argument) {
        if (argument instanceof SingleCommandArgument<?> single) {
            RequiredArgumentBuilder<CommandSourceStack, ?> builder = Commands.argument(single.getKey(), single.getArgument().get().getNativeType());

            final SingleCommandArgument<?> lastRequired = getLastRequiredArgument(node).orElse(null);
            if (lastRequired != null) {
                if (lastRequired.getKey().equals(single.getKey())) {
                    builder = builder.executes(cmd -> {
                        final CommandSender cmdSender = cmd.getSource().getSender();

                        final Map<Class<? extends CommandSender>, Consumer<CommandContext<? extends CommandSender>>> executors = node.executors();

                        final Class<? extends CommandSender> cmdSenderType = cmdSender.getClass();

                        final Optional<Consumer<CommandContext<? extends CommandSender>>> consumerFound = Optional.ofNullable(executors.get(cmdSenderType));

                        consumerFound.ifPresentOrElse(
                            commandContextConsumer -> commandContextConsumer.accept(buildContext(node, cmd)),
                            () -> executors.getOrDefault(CommandSender.class, commandContext -> {}).accept(buildContext(node, cmd))
                        );

                        return Command.SINGLE_SUCCESS;
                    });

                    if (argument.getCompletions().isPresent()) {
                        builder = builder.suggests((context, builder1) -> convert(node, argument.getCompletions().get(), context, builder1));
                    }
                }
            }

            return builder.build();
        } else {
            throw new IllegalArgumentException();
        }
    }

    @NotNull
    @ApiStatus.Internal
    @SuppressWarnings("UnstableApiUsage")
    private static CompletableFuture<Suggestions> convert(
        final CommandNode node,
        final @NotNull CompletionProvider<?> customCompletions,
        final @NotNull com.mojang.brigadier.context.CommandContext<CommandSourceStack> stackCtx,
        final @NotNull SuggestionsBuilder suggestionsBuilder
    ) {
        if (customCompletions instanceof AsyncCompletionProvider asyncCompletionProvider) {
            return asyncCompletionProvider.completes(buildContext(node, stackCtx))
                .thenApply(completions -> {
                    completions.forEach(completion -> suggestionsBuilder.suggest(
                        completion.getText(),
                        MessageComponentSerializer.message().serialize(completion.getTooltip())
                    ));
                    return suggestionsBuilder.build();
                });
        } else if (customCompletions instanceof SyncCompletionProvider syncCompletionProvider) {
            syncCompletionProvider.completes(buildContext(node, stackCtx))
                .forEach(completion -> suggestionsBuilder.suggest(
                    completion.getText(),
                    MessageComponentSerializer.message().serialize(completion.getTooltip())
                ));

            return suggestionsBuilder.buildFuture();
        } else {
            return Suggestions.empty();
        }
    }
}
