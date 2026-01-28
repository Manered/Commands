package dev.manere.commands.completion;

import dev.manere.commands.CommandContext;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public final class EmptySuggestions implements Suggestions<Collection<Completion>> {
    @NotNull
    @Override
    @Unmodifiable
    public Collection<Completion> suggests(final @NotNull CommandContext<? extends CommandSender> context) {
        return Collections.emptyList();
    }

    @Override
    public int hashCode() {
        return Objects.hash(toString());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof EmptySuggestions other && other.toString().equals(toString());
    }

    @Override
    public String toString() {
        return "EmptyCompletionProvider";
    }
}
