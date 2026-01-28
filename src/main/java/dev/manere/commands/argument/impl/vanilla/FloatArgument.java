package dev.manere.commands.argument.impl.vanilla;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import dev.manere.commands.argument.Argument;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.jetbrains.annotations.NotNull;

public class FloatArgument implements Argument<Float, Float> {
    private float minimum = -Float.MAX_VALUE;
    private float maximum = Float.MAX_VALUE;

    public FloatArgument(final float minimum, final float maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public FloatArgument(final float minimum) {
        this.minimum = minimum;
    }

    public FloatArgument() {}

    @Override
    public Float convert(@NotNull CommandSourceStack stack, @NotNull Float nativeValue) {
        return nativeValue;
    }

    @Override
    public @NotNull ArgumentType<Float> getNativeType() {
        if (minimum == -Float.MAX_VALUE && maximum == Float.MAX_VALUE) return FloatArgumentType.floatArg();
        if (minimum != -Float.MAX_VALUE && maximum == Float.MAX_VALUE) return FloatArgumentType.floatArg(minimum);
        return FloatArgumentType.floatArg(minimum, maximum);
    }
}
