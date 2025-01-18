package dev.manere.commands.argument.impl.paper;

import com.mojang.brigadier.arguments.ArgumentType;
import dev.manere.commands.argument.Argument;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.entity.LookAnchor;
import org.jetbrains.annotations.NotNull;

public class EntityAnchorArgument implements Argument<LookAnchor, LookAnchor> {
    @Override
    public @NotNull ArgumentType<LookAnchor> getNativeType() {
        return ArgumentTypes.entityAnchor();
    }
}
