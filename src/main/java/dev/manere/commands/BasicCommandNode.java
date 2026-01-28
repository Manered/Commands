package dev.manere.commands;

import dev.manere.commands.api.CommandAPI;
import dev.manere.commands.argument.CommandArgument;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public abstract class BasicCommandNode {
    @NotNull
    public abstract String getLiteral();

    @NotNull
    public Optional<String> getPermission() {
        return Optional.empty();
    }

    @NotNull
    public Collection<String> getAliases() {
        return List.of();
    }

    @NotNull
    public Collection<? extends CommandArgument<?>> getArguments() {
        return List.of();
    }

    @NotNull
    public Collection<?> getChildren() {
        return List.of();
    }

    public void configure(final @NotNull CommandNode node) {}

    public abstract void execute(final @NotNull CommandContext<? extends CommandSender> context);

    public final void register() {
        CommandAPI.getInstance().ifPresent(this::register);
    }

    public final void register(final @NotNull CommandAPI api) {
        api.register(this);
    }
}
