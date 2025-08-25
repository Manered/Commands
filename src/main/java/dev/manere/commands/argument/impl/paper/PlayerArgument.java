package dev.manere.commands.argument.impl.paper;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.manere.commands.argument.Argument;
import dev.manere.commands.completion.Completion;
import dev.manere.commands.completion.CompletionProvider;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class PlayerArgument implements Argument<Player, String> {
    @Nullable
    @Override
    public Player convert(final @NotNull CommandSourceStack stack, final @NotNull String nativeValue) throws CommandSyntaxException {
        if (nativeValue == null || nativeValue.isBlank()) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS
                .dispatcherParseException()
                .create("Player not found");
        }

        final Player player = Bukkit.getPlayer(nativeValue);
        if (player == null) throw CommandSyntaxException.BUILT_IN_EXCEPTIONS
            .dispatcherParseException()
            .create("Player '" + nativeValue + "' not found");

        return player;
    }

    @NotNull
    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

    @NotNull
    @Override
    public CompletionProvider<?> getDefaultCompletions() {
        return CompletionProvider.async(context -> {
            final List<Completion> completions = new ArrayList<>();

            for (final Player player : Bukkit.getOnlinePlayers()) {
                completions.add(Completion.completion(player));
            }

            return completions;
        });
    }
}
