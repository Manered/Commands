package dev.manere.commands.argument.impl.paper;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.manere.commands.argument.Argument;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.PlayerProfileListResolver;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class PlayerProfileArgument implements Argument<List<PlayerProfile>, PlayerProfileListResolver> {
    @Override
    public @NotNull List<PlayerProfile> convert(@NotNull CommandSourceStack stack, @NotNull PlayerProfileListResolver nativeValue) throws CommandSyntaxException {
        return List.copyOf(nativeValue.resolve(stack));
    }

    @Override
    public @NotNull ArgumentType<PlayerProfileListResolver> getNativeType() {
        return ArgumentTypes.playerProfiles();
    }
}
