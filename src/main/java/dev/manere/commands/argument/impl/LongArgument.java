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

public class LongArgument implements Argument<Long>, SyncSuggestionHandler {
    @NotNull
    @Override
    @Unmodifiable
    public Set<Class<? super Long>> types() {
        return Argument.newSet(Long.class, long.class);
    }

    @Nullable
    @Override
    public Long parse(final @NotNull CommandContext context, final @Nullable String text) throws ArgumentParseException {
        if (text == null) return null;

        try {
            return Long.valueOf(text);
        } catch (final @NotNull Exception e) {
            throw ArgumentParseException.exception(context, "Expected `type: Long` but got " + text);
        }
    }

    @NotNull
    @Override
    @Unmodifiable
    public List<Suggestion> suggestions(final @NotNull CommandContext context) {
        return List.of(Suggestion.suggestion("<number>", true));
    }
}
