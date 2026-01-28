package dev.manere.commands.argument.impl.paper;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.manere.commands.argument.Argument;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import org.bukkit.HeightMap;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class HeightMapArgument implements Argument<HeightMap, HeightMap> {
    @Override
    public @NotNull HeightMap convert(@NotNull CommandSourceStack stack, @NotNull HeightMap nativeValue) throws CommandSyntaxException {
        return nativeValue;
    }

    @Override
    public @NotNull ArgumentType<HeightMap> getNativeType() {
        return ArgumentTypes.heightMap();
    }
}
