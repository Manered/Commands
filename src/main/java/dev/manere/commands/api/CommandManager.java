package dev.manere.commands.api;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import dev.manere.commands.CommandNode;
import dev.manere.commands.info.CommandData;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    private final List<CommandData> registeredCommands = new ArrayList<>();
    private final JavaPlugin plugin;

    private CommandManager(final @NotNull JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @NotNull
    protected static CommandManager manager(final @NotNull JavaPlugin plugin) {
        return new CommandManager(plugin);
    }

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
}
