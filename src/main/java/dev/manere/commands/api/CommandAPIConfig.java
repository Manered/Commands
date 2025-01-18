package dev.manere.commands.api;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class CommandAPIConfig {
    private final Map<CommandAPIOption<?>, Object> options = new ConcurrentHashMap<>();

    @NotNull
    @CanIgnoreReturnValue
    public <V> CommandAPIConfig set(final @NotNull CommandAPIOption<V> option, final @Nullable V value) {
        this.options.put(option, value);
        return this;
    }

    @NotNull
    public <V> Optional<V> get(final @NotNull CommandAPIOption<V> option) {
        final Optional<V> defaultValue = option.getDefaultValue();
        final Optional<V> customValue = Optional.ofNullable(option.getType().cast(options.get(option)));

        return customValue.isPresent() ? customValue : defaultValue;
    }

    @NotNull
    @Unmodifiable
    public Map<CommandAPIOption<?>, Object> getOptions() {
        return Map.copyOf(options);
    }
}
