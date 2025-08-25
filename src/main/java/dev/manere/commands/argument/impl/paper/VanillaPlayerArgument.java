package dev.manere.commands.argument.impl.paper;

import com.mojang.brigadier.arguments.ArgumentType;
import dev.manere.commands.argument.Argument;
import dev.manere.commands.completion.Completion;
import dev.manere.commands.completion.CompletionProvider;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class VanillaPlayerArgument implements Argument<Player, PlayerSelectorArgumentResolver> {
    @Override
    public @NotNull ArgumentType<PlayerSelectorArgumentResolver> getNativeType() {
        return ArgumentTypes.player();
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
