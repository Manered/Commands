package dev.manere.commands.api;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.HashSet;
import java.util.Set;

public final class CommandAPIOptions {
    @NotNull
    public static final Set<CommandAPIOption<?>> REGISTRY = new HashSet<>();

    @NotNull
    public static final CommandAPIOption<Boolean> SILENT_LOGS = register("silent_logs", false);

    @NotNull
    @ApiStatus.Internal
    public static <V> CommandAPIOption<V> register(final @NotNull String key, final @Nullable V defaultValue) {
        final CommandAPIOption<V> option = new CommandAPIOption<>(key, defaultValue);
        REGISTRY.add(option);

        return option;
    }

    @NotNull
    @ApiStatus.Internal
    public static <V> CommandAPIOption<V> register(final @NotNull String key) {
        return register(key, null);
    }

    @NotNull
    @Unmodifiable
    public static Set<CommandAPIOption<?>> getRegistry() {
        return Set.copyOf(REGISTRY);
    }
}
