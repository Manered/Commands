package dev.manere.commands.argument.impl.vanilla;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import dev.manere.commands.argument.Argument;
import dev.manere.commands.completion.Completion;
import dev.manere.commands.completion.CompletionProvider;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BooleanArgument implements Argument<Boolean, Boolean> {
    @NotNull
    @Override
    public ArgumentType<Boolean> getNativeType() {
        return BoolArgumentType.bool();
    }

    @NotNull
    @Override
    public CompletionProvider<?> getDefaultCompletions() {
        return CompletionProvider.sync(context -> List.of(Completion.completion("true"), Completion.completion("false")));
    }
}
