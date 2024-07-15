package dev.manere.commands.info;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import dev.manere.commands.CommandNode;
import dev.manere.commands.api.CommandsAPI;
import org.jetbrains.annotations.NotNull;

/**
 * Holds data related to a command.
 */
public class CommandData {
    private final CommandNode command;
    private final CommandsAPI api;
    private boolean registered;

    /**
     * Constructs a CommandData object with the specified command and API.
     *
     * @param command the command node.
     * @param api     the commands API.
     */
    public CommandData(
        final @NotNull CommandNode command,
        final @NotNull CommandsAPI api
    ) {
        this.command = command;
        this.api = api;
        this.registered = false;
    }

    /**
     * Constructs a CommandData object with the specified command, API, and registration status.
     *
     * @param command    the command node.
     * @param api        the commands API.
     * @param registered whether the command is registered.
     */
    public CommandData(
        final @NotNull CommandNode command,
        final @NotNull CommandsAPI api,
        final boolean registered
    ) {
        this.command = command;
        this.api = api;
        this.registered = registered;
    }

    /**
     * Creates a new CommandData object with the specified command and API.
     *
     * @param command the command node.
     * @param api     the commands API.
     * @return a new CommandData object.
     */
    @NotNull
    public static CommandData data(
        final @NotNull CommandNode command,
        final @NotNull CommandsAPI api
    ) {
        return new CommandData(command, api);
    }

    /**
     * Creates a new CommandData object with the specified command, API, and registration status.
     *
     * @param command    the command node.
     * @param api        the commands API.
     * @param registered whether the command is registered.
     * @return a new CommandData object.
     */
    @NotNull
    public static CommandData data(
        final @NotNull CommandNode command,
        final @NotNull CommandsAPI api,
        final boolean registered
    ) {
        return new CommandData(command, api, registered);
    }

    /**
     * Gets the command node.
     *
     * @return the command node.
     */
    @NotNull
    public CommandNode command() {
        return command;
    }

    /**
     * Gets the commands API.
     *
     * @return the commands API.
     */
    @NotNull
    public CommandsAPI api() {
        return api;
    }

    /**
     * Checks if the command is registered.
     *
     * @return true if the command is registered, false otherwise.
     */
    public boolean registered() {
        return registered;
    }

    /**
     * Sets the registration status of the command.
     *
     * @param registered the new registration status.
     * @return this CommandData object.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandData registered(final boolean registered) {
        this.registered = registered;
        return this;
    }
}
