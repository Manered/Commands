package dev.manere.commands.argument.impl;

import dev.manere.commands.argument.Argument;
import dev.manere.commands.ctx.CommandContext;
import dev.manere.commands.exception.ArgumentParseException;
import dev.manere.commands.handler.Suggestion;
import dev.manere.commands.handler.SyncSuggestionHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Set;

public class StringArgument implements Argument<String>, SyncSuggestionHandler {
    @NotNull
    @Override
    @Unmodifiable
    public Set<Class<? super String>> types() {
        return Argument.newSet(String.class);
    }

    @Nullable
    @Override
    public String parse(final @NotNull CommandContext context, final @Nullable String text) throws ArgumentParseException {
        return text;
    }

    @NotNull
    @Override
    @Unmodifiable
    public List<Suggestion> suggestions(final @NotNull CommandContext context) {
        return List.of(
            Suggestion.suggestion("<string>")
        );
    }
}
