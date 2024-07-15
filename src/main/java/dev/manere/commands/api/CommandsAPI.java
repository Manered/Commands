package dev.manere.commands.api;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Represents the API for managing commands.
 */
public abstract class CommandsAPI {
    private final CommandManager manager;

    /**
     * Protected constructor to initialize the CommandsAPI with a CommandManager.
     *
     * @param manager the CommandManager instance.
     */
    protected CommandsAPI(final @NotNull CommandManager manager) {
        this.manager = manager;
        APIHolder.init(this);
        plugin().getServer().getPluginManager().registerEvents(new CommandRegistrar(), plugin());
    }

    /**
     * Creates a new instance of CommandsAPI.
     *
     * @param plugin  the JavaPlugin instance.
     * @param manager the CommandManager instance.
     * @return a new CommandsAPI instance.
     */
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

    /**
     * Creates a new instance of CommandsAPI from a JavaPlugin supplier.
     *
     * @param supplier the supplier of the JavaPlugin.
     * @return a new CommandsAPI instance.
     */
    @NotNull
    public static CommandsAPI api(final @NotNull Supplier<JavaPlugin> supplier) {
        return api(supplier.get());
    }

    /**
     * Creates a new instance of CommandsAPI from a JavaPlugin.
     *
     * @param plugin the JavaPlugin instance.
     * @return a new CommandsAPI instance.
     */
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

    /**
     * Gets the JavaPlugin associated with this API.
     *
     * @return the JavaPlugin.
     */
    @NotNull
    public abstract JavaPlugin plugin();

    /**
     * Executes a handler with the CommandManager.
     *
     * @param handler the handler to execute.
     * @return the current CommandsAPI instance.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandsAPI execute(final @NotNull Consumer<@NotNull CommandManager> handler) {
        handler.accept(manager);
        return this;
    }

    /**
     * Gets the CommandManager associated with this API.
     *
     * @return the CommandManager.
     */
    @NotNull
    public CommandManager manager() {
        return manager;
    }
}