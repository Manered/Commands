package dev.manere.commands.argument.impl;

import dev.manere.commands.argument.Argument;
import dev.manere.commands.ctx.CommandContext;
import dev.manere.commands.exception.ArgumentParseException;
import dev.manere.commands.handler.Suggestion;
import dev.manere.commands.handler.SyncSuggestionHandler;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.GameMode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GameModeArgument implements Argument<GameMode>, SyncSuggestionHandler {
    @NotNull
    @Override
    public Set<Class<? super GameMode>> types() {
        return Argument.newSet(GameMode.class);
    }

    @Nullable
    @Override
    public GameMode parse(final @NotNull CommandContext context, final @Nullable String text) throws ArgumentParseException {
        return text == null ? null
            : text.equalsIgnoreCase("survival") ? GameMode.SURVIVAL
            : text.equalsIgnoreCase("spectator") ? GameMode.SPECTATOR
            : text.equalsIgnoreCase("creative") ? GameMode.CREATIVE
            : text.equalsIgnoreCase("adventure") ? GameMode.ADVENTURE
            : null;
    }

    @NotNull
    @Override
    public List<Suggestion> suggestions(final @NotNull CommandContext context) {
        final List<Suggestion> suggestions = new ArrayList<>();

        for (final GameMode mode : GameMode.values()) {
            suggestions.add(Suggestion.suggestion(mode.name().toLowerCase(), MiniMessage.miniMessage().deserialize(
                "<dark_gray>" + mode.name()
            )));
        }

        return suggestions;
    }
}
