package dev.manere.commands.argument.impl;

import dev.manere.commands.argument.Argument;
import dev.manere.commands.ctx.CommandContext;
import dev.manere.commands.exception.ArgumentParseException;
import dev.manere.commands.handler.AsyncSuggestionHandler;
import dev.manere.commands.handler.Suggestion;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class EntityTypeArgument implements Argument<EntityType>, AsyncSuggestionHandler {
    @NotNull
    @Override
    public Set<Class<? super EntityType>> types() {
        return Argument.newSet(EntityType.class);
    }

    @Nullable
    @Override
    public EntityType parse(final @NotNull CommandContext context, final @Nullable String text) throws ArgumentParseException {
        if (text == null) return null;
        for (final EntityType type : EntityType.values()) if (type.name().toLowerCase().equalsIgnoreCase(text)) return type;
        return null;
    }

    @NotNull
    @Override
    public CompletableFuture<List<Suggestion>> suggestions(final @NotNull CommandContext context) {
        return CompletableFuture.supplyAsync(() -> {
            final List<Suggestion> suggestions = new ArrayList<>();

            for (final EntityType type : EntityType.values()) {
                suggestions.add(Suggestion.suggestion(type.name().toLowerCase(), MiniMessage.miniMessage().deserialize(
                    "<dark_gray>" + type.name()
                )));
            }

            return suggestions;
        });
    }
}
