package dev.manere.commands.api;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import dev.manere.commands.CommandNode;
import dev.manere.commands.info.CommandData;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the registration and handling of commands.
 */
public class CommandManager {
    private final List<CommandData> registeredCommands = new ArrayList<>();
    private final JavaPlugin plugin;

    /**
     * Private constructor to initialize the CommandManager with a plugin.
     *
     * @param plugin the JavaPlugin instance.
     */
    private CommandManager(final @NotNull JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Creates a new instance of CommandManager.
     *
     * @param plugin the JavaPlugin instance.
     * @return a new CommandManager instance.
     */
    @NotNull
    protected static CommandManager manager(final @NotNull JavaPlugin plugin) {
        return new CommandManager(plugin);
    }

    /**
     * Registers an array of CommandNodes.
     *
     * @param commands the CommandNodes to register.
     * @return the current CommandManager instance.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandManager register(final @NotNull CommandNode @NotNull... commands) {
        for (final CommandNode command : commands) {
            registeredCommands.add(CommandData.data(
                command,
                CommandsAPI.of(plugin, this),
                false
            ));
        }

        for (final CommandData data : registeredCommands) {
            if (data.registered()) continue;
            CommandRegistrar.register(data);
        }

        return this;
    }

    /**
     * Finds a CommandNode by its alias.
     *
     * @param alias the alias of the command.
     * @return the found CommandNode, or null if not found.
     */
    @Nullable
    public CommandNode find(final @NotNull String alias) {
        for (final CommandData data : registeredCommands) {
            final CommandNode node = data.command();
            final List<String> literals = new ArrayList<>();

            literals.add(node.literal());
            literals.addAll(node.info().aliases());

            if (literals.contains(alias)) return node;
        }

        return null;
    }
}