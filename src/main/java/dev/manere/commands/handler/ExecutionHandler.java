package dev.manere.commands.handler;

import dev.manere.commands.ctx.CommandContext;
import dev.manere.commands.ctx.CommandSource;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Handles the execution of a command.
 *
 * @see CommandContext
 */
@FunctionalInterface
public interface ExecutionHandler {
    @NotNull
    static ExecutionHandler create(final @NotNull Consumer<CommandSource> handler) {
        return context -> handler.accept(context.source());
    }

    /**
     * Executes the command in the given context.
     *
     * @param context the command context.
     */
    void run(final @NotNull CommandContext context);
}