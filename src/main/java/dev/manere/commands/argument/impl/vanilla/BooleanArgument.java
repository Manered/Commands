package dev.manere.commands.argument.impl.vanilla;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import dev.manere.commands.argument.Argument;
import dev.manere.commands.completion.Completion;
import dev.manere.commands.completion.Suggestions;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BooleanArgument implements Argument<Boolean, Boolean> {
    @Override
    public Boolean convert(@NotNull CommandSourceStack stack, @NotNull Boolean nativeValue) {
        return nativeValue;
    }

    @NotNull
    @Override
    public ArgumentType<Boolean> getNativeType() {
        return BoolArgumentType.bool();
    }

    @NotNull
    @Override
    public Suggestions<?> getDefaultCompletions() {
        return Suggestions.suggest(context -> List.of(Completion.completion("true"), Completion.completion("false")));
    }
}
