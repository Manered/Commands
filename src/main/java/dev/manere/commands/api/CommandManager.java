package dev.manere.commands.api;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import dev.manere.commands.BasicCommandNode;
import dev.manere.commands.CommandNode;
import dev.manere.commands.argument.CommandArgument;
import dev.manere.commands.ctx.CommandArguments;
import dev.manere.commands.info.CommandData;
import dev.manere.commands.info.CommandInfo;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Manages the registration and handling of commands.
 *
 * @see CommandsAPI
 * @see CommandNode
 * @see BasicCommandNode
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
    @ApiStatus.Internal
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
     * Registers an array of {@link BasicCommandNode} roots and their associated subcommands.
     * This method converts each {@link BasicCommandNode} to a {@link CommandNode} and registers them.
     *
     * @param roots the array of BasicCommandNode roots to register.
     * @return the current CommandManager instance for method chaining.
     */
    @NotNull    
    @CanIgnoreReturnValue
    public CommandManager register(final @NotNull BasicCommandNode @NotNull ... roots) {
        for (final BasicCommandNode root : roots) {
            final CommandNode commandNode = convert(root);

            registeredCommands.add(CommandData.data(
                commandNode,
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

    @NotNull
    @ApiStatus.Internal
    private CommandNode convert(final @NotNull BasicCommandNode basicNode) {
        final CommandNode node = CommandNode.of(basicNode.literal(), Objects.requireNonNullElse(basicNode.info(), CommandInfo.info()))
            .handler(basicNode);

        for (final CommandArgument<?> argument : basicNode.arguments()) node.argument(argument);
        for (final BasicCommandNode children : basicNode.children()) node.subcommand(convert(children));

        return node;
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