package dev.manere.commands.handler;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Represents a suggestion for a command argument.
 *
 * @see SuggestionHandler
 * @see SyncSuggestionHandler
 * @see AsyncSuggestionHandler
 */
public class Suggestion {
    @NotNull
    private final String suggestion;

    @Nullable
    private final Component tooltip;

    private final boolean sticky;

    /**
     * Constructs a Suggestion with the specified suggestion text, tooltip, and sticky flag.
     *
     * @param suggestion the suggestion text.
     * @param tooltip    the tooltip text.
     * @param sticky     whether the suggestion is sticky.
     */
    public Suggestion(final @NotNull String suggestion, final @Nullable Component tooltip, final boolean sticky) {
        this.suggestion = suggestion;
        this.tooltip = tooltip;
        this.sticky = sticky;
    }

    /**
     * Constructs a Suggestion with the specified suggestion text and sticky flag.
     *
     * @param suggestion the suggestion text.
     * @param sticky     whether the suggestion is sticky.
     */
    public Suggestion(final @NotNull String suggestion, final boolean sticky) {
        this(suggestion, null, sticky);
    }

    /**
     * Constructs a Suggestion with the specified suggestion text and tooltip.
     *
     * @param suggestion the suggestion text.
     * @param tooltip    the tooltip text.
     */
    public Suggestion(final @NotNull String suggestion, final @Nullable Component tooltip) {
        this(suggestion, tooltip, false);
    }

    /**
     * Constructs a Suggestion with the specified suggestion text.
     *
     * @param suggestion the suggestion text.
     */
    public Suggestion(final @NotNull String suggestion) {
        this(suggestion, false);
    }

    /**
     * Creates a new Suggestion with the specified suggestion text.
     *
     * @param suggestion the suggestion text.
     * @return a new Suggestion instance.
     */
    @NotNull
    public static Suggestion suggestion(final @NotNull String suggestion) {
        return suggestion(suggestion, null, false);
    }

    /**
     * Creates a new Suggestion with the specified suggestion text and sticky flag.
     *
     * @param suggestion the suggestion text.
     * @param sticky     whether the suggestion is sticky.
     * @return a new Suggestion instance.
     */
    @NotNull
    public static Suggestion suggestion(final @NotNull String suggestion, final boolean sticky) {
        return suggestion(suggestion, null, sticky);
    }

    /**
     * Creates a new Suggestion with the specified suggestion text, tooltip, and sticky flag.
     *
     * @param suggestion the suggestion text.
     * @param tooltip    the tooltip text.
     * @param sticky     whether the suggestion is sticky.
     * @return a new Suggestion instance.
     */
    @NotNull
    public static Suggestion suggestion(final @NotNull String suggestion, final @Nullable Component tooltip, final boolean sticky) {
        return new Suggestion(suggestion, tooltip, sticky);
    }

    /**
     * Creates a new Suggestion with the specified suggestion text and tooltip.
     *
     * @param suggestion the suggestion text.
     * @param tooltip    the tooltip text.
     * @return a new Suggestion instance.
     */
    @NotNull
    public static Suggestion suggestion(final @NotNull String suggestion, final @Nullable Component tooltip) {
        return new Suggestion(suggestion, tooltip);
    }

    /**
     * Gets the suggestion text.
     *
     * @return the suggestion text.
     */
    @NotNull
    public String suggestion() {
        return suggestion;
    }

    /**
     * Gets the tooltip text.
     *
     * @return the tooltip text.
     */
    @Nullable
    public Component tooltip() {
        return tooltip;
    }

    /**
     * Checks if the suggestion is sticky.
     *
     * @return true if the suggestion is sticky, false otherwise.
     */
    public boolean sticky() {
        return sticky;
    }

    @NotNull
    @Override
    public String toString() {
        return "Suggestion[suggestion=" + suggestion + ", plainTooltip=" + PlainTextComponentSerializer.plainText().serialize(Objects.requireNonNullElseGet(tooltip(), Component::empty)) + ", tooltip=" + Objects.requireNonNullElseGet(tooltip, Component::empty) + "]";
    }
}