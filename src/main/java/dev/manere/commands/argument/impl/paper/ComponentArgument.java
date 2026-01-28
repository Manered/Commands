package dev.manere.commands.argument.impl.paper;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.manere.commands.argument.Argument;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class ComponentArgument implements Argument<Component, Component> {
    @Override
    public @NotNull Component convert(@NotNull CommandSourceStack stack, @NotNull Component nativeValue) throws CommandSyntaxException {
        return nativeValue;
    }

    @Override
    public @NotNull ArgumentType<Component> getNativeType() {
        return ArgumentTypes.component();
    }
}
