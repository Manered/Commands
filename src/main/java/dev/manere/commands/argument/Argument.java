package dev.manere.commands.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.manere.commands.completion.Suggestions;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("UnstableApiUsage")
public interface Argument<V, N> {
    @Nullable
    V convert(final @NotNull CommandSourceStack stack, final @NotNull N nativeValue) throws CommandSyntaxException;

    @NotNull
    ArgumentType<N> getNativeType();

    @NotNull
    default Suggestions<?> getDefaultCompletions() {
        return Suggestions.EMPTY;
    }
}
