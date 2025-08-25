package dev.manere.commands.argument.impl.vanilla;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import dev.manere.commands.argument.Argument;
import org.jetbrains.annotations.NotNull;

public class DoubleArgument implements Argument<Double, Double> {
    private double minimum = -Double.MAX_VALUE;
    private double maximum = Double.MAX_VALUE;

    public DoubleArgument(final double minimum, final double maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public DoubleArgument(final double minimum) {
        this.minimum = minimum;
    }

    public DoubleArgument() {}

    @Override
    public @NotNull ArgumentType<Double> getNativeType() {
        if (minimum == -Double.MAX_VALUE && maximum == Integer.MAX_VALUE) return DoubleArgumentType.doubleArg();
        if (minimum != -Double.MAX_VALUE && maximum == Integer.MAX_VALUE) return DoubleArgumentType.doubleArg(minimum);
        return DoubleArgumentType.doubleArg(minimum, maximum);
    }
}
