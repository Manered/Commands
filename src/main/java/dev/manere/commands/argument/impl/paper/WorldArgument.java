package dev.manere.commands.argument.impl.paper;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.manere.commands.argument.Argument;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class WorldArgument implements Argument<World, World> {
    @Override
    public @NotNull World convert(@NotNull CommandSourceStack stack, @NotNull World nativeValue) throws CommandSyntaxException {
        return nativeValue;
    }

    @Override
    public @NotNull ArgumentType<World> getNativeType() {
        return ArgumentTypes.world();
    }
}
