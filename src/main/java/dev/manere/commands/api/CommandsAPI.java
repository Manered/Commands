package dev.manere.commands.api;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class CommandsAPI {
    private final CommandManager manager;

    protected CommandsAPI(final @NotNull CommandManager manager) {
        this.manager = manager;
    }

    @NotNull
    public static CommandsAPI of(final @NotNull JavaPlugin plugin, final @NotNull CommandManager manager) {
        return new CommandsAPI(manager) {
            @NotNull
            @Override
            public JavaPlugin plugin() {
                return plugin;
            }
        };
    }

    @NotNull
    public static CommandsAPI api(final @NotNull Supplier<JavaPlugin> supplier) {
        return api(supplier.get());
    }

    @NotNull
    public static CommandsAPI api(final @NotNull JavaPlugin plugin) {
        return new CommandsAPI(CommandManager.manager(plugin)) {
            @NotNull
            @Override
            public JavaPlugin plugin() {
                return plugin;
            }
        };
    }

    @NotNull
    public abstract JavaPlugin plugin();

    @NotNull
    @CanIgnoreReturnValue
    public CommandsAPI execute(final @NotNull Consumer<@NotNull CommandManager> handler) {
        handler.accept(manager);
        return this;
    }

    @NotNull
    public CommandManager manager() {
        return manager;
    }
}
