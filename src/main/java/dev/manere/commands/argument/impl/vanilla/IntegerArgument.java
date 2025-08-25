package dev.manere.commands.argument.impl.vanilla;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import dev.manere.commands.argument.Argument;
import dev.manere.commands.completion.Completion;
import dev.manere.commands.completion.CompletionProvider;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class IntegerArgument implements Argument<Integer, Integer> {
    private int minimum = -Integer.MAX_VALUE;
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
        if (minimum == -Integer.MAX_VALUE && maximum == Integer.MAX_VALUE) return IntegerArgumentType.integer();
        if (minimum != -Integer.MAX_VALUE && maximum == Integer.MAX_VALUE) return IntegerArgumentType.integer(minimum);
        return IntegerArgumentType.integer(minimum, maximum);
    }
}
