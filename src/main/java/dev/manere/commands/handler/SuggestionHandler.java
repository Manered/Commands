package dev.manere.commands.handler;

import dev.manere.commands.ctx.CommandContext;
import org.jetbrains.annotations.NotNull;

public interface SuggestionHandler<S> {
    @NotNull
    S suggestions(final @NotNull CommandContext context);
}