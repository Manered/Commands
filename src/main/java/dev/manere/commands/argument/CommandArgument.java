package dev.manere.commands.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import dev.manere.commands.completion.CompletionProvider;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Supplier;

@SuppressWarnings("UnstableApiUsage")
public interface CommandArgument {
    @NotNull
    default Optional<CompletionProvider<?>> getCompletions() {
        return Optional.empty();
    }

    default boolean isRequired() {
        return true;
    }

    @NotNull
    String getKey();

    @NotNull
    static <V, N> Argument<V, N> buildType(final @NotNull ArgumentType<N> nativeArgumentType) {
        return () -> nativeArgumentType;
    }

    @NotNull
    static <V, N> Argument<V, N> buildType(final @NotNull ArgumentType<N> nativeArgumentType, final @NotNull BiFunction<CommandSourceStack, N, V> converter) {
        return new Argument<>() {
            @Override
            public @NotNull ArgumentType<N> getNativeType() {
                return nativeArgumentType;
            }

            @Override
            public @Nullable V convert(final @NotNull CommandSourceStack stack, final @NotNull N nativeValue) {
                return converter.apply(stack, nativeValue);
            }
        };
    }

    @NotNull
    static <A extends Argument<?, ?>> SingleCommandArgument<A> required(final @NotNull Supplier<A> argument, final @NotNull String key) {
        return new SingleCommandArgument<>(argument, key, null, true);
    }

    @NotNull
    static <A extends Argument<?, ?>> SingleCommandArgument<A> required(final @NotNull Supplier<A> argument, final @NotNull String key, final @NotNull CompletionProvider<?> completions) {
        return new SingleCommandArgument<>(argument, key, completions, true);
    }

    @NotNull
    static <A extends Argument<?, ?>> SingleCommandArgument<A> optional(final @NotNull Supplier<A> argument, final @NotNull String key) {
        return new SingleCommandArgument<>(argument, key, null, false);
    }

    @NotNull
    static <A extends Argument<?, ?>> SingleCommandArgument<A> optional(final @NotNull Supplier<A> argument, final @NotNull String key, final @NotNull CompletionProvider<?> completions) {
        return new SingleCommandArgument<>(argument, key, completions, false);
    }
}
