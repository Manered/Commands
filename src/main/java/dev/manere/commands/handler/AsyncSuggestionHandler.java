package dev.manere.commands.handler;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Handles asynchronous suggestions for command arguments.
 *
 * @see CompletableFuture
 * @see Suggestion
 * @see SuggestionHandler
 */
@FunctionalInterface
public interface AsyncSuggestionHandler extends SuggestionHandler<CompletableFuture<List<Suggestion>>> {}
