package dev.manere.commands.handler;

import java.util.List;

/**
 * Handles synchronous suggestions for command arguments.
 */
@FunctionalInterface
public interface SyncSuggestionHandler extends SuggestionHandler<List<Suggestion>> { }
