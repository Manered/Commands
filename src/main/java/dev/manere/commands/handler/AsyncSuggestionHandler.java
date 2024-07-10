package dev.manere.commands.handler;

import dev.manere.commands.ctx.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface AsyncSuggestionHandler extends SuggestionHandler<CompletableFuture<List<Suggestion>>> { }
