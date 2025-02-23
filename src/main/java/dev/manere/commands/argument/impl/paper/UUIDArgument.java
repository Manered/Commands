package dev.manere.commands.argument.impl.paper;

import com.mojang.brigadier.arguments.ArgumentType;
import dev.manere.commands.argument.Argument;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@SuppressWarnings("UnstableApiUsage")
public class UUIDArgument implements Argument<UUID, UUID> {
    @Override
    public @NotNull ArgumentType<UUID> getNativeType() {
        return ArgumentTypes.uuid();
    }
}
