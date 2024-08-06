package dev.manere.commands.handler;

import dev.manere.commands.ctx.CommandContext;
import org.jetbrains.annotations.NotNull;

/**
 * Handles suggestions for command arguments.
 *
 * @param <S> the type of suggestions.
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
    S suggestions(final @NotNull CommandContext context);
}