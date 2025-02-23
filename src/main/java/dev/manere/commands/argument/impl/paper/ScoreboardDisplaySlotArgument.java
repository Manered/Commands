package dev.manere.commands.argument.impl.paper;

import com.mojang.brigadier.arguments.ArgumentType;
import dev.manere.commands.argument.Argument;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import org.bukkit.scoreboard.DisplaySlot;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class ScoreboardDisplaySlotArgument implements Argument<DisplaySlot, DisplaySlot> {
    @Override
    public @NotNull ArgumentType<DisplaySlot> getNativeType() {
        return ArgumentTypes.scoreboardDisplaySlot();
    }
}
