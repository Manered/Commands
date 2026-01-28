package dev.manere.commands.argument.impl.vanilla;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.manere.commands.argument.Argument;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class PlayersArgument implements Argument<List<Player>, PlayerSelectorArgumentResolver> {
    @NotNull
    @Override
    public List<Player> convert(@NotNull CommandSourceStack stack, @NotNull PlayerSelectorArgumentResolver nativeValue) throws CommandSyntaxException {
        return nativeValue.resolve(stack);
    }

    @NotNull
    @Override
    public ArgumentType<PlayerSelectorArgumentResolver> getNativeType() {
        return ArgumentTypes.players();
    }
}
