package dev.manere.commands.argument.impl;

import dev.manere.commands.argument.Argument;
import dev.manere.commands.ctx.CommandContext;
import dev.manere.commands.exception.ArgumentParseException;
import dev.manere.commands.handler.AsyncSuggestionHandler;
import dev.manere.commands.handler.Suggestion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class OfflinePlayerArgument implements Argument<OfflinePlayer>, AsyncSuggestionHandler {
    @NotNull
    @Override
    public Set<Class<? super OfflinePlayer>> types() {
        return Argument.newSet(OfflinePlayer.class);
    }

    @Nullable
    @Override
    public OfflinePlayer parse(final @NotNull CommandContext context, final @Nullable String text) throws ArgumentParseException {
        return text == null ? null : Bukkit.getOfflinePlayer(text);
    }

    @Override
    @NotNull
    public CompletableFuture<List<Suggestion>> suggestions(final @NotNull CommandContext context) {
        return CompletableFuture.supplyAsync(() -> {
            final List<Suggestion> completions = new ArrayList<>();

            for (final Player player : Bukkit.getOnlinePlayers()) {
                completions.add(Suggestion.suggestion(player.getName(), player.displayName(), false));
            }

            for (final OfflinePlayer player : Bukkit.getOfflinePlayers()) {
                Bukkit.createProfile(player.getUniqueId()).update().thenAccept(profile -> {
                    final String name = profile.getName();
                    if (name == null) return;
                    completions.add(Suggestion.suggestion(name, false));
                });
            }

            return completions;
        });
    }
}
