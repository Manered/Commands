package dev.manere.commands.argument.impl.vanilla;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import dev.manere.commands.argument.Argument;
import org.jetbrains.annotations.NotNull;

public class LongArgument implements Argument<Long, Long> {
    private long minimum = Long.MIN_VALUE;
    private long maximum = Long.MAX_VALUE;

    public LongArgument(final long minimum, final long maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public LongArgument(final long minimum) {
        this.minimum = minimum;
    }

    public LongArgument() {}

    @Override
    public @NotNull ArgumentType<Long> getNativeType() {
        if (minimum == Integer.MIN_VALUE && maximum == Integer.MAX_VALUE) return LongArgumentType.longArg();
        if (minimum != Integer.MIN_VALUE && maximum == Integer.MAX_VALUE) return LongArgumentType.longArg(minimum);
        return LongArgumentType.longArg(minimum, maximum);
    }
}
