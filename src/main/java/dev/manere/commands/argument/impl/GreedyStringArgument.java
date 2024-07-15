package dev.manere.commands.argument.impl;

import dev.manere.commands.argument.ListArgument;
import dev.manere.commands.ctx.CommandContext;
import dev.manere.commands.exception.ArgumentParseException;
import dev.manere.commands.handler.Suggestion;
import dev.manere.commands.handler.SyncSuggestionHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GreedyStringArgument implements ListArgument<String>, SyncSuggestionHandler {
    @Nullable
    @Override
    public String parse(final @NotNull CommandContext context, final int start) throws ArgumentParseException {
        final List<String> arguments = context.argumentsFromOffset();
        final List<String> list = new ArrayList<>();
        final int size = arguments.size();

        for (int pos = start; pos <= size - 1; pos++) list.add(arguments.get(pos));
        return String.join(" ", list);
    }

    @NotNull
    @Override
    public List<Suggestion> suggestions(final @NotNull CommandContext context) {
        return List.of(Suggestion.suggestion("<text>", true));
    }
}
