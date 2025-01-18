package dev.manere.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public final class CommandRequirements {
    public static <S> boolean requireSender(final @NotNull Class<S> senderRequired, final @NotNull CommandSender source) {
        return !senderRequired.isInstance(source);
    }

    public static boolean requirePlayer(final @NotNull CommandSender source) {
        return requireSender(Player.class, source);
    }

    public static boolean requirePlayer(final @NotNull CommandSender source, final @NotNull Predicate<Player> then) {
        return requireSender(Player.class, source, then);
    }

    public static <S> boolean requireSender(final @NotNull Class<S> senderRequired, final @NotNull CommandSender source, final @NotNull Predicate<S> then) {
        if (!requireSender(senderRequired, source)) {
            try {
                return then.test(senderRequired.cast(source));
            } catch (final ClassCastException e) {
                return true;
            }
        }

        return true;
    }
}
