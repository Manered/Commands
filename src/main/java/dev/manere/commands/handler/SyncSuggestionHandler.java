package dev.manere.commands.handler;

import java.util.List;

/**
 * Handles synchronous suggestions for command arguments.
 *
 * @see SuggestionHandler
 * @see Suggestion
 */
@FunctionalInterface
public interface SyncSuggestionHandler extends SuggestionHandler<List<Suggestion>> {}
