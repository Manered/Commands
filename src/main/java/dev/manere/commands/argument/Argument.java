package dev.manere.commands.argument;

import dev.manere.commands.ctx.CommandContext;
import dev.manere.commands.exception.ArgumentParseException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public interface Argument<A> {
    @NotNull
    @SafeVarargs
    static <E> Set<E> newSet(final @Nullable E @Nullable ... elements) {
        final Set<E> set = new HashSet<>();
        if (elements == null) return set;

        for (final E element : elements) {
            if (element == null) continue;
            set.add(element);
        }

        return set;
    }

    @NotNull
    Set<Class<? super A>> types();

    @Nullable
    A parse(final @NotNull CommandContext context, final @Nullable String text) throws ArgumentParseException;
}
