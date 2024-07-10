package dev.manere.commands.argument.impl;

import dev.manere.commands.argument.Argument;
import dev.manere.commands.ctx.CommandContext;
import dev.manere.commands.exception.ArgumentParseException;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class OfflinePlayerArgument implements Argument<OfflinePlayer> {
    @NotNull
    @Override
    public Set<Class<? super OfflinePlayer>> types() {
        return Argument.newSet(OfflinePlayer.class);
    }

    @Nullable
    @Override
    public OfflinePlayer parse(final @NotNull CommandContext context, final @Nullable String text) throws ArgumentParseException {
        if (text == null) return null;
        return Bukkit.getOfflinePlayer(text);
    }
}
