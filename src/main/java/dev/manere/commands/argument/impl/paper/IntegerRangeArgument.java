package dev.manere.commands.argument.impl.paper;

import com.mojang.brigadier.arguments.ArgumentType;
import dev.manere.commands.argument.Argument;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.range.IntegerRangeProvider;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class IntegerRangeArgument implements Argument<IntegerRangeProvider, IntegerRangeProvider> {
    @Override
    public @NotNull ArgumentType<IntegerRangeProvider> getNativeType() {
        return ArgumentTypes.integerRange();
    }
}
