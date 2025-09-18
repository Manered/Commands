package dev.manere.commands;

import dev.manere.commands.api.CommandAPI;
import dev.manere.commands.argument.Argument;
import dev.manere.commands.argument.ArgumentResult;
import dev.manere.commands.argument.CommandArgument;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("UnstableApiUsage")
public final class CommandContext<S extends CommandSender> {
    private final S source;
    private final CommandNode node;

    private final String input;

    private final com.mojang.brigadier.context.CommandContext<CommandSourceStack> stack;
    private final Map<String, ArgumentResult> arguments = new ConcurrentHashMap<>();

    public CommandContext(final @NotNull S source, final @NotNull CommandNode node, final @NotNull com.mojang.brigadier.context.CommandContext<CommandSourceStack> stack, final @NotNull String input) {
        this.source = source;
        this.node = node;
        this.input = input;
        this.stack = stack;
    }

    @NotNull
    public Optional<Player> getAsPlayer() {
        return getSource() instanceof Player player ? Optional.of(player) : Optional.empty();
    }

    @NotNull
    public S getSource() {
        return source;
    }

    @NotNull
    public <T> Optional<T> getOptionalArgument(final @NotNull String key, final @NotNull Class<T> type) {
        return getOptionalArgument(type, key);
    }

    @NotNull
    public <T> Optional<T> getOptionalArgument(final @NotNull Class<T> type, final @NotNull String key) {
        if (getCommandArgument(key).isEmpty()) {
            CommandAPI.getInstance().ifPresent(commandAPI -> commandAPI.getPlugin().getLogger().severe("Argument '" + key + "' not found."));
            return Optional.empty();
        }

        final ArgumentResult argumentResult = arguments.get(key);
        if (argumentResult == null) {
            return Optional.empty();
        }

        final var result = argumentResult.result().orElse(null);
        if (result == null) {
            return Optional.empty();
        }

        return Optional.of(type.cast(result));
    }

    @NotNull
    public <T> T getRequiredArgument(final @NotNull String key, final @NotNull Class<T> type) {
        return getRequiredArgument(type, key);
    }

    @NotNull
    public <T> T getRequiredArgument(final @NotNull Class<T> type, final @NotNull String key) {
        if (getCommandArgument(key).isEmpty()) {
            throw new NullPointerException("Argument '" + key + "' not found.");
        }

        final ArgumentResult argumentResult = arguments.get(key);
        if (argumentResult == null) {
            throw new NullPointerException("Argument '" + key + "' not found.");
        }

        final var result = argumentResult.result().orElse(null);
        if (result == null) {
            throw new NullPointerException("Required argument '" + key + "' has no value.");
        }

        return type.cast(result);
    }

    @NotNull
    @ApiStatus.Internal
    public Optional<CommandArgument<? extends Argument<Object, Object>>> getCommandArgument(final @NotNull String key) {
        for (final CommandArgument<?> argument : node.arguments()) {
            if (argument instanceof CommandArgument<?> single) {
                if (single.getKey().equals(key)) return Optional.of((CommandArgument<? extends Argument<Object, Object>>) single);
            }
        }

        return Optional.empty();
    }

    @ApiStatus.Internal
    public void registerArgumentResult(final @NotNull ArgumentResult result) {
        arguments.put(result.key(), result);
    }

    @NotNull
    @ApiStatus.Internal
    public Optional<ArgumentResult> findArgumentResult(final @NotNull String key) {
        return Optional.ofNullable(arguments.get(key));
    }

    @NotNull
    public CommandNode getNode() {
        return node;
    }

    @NotNull
    public String getInput() {
        return input;
    }

    @Override
    public String toString() {
        return "CommandContext[source = " + source + ", node = " + node + ", input = " + input + "]";
    }

    @NotNull
    @ApiStatus.Internal
    public com.mojang.brigadier.context.CommandContext<CommandSourceStack> getStack() {
        return stack;
    }
}
