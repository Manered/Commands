package dev.manere.commands.info;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import dev.manere.commands.CommandNode;
import dev.manere.commands.api.CommandsAPI;
import org.jetbrains.annotations.NotNull;

public final class CommandData {
    private final CommandNode command;
    private final CommandsAPI api;
    private boolean registered;

    public CommandData(
        final @NotNull CommandNode command,
        final @NotNull CommandsAPI api
    )
    {
        this.command = command;
        this.api = api;
        this.registered = false;
    }

    public CommandData(
        final @NotNull CommandNode command,
        final @NotNull CommandsAPI api,
        final boolean registered
    )
    {
        this.command = command;
        this.api = api;
        this.registered = registered;
    }

    @NotNull
    public static CommandData data(
        final @NotNull CommandNode command,
        final @NotNull CommandsAPI api
    )
    {
        return new CommandData(command, api);
    }

    @NotNull
    public static CommandData data(
        final @NotNull CommandNode command,
        final @NotNull CommandsAPI api,
        final boolean registered
    )
    {
        return new CommandData(command, api, registered);
    }

    @NotNull
    public CommandNode command() {
        return command;
    }

    @NotNull
    public CommandsAPI api() {
        return api;
    }

    public boolean registered() {
        return registered;
    }

    @NotNull
    @CanIgnoreReturnValue
    public CommandData registered(final boolean registered) {
        this.registered = registered;
        return this;
    }
}
