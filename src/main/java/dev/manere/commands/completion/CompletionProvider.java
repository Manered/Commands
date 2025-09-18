package dev.manere.commands.completion;

import dev.manere.commands.CommandContext;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface CompletionProvider<C> {
    @NotNull
    C completes(final @NotNull CommandContext<? extends CommandSender> context);

    default boolean isEmpty() {
        return this instanceof EmptyCompletionProvider || this.equals(EMPTY);
    }

    @NotNull
    static AsyncCompletionProvider future(final @NotNull AsyncCompletionProvider lambda) {
        return lambda;
    }

    @NotNull
    static SyncCompletionProvider sync(final @NotNull SyncCompletionProvider lambda) {
        return lambda;
    }

    @NotNull
    static SyncCompletionProvider sync(final @NotNull Collection<Completion> completions) {
        return sync(ctx -> completions);
    }

    @NotNull
    static SyncCompletionProvider sync(final @NotNull Completion... completions) {
        return sync(ctx -> List.of(completions));
    }

    @NotNull
    static AsyncCompletionProvider async(final @NotNull Function<CommandContext<? extends CommandSender>, Collection<Completion>> function) {
        return context -> CompletableFuture.supplyAsync(() -> function.apply(context));
    }

    @NotNull
    EmptyCompletionProvider EMPTY = new EmptyCompletionProvider();

    @NotNull
    static EmptyCompletionProvider empty() {
        return EMPTY;
    }
}