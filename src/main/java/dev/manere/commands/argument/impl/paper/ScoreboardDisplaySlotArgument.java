package dev.manere.commands.argument.impl.paper;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.manere.commands.argument.Argument;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import org.bukkit.scoreboard.DisplaySlot;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class ScoreboardDisplaySlotArgument implements Argument<DisplaySlot, DisplaySlot> {
    @Override
    public @NotNull DisplaySlot convert(@NotNull CommandSourceStack stack, @NotNull DisplaySlot nativeValue) throws CommandSyntaxException {
        return nativeValue;
    }

    @Override
    public @NotNull ArgumentType<DisplaySlot> getNativeType() {
        return ArgumentTypes.scoreboardDisplaySlot();
    }
}
