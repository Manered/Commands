package dev.manere.commands.argument;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record ArgumentResult(@NotNull String key, @NotNull Optional<Object> result, @NotNull CommandArgument<? extends Argument<Object, Object>> argument) {
    @NotNull
    public static ArgumentResult result(final @NotNull String key, final @Nullable Object result, final @NotNull CommandArgument<? extends Argument<Object, Object>> argument) {
        return new ArgumentResult(key, Optional.ofNullable(result), argument);
    }
}
