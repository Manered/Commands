package dev.manere.commands;

import dev.manere.commands.argument.CommandArgument;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface BasicCommandNode {
    @NotNull
    String getLiteral();

    @NotNull
    default Optional<String> getPermission() {
        return Optional.empty();
    }

    @NotNull
    default Collection<String> getAliases() {
        return List.of();
    }

    @NotNull
    @Unmodifiable
    default Collection<? extends CommandArgument> getArguments() {
        return List.of();
    }

    @NotNull
    @Unmodifiable
    default Collection<?> getChildren() {
        return List.of();
    }

    // If you want to set the description, add any filters, etc.
    default void configure(final @NotNull CommandNode node) {}

    void execute(final @NotNull CommandContext<? extends CommandSender> context);
}
