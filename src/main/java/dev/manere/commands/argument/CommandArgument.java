package dev.manere.commands.argument;

import dev.manere.commands.handler.AsyncSuggestionHandler;
import dev.manere.commands.handler.SuggestionHandler;
import dev.manere.commands.handler.SyncSuggestionHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * Represents a command argument with optional suggestion handling.
 *
 * @param <A> the type of the argument.
 */
public final class CommandArgument<A extends Argument<?>> {
    private final @NotNull Supplier<A> argument;
    private final @NotNull String name;
    private final @Nullable SuggestionHandler<?> suggestions;

    private CommandArgument(final @NotNull Supplier<A> argument, final @NotNull String name, final @Nullable SuggestionHandler<?> suggestions) {
        this.argument = argument;
        this.name = name;
        this.suggestions = suggestions;
    }

    @NotNull
    public static <A extends Argument<?>> CommandArgument<A> argument(final @NotNull Supplier<A> argument, final @NotNull String name) {
        return argument(argument, name, (SyncSuggestionHandler) null);
    }

    @NotNull
    public static <A extends Argument<?>> CommandArgument<A> argument(final @NotNull Supplier<A> argument, final @NotNull String name, final @Nullable SyncSuggestionHandler suggestions) {
        return new CommandArgument<>(argument, name, suggestions);
    }

    @NotNull
    public static <A extends Argument<?>> CommandArgument<A> argument(final @NotNull Supplier<A> argument, final @NotNull String name, final @Nullable AsyncSuggestionHandler suggestions) {
        return new CommandArgument<>(argument, name, suggestions);
    }

    @NotNull
    public static <A extends Argument<?>> Builder<A> builder(final @NotNull Supplier<A> argument) {
        return new Builder<>(argument);
    }

    @NotNull
    public static <A extends Argument<?>> Builder<A> builder(final @NotNull Supplier<A> argument, final @NotNull String name) {
        return builder(argument).name(name);
    }

    public static final class Builder<A extends Argument<?>> {
        private final Supplier<A> argument;
        private String name;
        private SuggestionHandler<?> suggestions;

        private Builder(final @NotNull Supplier<A> argument) {
            this.argument = argument;
        }

        @NotNull
        public Builder<A> name(final @NotNull String name) {
            this.name = name;
            return this;
        }

        @NotNull
        public Builder<A> suggest(final @NotNull SyncSuggestionHandler suggestions) {
            this.suggestions = suggestions;
            return this;
        }

        @NotNull
        public Builder<A> suggest(final @NotNull AsyncSuggestionHandler suggestions) {
            this.suggestions = suggestions;
            return this;
        }

        @NotNull
        public CommandArgument<A> build() {
            if (suggestions instanceof SyncSuggestionHandler sync) {
                return CommandArgument.argument(argument, name, sync);
            } else if (suggestions instanceof AsyncSuggestionHandler async) {
                return CommandArgument.argument(argument, name, async);
            }

            throw new IllegalArgumentException();
        }
    }

    @NotNull
    public Supplier<A> argument() {
        return argument;
    }

    @NotNull
    public String name() {
        return name;
    }

    @Nullable
    public SuggestionHandler<?> suggestions() {
        return suggestions;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CommandArgument<?> other && this.name.equals(other.name);
    }

    @Override
    public String toString() {
        return "CommandArgument[name=" + name + ",type=" + argument.get() + "]";
    }
}