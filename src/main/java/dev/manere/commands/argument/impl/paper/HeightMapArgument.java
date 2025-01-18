package dev.manere.commands.argument.impl.paper;

import com.mojang.brigadier.arguments.ArgumentType;
import dev.manere.commands.argument.Argument;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import org.bukkit.HeightMap;
import org.jetbrains.annotations.NotNull;

public class HeightMapArgument implements Argument<HeightMap, HeightMap> {
    @Override
    public @NotNull ArgumentType<HeightMap> getNativeType() {
        return ArgumentTypes.heightMap();
    }
}
