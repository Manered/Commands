package dev.manere.commands.argument.impl.paper;

import com.mojang.brigadier.arguments.ArgumentType;
import dev.manere.commands.argument.Argument;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import net.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class StyleArgument implements Argument<Style, Style> {
    @Override
    public @NotNull ArgumentType<Style> getNativeType() {
        return ArgumentTypes.style();
    }
}
