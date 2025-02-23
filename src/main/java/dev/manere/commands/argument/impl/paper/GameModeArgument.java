package dev.manere.commands.argument.impl.paper;

import com.mojang.brigadier.arguments.ArgumentType;
import dev.manere.commands.argument.Argument;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import org.bukkit.GameMode;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class GameModeArgument implements Argument<GameMode, GameMode> {
    @Override
    public @NotNull ArgumentType<GameMode> getNativeType() {
        return ArgumentTypes.gameMode();
    }
}
