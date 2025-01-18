package dev.manere.commands.argument.impl.paper;

import com.mojang.brigadier.arguments.ArgumentType;
import dev.manere.commands.argument.Argument;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;

public class NamedColorArgument implements Argument<NamedTextColor, NamedTextColor> {
    @Override
    public @NotNull ArgumentType<NamedTextColor> getNativeType() {
        return ArgumentTypes.namedColor();
    }
}
