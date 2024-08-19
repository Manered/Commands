package dev.manere.commands.argument.impl;

import dev.manere.commands.argument.Argument;
import dev.manere.commands.ctx.CommandContext;
import dev.manere.commands.exception.ArgumentParseException;
import dev.manere.commands.handler.Suggestion;
import dev.manere.commands.handler.SyncSuggestionHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Set;

public class DoubleArgument implements Argument<Double>, SyncSuggestionHandler {
    @NotNull
    @Override
    @Unmodifiable
    public Set<Class<? super Double>> types() {
        return Argument.newSet(Double.class, double.class);
    }

    @Nullable
    @Override
    public Double parse(final @NotNull CommandContext context, final @Nullable String text) throws ArgumentParseException {
        if (text == null) return null;

        try {
            return Double.valueOf(text);
        } catch (final @NotNull Exception e) {
            throw ArgumentParseException.exception(context, "Expected `type: Double` but got " + text);
        }
    }

    @NotNull
    @Override
    @Unmodifiable
    public List<Suggestion> suggestions(final @NotNull CommandContext context) {
        return List.of(Suggestion.suggestion("<number>", true));
    }
}
