package dev.manere.commands.handler;

import dev.manere.commands.ctx.CommandContext;
import org.jetbrains.annotations.NotNull;

/**
 * Handles the execution of a command.
 */
@FunctionalInterface
public interface ExecutionHandler {
    /**
     * Executes the command in the given context.
     *
     * @param context the command context.
     */
    void run(final @NotNull CommandContext context);
}