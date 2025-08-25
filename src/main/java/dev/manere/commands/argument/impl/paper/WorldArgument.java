package dev.manere.commands.argument.impl.paper;

import com.mojang.brigadier.arguments.ArgumentType;
import dev.manere.commands.argument.Argument;
import dev.manere.commands.completion.Completion;
import dev.manere.commands.completion.CompletionProvider;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class WorldArgument implements Argument<World, World> {
    @Override
    public @NotNull ArgumentType<World> getNativeType() {
        return ArgumentTypes.world();
    }

    @NotNull
    @Override
    public CompletionProvider<?> getDefaultCompletions() {
        return CompletionProvider.sync(context -> {
            final List<Completion> completions = new ArrayList<>();

            for (final World world : Bukkit.getWorlds()) {
                completions.add(Completion.completion(world.getName()));
            }

            return completions;
        });
    }
}
