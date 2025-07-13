package dev.manere.commands.argument.impl.paper;

import com.mojang.brigadier.arguments.ArgumentType;
import dev.manere.commands.argument.Argument;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class VanillaPlayerArgument implements Argument<Player, PlayerSelectorArgumentResolver> {
    @Override
    public @NotNull ArgumentType<PlayerSelectorArgumentResolver> getNativeType() {
        return ArgumentTypes.player();
    }
}
