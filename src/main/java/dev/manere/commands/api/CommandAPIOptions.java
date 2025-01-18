package dev.manere.commands.api;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.HashSet;
import java.util.Set;

public interface CommandAPIOptions {
    @NotNull
    Set<CommandAPIOption<?>> REGISTRY = new HashSet<>();

    @NotNull
    @ApiStatus.Internal
    private static <V> CommandAPIOption<V> register(final @NotNull String key, final @NotNull Class<V> type, final @Nullable V defaultValue) {
        final CommandAPIOption<V> option = new CommandAPIOption<>(key, type, defaultValue);
        REGISTRY.add(option);

        return option;
    }

    @NotNull
    @ApiStatus.Internal
    private static <V> CommandAPIOption<V> register(final @NotNull String key, final @NotNull Class<V> type) {
        return register(key, type, null);
    }

    @NotNull
    @Unmodifiable
    static Set<CommandAPIOption<?>> getRegistry() {
        return Set.copyOf(REGISTRY);
    }
}
