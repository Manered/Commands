package dev.manere.commands.handler;

import dev.manere.commands.ctx.CommandContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Function;

/**
 * Handles suggestions for command arguments.
 *
 * @param <S> the type of suggestions.
 *
 * @see SyncSuggestionHandler
 * @see AsyncSuggestionHandler
 */
@FunctionalInterface
public interface SuggestionHandler<S> {
    /**
     * Provides suggestions for command arguments based on the given context.
     *
     * @param context the command context.
     * @return the suggestions.
     */
    @NotNull
    @Unmodifiable
    S suggestions(final @NotNull CommandContext context);

    @NotNull
    static SyncSuggestionHandler provide(final @NotNull Set<String> textSuggestions) {
        return sync(context -> {
            final List<Suggestion> suggestions = new ArrayList<>();

            for (final String suggestion : textSuggestions) {
                suggestions.add(
                    suggestion.startsWith("<") && suggestion.endsWith(">") || suggestion.startsWith("[") && suggestion.endsWith("]")
                        ? Suggestion.suggestion(suggestion, true)
                        : Suggestion.suggestion(suggestion, false)
                );
            }

            return suggestions;
        });
    }

    @NotNull
    static SyncSuggestionHandler sync(final @NotNull Function<CommandContext, List<Suggestion>> function) {
        return function::apply;
    }

    @NotNull
    static AsyncSuggestionHandler async(final @NotNull Function<CommandContext, List<Suggestion>> function) {
        return context -> CompletableFuture.supplyAsync(() -> function.apply(context));
    }

    @NotNull
    static AsyncSuggestionHandler asyncFuture(final @NotNull Function<CommandContext, CompletableFuture<List<Suggestion>>> function) {
        return function::apply;
    }
}