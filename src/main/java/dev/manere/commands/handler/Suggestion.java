package dev.manere.commands.handler;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Suggestion {
    @NotNull
    private final String suggestion;

    @Nullable
    private final Component tooltip;

    private final boolean sticky;

    public Suggestion(final @NotNull String suggestion, final @Nullable Component tooltip, final boolean sticky) {
        this.suggestion = suggestion;
        this.tooltip = tooltip;
        this.sticky = sticky;
    }

    public Suggestion(final @NotNull String suggestion, final boolean sticky) {
        this(suggestion, null, sticky);
    }

    public Suggestion(final @NotNull String suggestion, final @Nullable Component tooltip) {
        this(suggestion, tooltip, false);
    }

    public Suggestion(final @NotNull String suggestion) {
        this(suggestion, false);
    }

    @NotNull
    public static Suggestion suggestion(final @NotNull String suggestion) {
        return suggestion(suggestion, null, false);
    }

    @NotNull
    public static Suggestion suggestion(final @NotNull String suggestion, final boolean sticky) {
        return suggestion(suggestion, null, sticky);
    }

    @NotNull
    public static Suggestion suggestion(final @NotNull String suggestion, final @Nullable Component tooltip, final boolean sticky) {
        return new Suggestion(suggestion, tooltip, sticky);
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

    public boolean sticky() {
        return sticky;
    }
}
