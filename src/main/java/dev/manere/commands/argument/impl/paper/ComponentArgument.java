package dev.manere.commands.argument.impl.paper;

import com.mojang.brigadier.arguments.ArgumentType;
import dev.manere.commands.argument.Argument;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class ComponentArgument implements Argument<Component, Component> {
    @Override
    public @NotNull ArgumentType<Component> getNativeType() {
        return ArgumentTypes.component();
    }
}
