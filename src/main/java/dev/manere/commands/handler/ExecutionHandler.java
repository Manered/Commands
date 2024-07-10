package dev.manere.commands.handler;

import dev.manere.commands.ctx.CommandContext;
import org.jetbrains.annotations.NotNull;

public interface ExecutionHandler {
    void run(final @NotNull CommandContext context);
}
