package dev.manere.commands.argument;

import dev.manere.commands.completion.CompletionProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;

public class SingleCommandArgument<A extends Argument<?, ?>> implements CommandArgument {
    @NotNull
    private final Supplier<A> argument;

    @NotNull
    private final String key;

    @Nullable
    private final CompletionProvider<?> completions;

    private final boolean required;

    public SingleCommandArgument(final @NotNull Supplier<A> argument, final @NotNull String key, final @Nullable CompletionProvider<?> completions, final boolean required) {
        this.argument = argument;
        this.key = key;
        this.completions = completions;
        this.required = required;
    }

    @NotNull
    public Supplier<A> getArgument() {
        return argument;
    }

    @NotNull
    public String getKey() {
        return key;
    }

    @Override
    @NotNull
    public Optional<CompletionProvider<?>> getCompletions() {
        return Optional.ofNullable(completions);
    }

    public boolean isRequired() {
        return required;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SingleCommandArgument<?> other && other.getKey().equals(this.getKey());
    }

    @Override
    public String toString() {
        return "SingleCommandArgument[key = " + getKey() + ", required = " + required + "]";
    }
}
