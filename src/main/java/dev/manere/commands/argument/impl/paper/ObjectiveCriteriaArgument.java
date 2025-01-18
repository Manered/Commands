package dev.manere.commands.argument.impl.paper;

import com.mojang.brigadier.arguments.ArgumentType;
import dev.manere.commands.argument.Argument;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import org.bukkit.scoreboard.Criteria;
import org.jetbrains.annotations.NotNull;

public class ObjectiveCriteriaArgument implements Argument<Criteria, Criteria> {
    @Override
    public @NotNull ArgumentType<Criteria> getNativeType() {
        return ArgumentTypes.objectiveCriteria();
    }
}
