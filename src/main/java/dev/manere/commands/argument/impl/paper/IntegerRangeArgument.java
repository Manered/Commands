package dev.manere.commands.argument.impl.paper;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.manere.commands.argument.Argument;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.range.IntegerRangeProvider;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class IntegerRangeArgument implements Argument<IntegerRangeProvider, IntegerRangeProvider> {
    @Override
    public @NotNull IntegerRangeProvider convert(@NotNull CommandSourceStack stack, @NotNull IntegerRangeProvider nativeValue) throws CommandSyntaxException {
        return nativeValue;
    }

    @Override
    public @NotNull ArgumentType<IntegerRangeProvider> getNativeType() {
        return ArgumentTypes.integerRange();
    }
}
