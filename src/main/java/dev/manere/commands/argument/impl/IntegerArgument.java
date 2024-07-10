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

public class IntegerArgument implements Argument<Integer>, SyncSuggestionHandler {
    @NotNull
    @Override
    public Set<Class<? super Integer>> types() {
        return Argument.newSet(Integer.class, int.class);
    }

    @Nullable
    @Override
    public Integer parse(final @NotNull CommandContext context, final @Nullable String text) throws ArgumentParseException {
        if (text == null) return null;

        try {
            return Integer.valueOf(text);
        } catch (final @NotNull Exception e) {
            throw ArgumentParseException.exception(context, "Expected `type: Int` but got " + text);
        }
    }

    @NotNull
    @Override
    public List<Suggestion> suggestions(final @NotNull CommandContext context) {
        return List.of(
            Suggestion.suggestion("<integer>")
        );
    }
}
