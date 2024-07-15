package dev.manere.commands.argument;

import dev.manere.commands.ctx.CommandContext;
import dev.manere.commands.exception.ArgumentParseException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

/**
 * Represents a list argument for a command.
 *
 * @param <E> the type of elements in the list.
 */
public interface ListArgument<E> extends Argument<List<E>> {
    /**
     * Unsupported operation for ListArgument types.
     *
     * @return throws UnsupportedOperationException.
     */
    @NotNull
    @Override
    default Set<Class<? super List<E>>> types() {
        throw new UnsupportedOperationException();
    }

    /**
     * Parses an element of the list argument from the specified start index in the context.
     *
     * @param context the command context.
     * @param start   the start index.
     * @return the parsed element.
     * @throws ArgumentParseException if the element cannot be parsed.
     */
    @Nullable
    E parse(final @NotNull CommandContext context, final int start) throws ArgumentParseException;

    /**
     * Unsupported operation for ListArgument parse from text.
     *
     * @param context the command context.
     * @param text    the text to parse.
     * @return throws UnsupportedOperationException.
     */
    @Nullable
    default List<E> parse(final @NotNull CommandContext context, final @Nullable String text) {
        throw new UnsupportedOperationException();
    }
}
