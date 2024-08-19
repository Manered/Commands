package dev.manere.commands.argument;

import dev.manere.commands.ctx.CommandArguments;
import dev.manere.commands.ctx.CommandContext;
import dev.manere.commands.exception.ArgumentParseException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents an argument for a command.
 *
 * @param <A> the type of the argument.
 *
 * @see CommandContext
 * @see CommandArguments
 * @see ArgumentParseException
 * @see ListArgument
 * @see CommandArgument
 */
public interface Argument<A> {
    /**
     * Creates a new set containing the specified elements.
     *
     * @param elements the elements to be added to the set.
     * @param <E>      the type of elements in the set.
     * @return a set containing the specified elements.
     */
    @NotNull
    @SafeVarargs
    @Unmodifiable
    static <E> Set<E> newSet(final @Nullable E @Nullable ... elements) {
        final Set<E> set = new HashSet<>();
        if (elements == null) return set;

        for (final E element : elements) {
            if (element == null) continue;
            set.add(element);
        }

        return set;
    }

    /**
     * Gets the types that this argument can parse.
     *
     * @return a set of classes representing the types this argument can parse.
     */
    @NotNull
    @Unmodifiable
    Set<Class<? super A>> types();

    /**
     * Parses the argument from the given text in the specified context.
     *
     * @param context the command context.
     * @param text    the text to parse.
     * @return the parsed argument.
     * @throws ArgumentParseException if the argument cannot be parsed.
     */
    @Nullable
    A parse(final @NotNull CommandContext context, final @Nullable String text) throws ArgumentParseException;
}