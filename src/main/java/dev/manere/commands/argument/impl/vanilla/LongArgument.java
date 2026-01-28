package dev.manere.commands.argument.impl.vanilla;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import dev.manere.commands.argument.Argument;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.jetbrains.annotations.NotNull;

public class LongArgument implements Argument<Long, Long> {
    private long minimum = -Long.MAX_VALUE;
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
    public Long convert(@NotNull CommandSourceStack stack, @NotNull Long nativeValue) {
        return nativeValue;
    }

    @Override
    public @NotNull ArgumentType<Long> getNativeType() {
        if (minimum == -Long.MAX_VALUE && maximum == Long.MAX_VALUE) return LongArgumentType.longArg();
        if (minimum != -Long.MAX_VALUE && maximum == Long.MAX_VALUE) return LongArgumentType.longArg(minimum);
        return LongArgumentType.longArg(minimum, maximum);
    }
}
