package dev.manere.commands.handler;

import dev.manere.commands.ctx.CommandContext;
import org.jetbrains.annotations.NotNull;

/**
 * Defines a requirement for executing a command.
 */
public interface CommandRequirement {
    /**
     * Checks if the command can be executed in the given context.
     *
     * @param context the command context.
     * @return the result of the requirement check.
     */
    @NotNull
    RequirementResult require(final @NotNull CommandContext context);
}