package dev.manere.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class CommandRequirements {
    public static <S> boolean requireSender(final @NotNull Class<S> senderRequired, final @NotNull CommandSender source) {
        return !senderRequired.isInstance(source);
    }

    public static boolean requirePlayer(final @NotNull CommandSender source) {
        return requireSender(Player.class, source);
    }
}
