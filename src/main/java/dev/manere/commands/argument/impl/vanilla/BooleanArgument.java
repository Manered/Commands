package dev.manere.commands.argument.impl.vanilla;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import dev.manere.commands.argument.Argument;
import org.jetbrains.annotations.NotNull;

public class BooleanArgument implements Argument<Boolean, Boolean> {
    @Override
    public @NotNull ArgumentType<Boolean> getNativeType() {
        return BoolArgumentType.bool();
    }
}
