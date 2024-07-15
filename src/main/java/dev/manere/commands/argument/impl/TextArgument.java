package dev.manere.commands.argument.impl;

import dev.manere.commands.argument.Argument;
import dev.manere.commands.argument.ArgumentTypes;
import dev.manere.commands.ctx.CommandContext;
import dev.manere.commands.exception.ArgumentParseException;
import dev.manere.commands.handler.Suggestion;
import dev.manere.commands.handler.SyncSuggestionHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class TextArgument implements Argument<Component>, SyncSuggestionHandler {
    @NotNull
    @Override
    public Set<Class<? super Component>> types() {
        return Argument.newSet(Component.class);
    }

    @Nullable
    @Override
    public Component parse(final @NotNull CommandContext context, final @Nullable String text) throws ArgumentParseException {
        final String string = new StringArgument().parse(context, text);
        return string == null ? null : MiniMessage.miniMessage().deserialize(string);
    }

    @NotNull
    @Override
    public List<Suggestion> suggestions(final @NotNull CommandContext context) {
        return List.of(Suggestion.suggestion("<text>", true));
    }
}
