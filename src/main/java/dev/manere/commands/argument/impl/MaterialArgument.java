package dev.manere.commands.argument.impl;

import dev.manere.commands.argument.Argument;
import dev.manere.commands.ctx.CommandContext;
import dev.manere.commands.exception.ArgumentParseException;
import dev.manere.commands.handler.Suggestion;
import dev.manere.commands.handler.SyncSuggestionHandler;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MaterialArgument implements Argument<Material>, SyncSuggestionHandler {
    @NotNull
    @Override
    public Set<Class<? super Material>> types() {
        return Argument.newSet(Material.class);
    }

    @Nullable
    @Override
    public Material parse(final @NotNull CommandContext context, final @Nullable String text) throws ArgumentParseException {
        return text == null ? null : Material.matchMaterial(text.toUpperCase());
    }

    @NotNull
    @Override
    public List<Suggestion> suggestions(final @NotNull CommandContext context) {
        final List<Suggestion> suggestions = new ArrayList<>();

        for (final Material material : Material.values()) {
            suggestions.add(Suggestion.suggestion("minecraft:" + material.name().toLowerCase(), false));
        }

        return suggestions;
    }
}
