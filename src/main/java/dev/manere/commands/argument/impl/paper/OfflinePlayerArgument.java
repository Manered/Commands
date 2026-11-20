package dev.manere.commands.argument.impl.paper;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.manere.commands.argument.Argument;
import dev.manere.commands.completion.Completion;
import dev.manere.commands.completion.Suggestions;
import dev.manere.commands.completion.SyncSuggestions;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class OfflinePlayerArgument implements Argument<OfflinePlayer, String> {
    @Override
    public @NotNull OfflinePlayer convert(final @NotNull CommandSourceStack stack, final @NotNull String nativeValue) throws CommandSyntaxException {
        final Player online = Bukkit.getPlayerExact(nativeValue);
        if (online != null) return online;

        final OfflinePlayer cached = Bukkit.getOfflinePlayerIfCached(nativeValue);
        if (cached != null) return cached;

        try {
            final UUID uuid = UUID.fromString(nativeValue);
            return Bukkit.getOfflinePlayer(uuid);
        } catch (final IllegalArgumentException ignored) {}

        return Bukkit.getOfflinePlayer(nativeValue);
    }

    @NotNull
    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

    @NotNull
    @Override
    public Suggestions<?> getDefaultCompletions() {
        return (SyncSuggestions) context ->
            Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .map(Completion::completion)
                .toList();
    }
}
