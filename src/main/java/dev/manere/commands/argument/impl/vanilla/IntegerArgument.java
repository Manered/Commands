package dev.manere.commands.argument.impl.vanilla;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import dev.manere.commands.argument.Argument;
import org.jetbrains.annotations.NotNull;

public class IntegerArgument implements Argument<Integer, Integer> {
    private int minimum = Integer.MIN_VALUE;
    private int maximum = Integer.MAX_VALUE;

    public IntegerArgument(final int minimum, final int maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public IntegerArgument(final int minimum) {
        this.minimum = minimum;
    }

    public IntegerArgument() {}

    @Override
    public @NotNull ArgumentType<Integer> getNativeType() {
        if (minimum == Integer.MIN_VALUE && maximum == Integer.MAX_VALUE) return IntegerArgumentType.integer();
        if (minimum != Integer.MIN_VALUE && maximum == Integer.MAX_VALUE) return IntegerArgumentType.integer(minimum);
        return IntegerArgumentType.integer(minimum, maximum);
    }
}
