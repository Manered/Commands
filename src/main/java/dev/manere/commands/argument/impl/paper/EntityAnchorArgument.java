package dev.manere.commands.argument.impl.paper;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.manere.commands.argument.Argument;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.entity.LookAnchor;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class EntityAnchorArgument implements Argument<LookAnchor, LookAnchor> {
    @Override
    public @NotNull LookAnchor convert(@NotNull CommandSourceStack stack, @NotNull LookAnchor nativeValue) throws CommandSyntaxException {
        return nativeValue;
    }

    @Override
    public @NotNull ArgumentType<LookAnchor> getNativeType() {
        return ArgumentTypes.entityAnchor();
    }
}
