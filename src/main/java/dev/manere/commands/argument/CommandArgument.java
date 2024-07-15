package dev.manere.commands.argument;

import dev.manere.commands.handler.AsyncSuggestionHandler;
import dev.manere.commands.handler.SuggestionHandler;
import dev.manere.commands.handler.SyncSuggestionHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a command argument with optional suggestion handling.
 *
 * @param <A> the type of the argument.
 */
public final class CommandArgument<A extends Argument<?>> {
    private final @NotNull Class<A> argument;
    private final @NotNull String name;
    private final @Nullable SuggestionHandler<?> suggestions;

    private CommandArgument(final @NotNull Class<A> argument, final @NotNull String name, final @Nullable SuggestionHandler<?> suggestions) {
        this.argument = argument;
        this.name = name;
        this.suggestions = suggestions;
    }

    /**
     * Creates a new CommandArgument with the specified argument class and name.
     *
     * @param argument the argument class.
     * @param name     the name of the argument.
     * @param <A>      the type of the argument.
     * @return a new CommandArgument instance.
     */
    @NotNull
    public static <A extends Argument<?>> CommandArgument<A> argument(final @NotNull Class<A> argument, final @NotNull String name) {
        return argument(argument, name, (SyncSuggestionHandler) null);
    }

    /**
     * Creates a new CommandArgument with the specified argument class, name, and synchronous suggestion handler.
     *
     * @param argument    the argument class.
     * @param name        the name of the argument.
     * @param suggestions the synchronous suggestion handler.
     * @param <A>         the type of the argument.
     * @return a new CommandArgument instance.
     */
    @NotNull
    public static <A extends Argument<?>> CommandArgument<A> argument(final @NotNull Class<A> argument, final @NotNull String name, final @Nullable SyncSuggestionHandler suggestions) {
        return new CommandArgument<>(argument, name, suggestions);
    }

    /**
     * Creates a new CommandArgument with the specified argument class, name, and asynchronous suggestion handler.
     *
     * @param argument    the argument class.
     * @param name        the name of the argument.
     * @param suggestions the asynchronous suggestion handler.
     * @param <A>         the type of the argument.
     * @return a new CommandArgument instance.
     */
    @NotNull
    public static <A extends Argument<?>> CommandArgument<A> argument(final @NotNull Class<A> argument, final @NotNull String name, final @Nullable AsyncSuggestionHandler suggestions) {
        return new CommandArgument<>(argument, name, suggestions);
    }

    /**
     * Creates a new Builder for a CommandArgument with the specified argument class.
     *
     * @param argument the argument class.
     * @param <A>      the type of the argument.
     * @return a new Builder instance.
     */
    @NotNull
    public static <A extends Argument<?>> Builder<A> builder(final @NotNull Class<A> argument) {
        return new Builder<>(argument);
    }

    /**
     * Creates a new Builder for a CommandArgument with the specified argument class and name.
     *
     * @param argument the argument class.
     * @param name     the name of the argument.
     * @param <A>      the type of the argument.
     * @return a new Builder instance.
     */
    @NotNull
    public static <A extends Argument<?>> Builder<A> builder(final @NotNull Class<A> argument, final @NotNull String name) {
        return builder(argument).name(name);
    }

    /**
     * Builder for creating CommandArgument instances.
     *
     * @param <A> the type of the argument.
     */
    public static final class Builder<A extends Argument<?>> {
        private final Class<A> argument;
        private String name;
        private SuggestionHandler<?> suggestions;

        private Builder(final @NotNull Class<A> argument) {
            this.argument = argument;
        }

        /**
         * Sets the name of the argument.
         *
         * @param name the name of the argument.
         * @return the current Builder instance.
         */
        @NotNull
        public Builder<A> name(final @NotNull String name) {
            this.name = name;
            return this;
        }

        /**
         * Sets the synchronous suggestion handler for the argument.
         *
         * @param suggestions the synchronous suggestion handler.
         * @return the current Builder instance.
         */
        @NotNull
        public Builder<A> suggest(final @NotNull SyncSuggestionHandler suggestions) {
            this.suggestions = suggestions;
            return this;
        }

        /**
         * Sets the asynchronous suggestion handler for the argument.
         *
         * @param suggestions the asynchronous suggestion handler.
         * @return the current Builder instance.
         */
        @NotNull
        public Builder<A> suggest(final @NotNull AsyncSuggestionHandler suggestions) {
            this.suggestions = suggestions;
            return this;
        }

        /**
         * Builds and returns the CommandArgument instance.
         *
         * @return the CommandArgument instance.
         */
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

    /**
     * Gets the argument class.
     *
     * @return the argument class.
     */
    @NotNull
    public Class<A> argument() {
        return argument;
    }

    /**
     * Gets the name of the argument.
     *
     * @return the name of the argument.
     */
    @NotNull
    public String name() {
        return name;
    }

    /**
     * Gets the suggestion handler for the argument.
     *
     * @return the suggestion handler, or null if none is set.
     */
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
        return "CommandArgument[name=" + name + ",type=" + argument.getSimpleName() + "]";
    }
}