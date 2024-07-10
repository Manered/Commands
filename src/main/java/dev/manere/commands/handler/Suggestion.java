package dev.manere.commands.handler;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Suggestion {
    @NotNull
    private final String suggestion;

    @Nullable
    private final Component tooltip;

    public Suggestion(final @NotNull String suggestion, final @Nullable Component tooltip) {
        this.suggestion = suggestion;
        this.tooltip = tooltip;
    }

    public Suggestion(final @NotNull String suggestion) {
        this(suggestion, null);
    }

    @NotNull
    public static Suggestion suggestion(final @NotNull String suggestion) {
        return suggestion(suggestion, null);
    }

    @NotNull
    public static Suggestion suggestion(final @NotNull String suggestion, final @Nullable Component tooltip) {
        return new Suggestion(suggestion, tooltip);
    }

    @NotNull
    public String suggestion() {
        return suggestion;
    }

    @Nullable
    public Component tooltip() {
        return tooltip;
    }
}
