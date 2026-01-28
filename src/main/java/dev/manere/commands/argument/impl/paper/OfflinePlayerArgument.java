package dev.manere.commands.argument.impl.paper;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.manere.commands.argument.Argument;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class OfflinePlayerArgument implements Argument<OfflinePlayer, String> {
    @Override
    public @NotNull OfflinePlayer convert(final @NotNull CommandSourceStack stack, final @NotNull String nativeValue) throws CommandSyntaxException {
        return Bukkit.getOfflinePlayer(nativeValue);
    }

    @NotNull
    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }
}