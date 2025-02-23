package dev.manere.commands.argument.impl.paper;

import com.mojang.brigadier.arguments.ArgumentType;
import dev.manere.commands.argument.Argument;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.range.DoubleRangeProvider;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class DoubleRangeArgument implements Argument<DoubleRangeProvider, DoubleRangeProvider> {
    @Override
    public @NotNull ArgumentType<DoubleRangeProvider> getNativeType() {
        return ArgumentTypes.doubleRange();
    }
}
