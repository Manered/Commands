package dev.manere.commands.exception;

import dev.manere.commands.argument.Argument;
import dev.manere.commands.ctx.CommandArguments;
import dev.manere.commands.ctx.CommandContext;
import org.jetbrains.annotations.NotNull;

/**
 * Exception thrown when an error occurs while parsing command arguments.

 * @see CommandArguments
 * @see Argument
 */
public class ArgumentParseException extends RuntimeException {
    private final CommandContext context;
    private final String text;

    /**
     * Constructs an ArgumentParseException with the specified context and text.
     *
     * @param context the command context where the exception occurred.
     * @param text    the error message.
     */
    private ArgumentParseException(final @NotNull CommandContext context, final @NotNull String text) {
        this.context = context;
        this.text = text;
    }

    /**
     * Creates a new ArgumentParseException with the specified context and text.
     *
     * @param context the command context where the exception occurred.
     * @param text    the error message.
     * @return a new ArgumentParseException instance.
     */
    @NotNull
    public static ArgumentParseException exception(final @NotNull CommandContext context, final @NotNull String text) {
        return new ArgumentParseException(context, text);
    }

    /**
     * Gets the command context where the exception occurred.
     *
     * @return the command context.
     */
    @NotNull
    public CommandContext context() {
        return context;
    }

    /**
     * Gets the error message.
     *
     * @return the error message.
     */
    @NotNull
    public String text() {
        return text;
    }
}