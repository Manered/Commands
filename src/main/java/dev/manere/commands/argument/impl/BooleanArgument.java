package dev.manere.commands.argument.impl;

import dev.manere.commands.argument.Argument;
import dev.manere.commands.ctx.CommandContext;
import dev.manere.commands.exception.ArgumentParseException;
import dev.manere.commands.handler.Suggestion;
import dev.manere.commands.handler.SyncSuggestionHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class BooleanArgument implements Argument<Boolean>, SyncSuggestionHandler {
    @NotNull
    @Override
    public Set<Class<? super Boolean>> types() {
        return Argument.newSet(Boolean.class, boolean.class);
    }

    @Nullable
    @Override
    public Boolean parse(final @NotNull CommandContext context, final @Nullable String text) throws ArgumentParseException {
        return text == null ? null : text.equalsIgnoreCase("true") ? Boolean.valueOf(true) : text.equalsIgnoreCase("false") ? false : null;
    }

    @NotNull
    @Override
    public List<Suggestion> suggestions(final @NotNull CommandContext ctx) {
        return List.of(Suggestion.suggestion("true", false), Suggestion.suggestion("false", false));
    }
}
