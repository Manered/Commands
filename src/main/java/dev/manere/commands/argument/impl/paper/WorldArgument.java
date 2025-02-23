package dev.manere.commands.argument.impl.paper;

import com.mojang.brigadier.arguments.ArgumentType;
import dev.manere.commands.argument.Argument;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class WorldArgument implements Argument<World, World> {
    @Override
    public @NotNull ArgumentType<World> getNativeType() {
        return ArgumentTypes.world();
    }
}
