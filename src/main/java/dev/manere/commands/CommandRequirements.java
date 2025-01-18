package dev.manere.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class CommandRequirements {
    public static <S> boolean requireSender(final @NotNull Class<S> senderRequired, final @NotNull CommandContext<? extends CommandSender> context) {
        return !senderRequired.isInstance(context.getSource());
    }

    public static boolean requirePlayer(final @NotNull CommandContext<? extends CommandSender> context) {
        return requireSender(Player.class, context);
    }
}
