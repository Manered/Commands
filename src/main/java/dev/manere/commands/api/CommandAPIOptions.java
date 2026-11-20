package dev.manere.commands.api;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
    public static final CommandAPIOption<Boolean> SILENT_LOGS = register(
        "silent_logs",
        true
    );

    @NotNull
    public static final CommandAPIOption<Boolean> DEBUG_TREE = register(
        "debug_tree",
        false
    );

    @NotNull
    public static final CommandAPIOption<Component> PLAYER_ONLY_COMMAND_MESSAGE = register(
        "player_only_command_message",
        Component.text("This command can only be executed by players.", NamedTextColor.RED)
    );

    @NotNull
    public static final CommandAPIOption<Component> EXECUTOR_MISMATCH_MESSAGE = register(
        "executor_mismatch_message",
        Component.text("You cannot execute this command.", NamedTextColor.RED)
    );

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
