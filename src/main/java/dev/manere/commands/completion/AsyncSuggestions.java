package dev.manere.commands.completion;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public interface AsyncSuggestions extends Suggestions<CompletableFuture<Collection<Completion>>> {}
