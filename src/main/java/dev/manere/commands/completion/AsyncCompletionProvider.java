package dev.manere.commands.completion;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public interface AsyncCompletionProvider extends CompletionProvider<CompletableFuture<Collection<Completion>>> {}
