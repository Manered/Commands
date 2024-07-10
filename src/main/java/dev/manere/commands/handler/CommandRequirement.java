package dev.manere.commands.handler;

import dev.manere.commands.ctx.CommandContext;
import org.jetbrains.annotations.NotNull;

public interface CommandRequirement {
    @NotNull
    RequirementResult require(final @NotNull CommandContext context);
}
