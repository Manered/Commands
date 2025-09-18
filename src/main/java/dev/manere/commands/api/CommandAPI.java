package dev.manere.commands.api;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.manere.commands.BasicCommandNode;
import dev.manere.commands.CommandNode;
import dev.manere.commands.argument.CommandArgument;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public final class CommandAPI {
    @Nullable
    public static CommandAPI INSTANCE = null;

    private final JavaPlugin plugin;
    private final CommandAPIConfig config;

    private final Set<CommandNode> registeredCommands = new HashSet<>();

    private CommandAPI(final @NotNull JavaPlugin plugin, final @NotNull CommandAPIConfig config) {
        this.plugin = plugin;
        this.config = config;

        INSTANCE = this;
    }

    @NotNull
    @CanIgnoreReturnValue
    public static CommandAPI register(final @NotNull JavaPlugin plugin, final @NotNull Consumer<CommandAPIConfig> configurator) {
        final CommandAPIConfig config = new CommandAPIConfig();
        configurator.accept(config);

        return register(plugin, config);
    }

    @NotNull
    @CanIgnoreReturnValue
    public static CommandAPI register(final @NotNull JavaPlugin plugin, final @NotNull CommandAPIConfig config) {
        return new CommandAPI(plugin, config);
    }

    @NotNull
    @CanIgnoreReturnValue
    public static CommandAPI register(final @NotNull JavaPlugin plugin) {
        return register(plugin, config -> {});
    }

    @NotNull
    public static Optional<CommandAPI> getInstance() {
        return Optional.ofNullable(INSTANCE);
    }

    @NotNull
    public CommandAPIConfig getConfig() {
        return config;
    }

    @NotNull
    public JavaPlugin getPlugin() {
        return plugin;
    }

    public void register(final @NotNull CommandNode @NotNull ... roots) {
        for (final CommandNode root : roots) register(root);
    }

    public void register(final @NotNull BasicCommandNode @NotNull ... roots) {
        for (final BasicCommandNode root : roots) register(root);
    }

    @SuppressWarnings("UnstableApiUsage")
    public void register(final @NotNull CommandNode root) {
        final boolean silentLogs = config.get(CommandAPIOptions.SILENT_LOGS).orElse(true);

        getPlugin().getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS.newHandler(event -> {
            final Commands commands = event.registrar();

            final LiteralCommandNode<CommandSourceStack> converted = CommandAPIBrigadier.convert(root);

            commands.register(converted, root.description().orElse(null), root.aliases());
            if (!silentLogs) plugin.getLogger().info("Registered command " + root.literal() + ".");

            for (final Player player : Bukkit.getOnlinePlayers()) {
                if (!silentLogs) plugin.getLogger().info("Updating commands...");
                player.updateCommands();
                if (!silentLogs) plugin.getLogger().info("Updated commands.");
            }
        }));

        registeredCommands.add(root);
    }

    public void register(final @NotNull BasicCommandNode root) {
        register(convert(root));
    }

    @SuppressWarnings("UnstableApiUsage")
    public void unregister(final @NotNull String label) {
        final boolean silentLogs = config.get(CommandAPIOptions.SILENT_LOGS).orElse(true);

        getPlugin().getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS.newHandler(event -> {
            final Commands commands = event.registrar();

            registeredCommands.removeIf(node ->
                node.literal().equalsIgnoreCase(label) || node.aliases().stream().anyMatch(alias -> alias.equalsIgnoreCase(label))
            );

            try {
                final var commandMap = Bukkit.getCommandMap();
                final var bukkitCommand = commandMap.getCommand(label);

                if (bukkitCommand != null) {
                    bukkitCommand.unregister(commandMap);
                    commandMap.getKnownCommands().remove(label.toLowerCase());

                    for (final String alias : bukkitCommand.getAliases()) {
                        commandMap.getKnownCommands().remove(alias.toLowerCase());
                    }

                    if (!silentLogs) plugin.getLogger().info("Unregistered Bukkit command " + label + ".");
                }
            } catch (final Exception e) {
                plugin.getLogger().warning("Failed to unregister Bukkit command " + label + ": " + e.getMessage());
            }

            try {
                final var dispatcher = commands.getDispatcher();
                final var root = dispatcher.getRoot();

                removeChild(root, label.toLowerCase());

                if (!silentLogs) plugin.getLogger().info("Unregistered Brigadier command " + label + ".");
            } catch (final Exception e) {
                plugin.getLogger().warning("Failed to unregister Brigadier command " + label + ": " + e.getMessage());
            }

            for (final Player player : Bukkit.getOnlinePlayers()) {
                if (!silentLogs) plugin.getLogger().info("Updating commands...");
                player.updateCommands();
                if (!silentLogs) plugin.getLogger().info("Updated commands.");
            }
        }));
    }

    @SuppressWarnings("unchecked")
    private <S> void removeChild(final com.mojang.brigadier.tree.CommandNode<S> parent, final String name) {
        try {
            final var childrenField = com.mojang.brigadier.tree.CommandNode.class.getDeclaredField("children");
            final var literalsField = com.mojang.brigadier.tree.CommandNode.class.getDeclaredField("literals");
            final var argumentsField = com.mojang.brigadier.tree.CommandNode.class.getDeclaredField("arguments");

            childrenField.setAccessible(true);
            literalsField.setAccessible(true);
            argumentsField.setAccessible(true);

            final var children = (Map<String, com.mojang.brigadier.tree.CommandNode<S>>) childrenField.get(parent);
            final var literals = (Map<String, com.mojang.brigadier.tree.LiteralCommandNode<S>>) literalsField.get(parent);
            final var arguments = (Map<String, com.mojang.brigadier.tree.ArgumentCommandNode<S, ?>>) argumentsField.get(parent);

            children.remove(name);
            literals.remove(name);
            arguments.remove(name);
        } catch (final ReflectiveOperationException e) {
            plugin.getLogger().warning("Failed to unregister Brigadier command " + name + ": " + e.getMessage());
        }
    }

    @NotNull
    @ApiStatus.Internal
    private CommandNode convert(final @NotNull BasicCommandNode basicNode) {
        final CommandNode node = CommandNode.of(basicNode.getLiteral(), basicNode::execute)
            .permission(basicNode.getPermission().orElse(null))
            .aliases(basicNode.getAliases());

        basicNode.configure(node);

        for (final CommandArgument<?> argument : basicNode.getArguments()) node.argument(argument);
        for (final Object child : basicNode.getChildren()) {
            if (child instanceof BasicCommandNode basicChild) {
                node.subcommand(convert(basicChild));
            } else if (child instanceof CommandNode nodeChild) {
                node.subcommand(nodeChild);
            }
        }

        return node;
    }

    @NotNull
    @Unmodifiable
    public Set<CommandNode> getRegisteredCommands() {
        return Set.copyOf(registeredCommands);
    }
}
