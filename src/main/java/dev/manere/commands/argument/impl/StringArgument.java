package dev.manere.commands.argument.impl;

import dev.manere.commands.argument.Argument;
import dev.manere.commands.ctx.CommandContext;
import dev.manere.commands.exception.ArgumentParseException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class StringArgument implements Argument<String> {
    @NotNull
    @Override
    public Set<Class<? super String>> types() {
        return Argument.newSet(String.class);
    }

    @Nullable
    @Override
    public String parse(final @NotNull CommandContext context, final @Nullable String text) throws ArgumentParseException {
        return text;
    }
}
