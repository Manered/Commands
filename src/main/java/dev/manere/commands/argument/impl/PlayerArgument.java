package dev.manere.commands.argument.impl;

import dev.manere.commands.argument.Argument;
import dev.manere.commands.ctx.CommandContext;
import dev.manere.commands.exception.ArgumentParseException;
import dev.manere.commands.handler.Suggestion;
import dev.manere.commands.handler.SyncSuggestionHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PlayerArgument implements Argument<Player>, SyncSuggestionHandler {
    @NotNull
    @Override
    public Set<Class<? super Player>> types() {
        return Argument.newSet(Player.class);
    }

    @Nullable
    @Override
    public Player parse(final @NotNull CommandContext context, final @Nullable String text) throws ArgumentParseException {
        return text == null ? null : Bukkit.getPlayerExact(text);
    }

    @NotNull
    @Override
    public List<Suggestion> suggestions(final @NotNull CommandContext context) {
        final List<Suggestion> suggestions = new ArrayList<>();

        for (final Player player : Bukkit.getOnlinePlayers()) {
            suggestions.add(Suggestion.suggestion(player.getName(), player.displayName(), false));
        }

        return suggestions;
    }
}
