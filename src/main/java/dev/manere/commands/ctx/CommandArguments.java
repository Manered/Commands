package dev.manere.commands.ctx;

import dev.manere.commands.CommandNode;
import dev.manere.commands.argument.Argument;
import dev.manere.commands.argument.CommandArgument;
import dev.manere.commands.exception.ArgumentParseException;
import dev.manere.commands.exception.IgnorableCommandException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

public class CommandArguments {
    private final CommandContext context;

    CommandArguments(final @NotNull CommandContext context) {
        this.context = context;
    }

    @Nullable
    private CommandArgument<?> argument(final int position) {
        return position >= 0 && position < context.command().arguments().size() ? context.command().arguments().get(position) : null;
    }

    private int size() {
        return context.argumentsFromOffset().size();
    }

    public int position() {
        return Math.max(size() - 1, 0);
    }

    @Nullable
    public CommandArgument<?> current() {
        return argument(position());
    }

    @Nullable
    public Integer position(final @NotNull CommandArgument<?> argument) {
        final CommandNode node = context.command();

        for (int position = 0; position < node.arguments().size(); position++) {
            final CommandArgument<?> possible = node.arguments().get(position);
            if (argument.name().equals(possible.name())) return position;
        }

        return null;
    }

    @Nullable
    public CommandArgument<?> argument(final @NotNull String name) {
        for (final CommandArgument<?> argument : context.command().arguments()) {
            if (argument.name().equals(name)) return argument;
        }
        return null;
    }

    @Nullable
    public <T> T argument(final @NotNull Class<T> type, final @NotNull String name) {
        final CommandArgument<?> argument = argument(name);
        if (argument == null) return null;

        final int commandPosition = position();
        if (commandPosition >= context.argumentsFromOffset().size()) return null;

        try {
            return type.cast(argument.argument().getDeclaredConstructor().newInstance().parse(context, context.argumentsFromOffset().get(commandPosition)));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (ArgumentParseException e) {
            throw ArgumentParseException.exception(e.context(), e.text());
        }
    }

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
}
