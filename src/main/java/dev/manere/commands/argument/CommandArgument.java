package dev.manere.commands.argument;

import dev.manere.commands.ctx.CommandArguments;
import dev.manere.commands.handler.AsyncSuggestionHandler;
import dev.manere.commands.handler.SuggestionHandler;
import dev.manere.commands.handler.SyncSuggestionHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class CommandArgument<A extends Argument<?>> {
    private final @NotNull Class<A> argument;
    private final @NotNull String name;
    private final @Nullable SuggestionHandler<?> suggestions;

    private CommandArgument(final @NotNull Class<A> argument, final @NotNull String name, final @Nullable SuggestionHandler<?> suggestions) {
        this.argument = argument;
        this.name = name;
        this.suggestions = suggestions;
    }

    @NotNull
    public static <A extends Argument<?>> CommandArgument<A> argument(final @NotNull Class<A> argument, final @NotNull String name) {
        return argument(argument, name, null);
    }

    @NotNull
    public static <A extends Argument<?>> CommandArgument<A> argument(final @NotNull Class<A> argument, final @NotNull String name, final @Nullable SuggestionHandler<?> suggestions) {
        return new CommandArgument<>(argument, name, suggestions);
    }

    @NotNull
    public static <A extends Argument<?>> Builder<A> builder(final @NotNull Class<A> argument) {
        return new Builder<>(argument);
    }

    @NotNull
    public static <A extends Argument<?>> Builder<A> builder(final @NotNull Class<A> argument, final @NotNull String name) {
        return builder(argument).name(name);
    }

    public static final class Builder<A extends Argument<?>> {
        private final Class<A> argument;
        private String name;
        private SuggestionHandler<?> suggestions;

        private Builder(final @NotNull Class<A> argument) {
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
            return CommandArgument.argument(argument, name, suggestions);
        }
    }

    @NotNull
    public Class<A> argument() {
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
}
