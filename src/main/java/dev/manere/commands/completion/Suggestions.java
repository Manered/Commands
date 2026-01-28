package dev.manere.commands.completion;

import dev.manere.commands.CommandContext;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface Suggestions<C> {
    @NotNull
    C suggests(final @NotNull CommandContext<? extends CommandSender> context);

    default boolean isEmpty() {
        return this instanceof EmptySuggestions || this.equals(EMPTY);
    }

    @NotNull
    static AsyncSuggestions suggestFuture(final @NotNull AsyncSuggestions lambda) {
        return lambda;
    }

    @NotNull
    static SyncSuggestions suggest(final @NotNull SyncSuggestions lambda) {
        return lambda;
    }

    @NotNull
    static SyncSuggestions suggest(final @NotNull Collection<Completion> completions) {
        return suggest(ctx -> completions);
    }

    @NotNull
    static SyncSuggestions suggest(final @NotNull Completion... completions) {
        return suggest(ctx -> List.of(completions));
    }

    @NotNull
    static AsyncSuggestions suggestAsync(final @NotNull Function<CommandContext<? extends CommandSender>, Collection<Completion>> function) {
        return context -> CompletableFuture.supplyAsync(() -> function.apply(context));
    }

    @NotNull
    EmptySuggestions EMPTY = new EmptySuggestions();

    @NotNull
    static EmptySuggestions empty() {
        return EMPTY;
    }
}