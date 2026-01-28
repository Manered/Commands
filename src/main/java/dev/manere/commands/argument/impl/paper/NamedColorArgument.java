package dev.manere.commands.argument.impl.paper;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.manere.commands.argument.Argument;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class NamedColorArgument implements Argument<NamedTextColor, NamedTextColor> {
    @Override
    public @NotNull NamedTextColor convert(@NotNull CommandSourceStack stack, @NotNull NamedTextColor nativeValue) throws CommandSyntaxException {
        return nativeValue;
    }

    @Override
    public @NotNull ArgumentType<NamedTextColor> getNativeType() {
        return ArgumentTypes.namedColor();
    }
}
