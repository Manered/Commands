package dev.manere.commands.argument.impl.paper;

import com.mojang.brigadier.arguments.ArgumentType;
import dev.manere.commands.argument.Argument;
import dev.manere.commands.completion.Completion;
import dev.manere.commands.completion.CompletionProvider;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("UnstableApiUsage")
public class UUIDArgument implements Argument<UUID, UUID> {
    @Override
    public @NotNull ArgumentType<UUID> getNativeType() {
        return ArgumentTypes.uuid();
    }
}
