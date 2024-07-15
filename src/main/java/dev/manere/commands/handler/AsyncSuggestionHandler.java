package dev.manere.commands.handler;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Handles asynchronous suggestions for command arguments.
 */
public interface AsyncSuggestionHandler extends SuggestionHandler<CompletableFuture<List<Suggestion>>> { }
