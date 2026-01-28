package dev.manere.commands.argument.impl.paper;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.manere.commands.argument.Argument;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import org.bukkit.GameMode;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class GameModeArgument implements Argument<GameMode, GameMode> {
    @Override
    public @NotNull GameMode convert(@NotNull CommandSourceStack stack, @NotNull GameMode nativeValue) throws CommandSyntaxException {
        return nativeValue;
    }

    @Override
    public @NotNull ArgumentType<GameMode> getNativeType() {
        return ArgumentTypes.gameMode();
    }
}
