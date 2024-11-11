package dev.manere.commands.handler;

import dev.manere.commands.ctx.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

/**
 * Defines a requirement for executing a command.
 *
 * @see CommandContext
 * @see RequirementResult
 */
@FunctionalInterface
public interface CommandRequirement extends Predicate<CommandContext> {
    /**
     * Checks if the command can be executed in the given context.
     *
     * @param context the command context.
     * @return the result of the requirement check.
     */
    @NotNull
    RequirementResult require(final @NotNull CommandContext context);

    @Override
    default boolean test(final @NotNull CommandContext context) {
        return require(context).equals(RequirementResult.FAILED);
    }
}