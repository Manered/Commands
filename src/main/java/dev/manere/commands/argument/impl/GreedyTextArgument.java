package dev.manere.commands.argument.impl;

import dev.manere.commands.argument.ListArgument;
import dev.manere.commands.ctx.CommandContext;
import dev.manere.commands.exception.ArgumentParseException;
import dev.manere.commands.handler.Suggestion;
import dev.manere.commands.handler.SyncSuggestionHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public class GreedyTextArgument implements ListArgument<Component>, SyncSuggestionHandler {
    @Nullable
    @Override
    @Unmodifiable
    public Component parse(final @NotNull CommandContext context, final int start) throws ArgumentParseException {
        final String text = new GreedyStringArgument().parse(context, start);
        return text != null ? MiniMessage.miniMessage().deserialize(text) : null;
    }

    @NotNull
    @Override
    @Unmodifiable
    public List<Suggestion> suggestions(final @NotNull CommandContext context) {
        return List.of(Suggestion.suggestion("<text>", true));
    }
}
