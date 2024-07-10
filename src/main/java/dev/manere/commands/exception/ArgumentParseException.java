package dev.manere.commands.exception;

import dev.manere.commands.ctx.CommandContext;
import org.jetbrains.annotations.NotNull;

public class ArgumentParseException extends RuntimeException {
    private final CommandContext context;
    private final String text;

    private ArgumentParseException(final @NotNull CommandContext context, final @NotNull String text) {
        this.context = context;
        this.text = text;
    }

    @NotNull
    public static ArgumentParseException exception(final @NotNull CommandContext context, final @NotNull String text) {
        return new ArgumentParseException(context, text);
    }

    @NotNull
    public CommandContext context() {
        return context;
    }

    @NotNull
    public String text() {
        return text;
    }
}
