package dev.manere.commands.ctx;

import dev.manere.commands.CommandNode;
import dev.manere.commands.argument.Argument;
import dev.manere.commands.argument.CommandArgument;
import dev.manere.commands.argument.ListArgument;
import dev.manere.commands.exception.ArgumentParseException;
import dev.manere.commands.exception.IgnorableCommandException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * Represents the arguments of a command in a specific context.
 *
 * @see CommandContext
 * @see CommandArgument
 * @see Argument
 */
public class CommandArguments {
    private final CommandContext context;

    /**
     * Constructs a CommandArguments instance with the given context.
     *
     * @param context the command context.
     */
    CommandArguments(final @NotNull CommandContext context) {
        this.context = context;
    }

    /**
     * Retrieves the CommandArgument at the specified position.
     *
     * @param position the position of the argument.
     * @return the CommandArgument at the specified position, or null if not found.
     */
    @Nullable
    private CommandArgument<?> named(final int position) {
        CommandArgument<?> arg;
        if (position >= 0 && position < context.command().arguments().size()) {
            arg = context.command().arguments().get(position);
        } else {
            arg = null;
        }

        return arg;
    }

    /**
     * Retrieves the current CommandArgument based on the current position.
     *
     * @return the current CommandArgument, or null if not found.
     */
    @Nullable
    public CommandArgument<?> current() {
        int currentPosition = context.argumentsFromOffset().size();
        return named(currentPosition);
    }

    /**
     * Retrieves the position of the specified CommandArgument.
     *
     * @param argument the CommandArgument to find.
     * @return the position of the argument, or null if not found.
     */
    @Nullable
    public Integer position(final @NotNull CommandArgument<?> argument) {
        final CommandNode node = context.command();

        for (int position = 0; position < node.arguments().size(); position++) {
            final CommandArgument<?> possible = node.arguments().get(position);
            if (argument.name().equals(possible.name())) return position;
        }

        return null;
    }

    /**
     * Retrieves the CommandArgument with the specified name.
     *
     * @param name the name of the argument.
     * @return the CommandArgument with the specified name, or null if not found.
     */
    @Nullable
    public CommandArgument<?> named(final @NotNull String name) {
        for (final CommandArgument<?> argument : context.command().arguments()) if (argument.name().equals(name)) return argument;
        return null;
    }

    /**
     * Retrieves the argument of the specified type and name.
     *
     * @param type the class of the argument type.
     * @param name the name of the argument.
     * @param <T>  the type of the argument.
     * @return the argument of the specified type and name, or null if not found.
     */
    @Nullable
    public <T> T argument(final @NotNull Class<T> type, final @NotNull String name) {
        final CommandArgument<?> argument = named(name);
        if (argument == null) return null;

        final Integer argumentPosition = position(argument);
        if (argumentPosition == null || argumentPosition >= context.argumentsFromOffset().size()) return null;

        if (argument.argument().get() instanceof ListArgument<?> listArgument) {
            try {
                return type.cast(listArgument.parse(context, argumentPosition));
            } catch (ArgumentParseException e) {
                throw ArgumentParseException.exception(e.context(), e.text());
            }
        }

        try {
            return type.cast(argument.argument().get().parse(context, context.argumentsFromOffset().get(argumentPosition)));
        } catch (ArgumentParseException e) {
            throw ArgumentParseException.exception(e.context(), e.text());
        }
    }

    /**
     * Retrieves the argument with the specified name.
     *
     * @param name the name of the argument.
     * @return the argument with the specified name, or null if not found.
     */
    @Nullable
    public Object argument(final @NotNull String name) {
        return argument(Object.class, name);
    }

    /**
     * Retrieves the argument of the specified type and name, or returns a default value if not found.
     *
     * @param type the class of the argument type.
     * @param name the name of the argument.
     * @param or   the supplier of the default value.
     * @param <T>  the type of the argument.
     * @return the argument of the specified type and name, or the default value if not found.
     */
    @NotNull
    public <T> T argumentOr(final @NotNull Class<T> type, final @NotNull String name, final @NotNull Supplier<@Nullable T> or) {
        final T argument = argument(type, name);

        if (argument == null) {
            final T orGet = or.get();

            if (orGet == null) {
                throw new IgnorableCommandException();
            } else {
                return orGet;
            }
        }

        return argument;
    }

    /**
     * Retrieves the argument with the specified name, or returns a default value if not found.
     *
     * @param name the name of the argument.
     * @param or   the supplier of the default value.
     * @param <T>  the type of the argument.
     * @return the argument with the specified name, or the default value if not found.
     */
    @NotNull
    @SuppressWarnings("unchecked")
    public <T> T argumentOr(final @NotNull String name, final @NotNull Supplier<@Nullable T> or) {
        final Object argument = argument(name);

        if (argument == null) {
            final T orGet = or.get();

            if (orGet == null) {
                throw new IgnorableCommandException();
            } else {
                return orGet;
            }
        }

        return (T) argument;
    }

    @NotNull
    public <T> T argumentOr(final @NotNull Class<T> type, final @NotNull String name, final @NotNull Runnable or) {
        final T argument = argument(type, name);

        if (argument == null) {
            or.run();
            throw new IgnorableCommandException();
        }

        return argument;
    }

    @NotNull
    @SuppressWarnings("unchecked")
    public <T> T argumentOr(final @NotNull String name, final @NotNull Runnable or) {
        final Object argument = argument(name);

        if (argument == null) {
            or.run();
            throw new IgnorableCommandException();
        }

        return (T) argument;
    }
}