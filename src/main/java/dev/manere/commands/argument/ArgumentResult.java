package dev.manere.commands.argument;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public record ArgumentResult(@NotNull String key, @NotNull Optional<Object> result, @NotNull SingleCommandArgument<? extends Argument<Object, Object>> argument) {
    @NotNull
    public static ArgumentResult result(final @NotNull String key, final @NotNull Optional<Object> result, final @NotNull SingleCommandArgument<? extends Argument<Object, Object>> argument) {
        return new ArgumentResult(key, result, argument);
    }
}
