package dev.manere.commands.argument.impl.paper;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.manere.commands.argument.Argument;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("UnstableApiUsage")
public class PlayerArgument implements Argument<Player, String> {
    @Nullable
    @Override
    public Player convert(final @NotNull CommandSourceStack stack, final @NotNull String nativeValue) throws CommandSyntaxException {
        final Player player = Bukkit.getPlayer(nativeValue);
        if (player == null) throw CommandSyntaxException.BUILT_IN_EXCEPTIONS
            .dispatcherParseException()
            .create("Player not found");

        return player;
    }

    @NotNull
    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }
}
