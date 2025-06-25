package dev.manere.commands.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public final class CommandAPIOption<V> {
    @NotNull
    private final String key;

    @Nullable
    private final V defaultValue;

    public CommandAPIOption(final @NotNull String key, final @Nullable V defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    @NotNull
    public Optional<V> getDefaultValue() {
        return Optional.ofNullable(defaultValue);
    }

    @NotNull
    public String getKey() {
        return key;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof CommandAPIOption<?> other && other.getKey().equals(this.getKey());
    }
}
