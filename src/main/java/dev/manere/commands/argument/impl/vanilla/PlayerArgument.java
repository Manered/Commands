package dev.manere.commands.argument.impl.vanilla;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import dev.manere.commands.argument.Argument;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class PlayerArgument implements Argument<Player, PlayerSelectorArgumentResolver> {
    @NotNull
    @Override
    public Player convert(@NotNull CommandSourceStack stack, @NotNull PlayerSelectorArgumentResolver nativeValue) throws CommandSyntaxException {
        final var players = nativeValue.resolve(stack);
        if (players.isEmpty()) {
            throw new SimpleCommandExceptionType(() -> "No player was found.").create();
        }
        return players.getFirst();
    }

    @NotNull
    @Override
    public ArgumentType<PlayerSelectorArgumentResolver> getNativeType() {
        return ArgumentTypes.player();
    }
}
