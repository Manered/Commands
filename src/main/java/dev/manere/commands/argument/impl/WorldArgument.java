package dev.manere.commands.argument.impl;

import dev.manere.commands.argument.Argument;
import dev.manere.commands.ctx.CommandContext;
import dev.manere.commands.exception.ArgumentParseException;
import dev.manere.commands.handler.Suggestion;
import dev.manere.commands.handler.SyncSuggestionHandler;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WorldArgument implements Argument<World>, SyncSuggestionHandler {
    @NotNull
    @Override
    public Set<Class<? super World>> types() {
        return Argument.newSet(World.class);
    }

    @Nullable
    @Override
    public World parse(final @NotNull CommandContext context, final @Nullable String text) throws ArgumentParseException {
        if (text == null) return null;
        return Bukkit.getWorld(text.replaceAll("minecraft:", ""));
    }

    @NotNull
    @Override
    public List<Suggestion> suggestions(final @NotNull CommandContext context) {
        final List<Suggestion> suggestions = new ArrayList<>();
        for (final World world : Bukkit.getWorlds()) suggestions.add(Suggestion.suggestion(world.getName(), false));
        return suggestions;
    }
}
