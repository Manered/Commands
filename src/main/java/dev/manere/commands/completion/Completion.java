package dev.manere.commands.completion;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Completion {
    private final String text;
    private final Component tooltip;

    public Completion(final @NotNull String text, final @Nullable Component tooltip) {
        this.text = text;
        this.tooltip = tooltip;
    }

    public Completion(final @NotNull String text) {
        this(text, null);
    }

    @NotNull
    public static Completion completion(final @NotNull String text) {
        return completion(text, null);
    }

    @NotNull
    public static Completion completion(final @NotNull String text, final @Nullable Component tooltip) {
        return new Completion(text, tooltip);
    }

    @NotNull
    public static Completion completion(final @NotNull Player player) {
        return completion(player.getName(), null);
    }

    @NotNull
    public static Completion completion(final @NotNull Object object) {
        return completion(object, null);
    }

    @NotNull
    public static Completion completion(final @NotNull Object object, final @Nullable Component tooltip) {
        return completion(object.toString(), tooltip);
    }

    @NotNull
    public static Completion convert(final @NotNull Object object, final @Nullable Component tooltip) {
        return object instanceof Completion completion ? completion : completion(object, tooltip);
    }

    @NotNull
    public static Completion convert(final @NotNull Object object) {
        return convert(object, null);
    }

    @NotNull
    public String getText() {
        return text;
    }

    @NotNull
    public Component getTooltip() {
        return tooltip != null ? tooltip : Component.empty();
    }

    @NotNull
    public Completion withTooltip(final @NotNull Component tooltip) {
        return Completion.completion(getText(), tooltip);
    }

    @NotNull
    public Completion withText(final @NotNull String text) {
        return Completion.completion(text, getTooltip());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Completion other && other.getText().equals(this.getText());
    }

    @Override
    public String toString() {
        return tooltip != null
            ? "Completion[text = " + text + ", tooltip = " + PlainTextComponentSerializer.plainText().serialize(getTooltip()) + "]"
            : "Completion[text = " + text + ", tooltip = " + null + "]";
    }
}
