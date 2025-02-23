package dev.manere.commands.argument.impl.paper;

import com.mojang.brigadier.arguments.ArgumentType;
import dev.manere.commands.argument.Argument;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import org.bukkit.block.structure.Mirror;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class TemplateMirrorArgument implements Argument<Mirror, Mirror> {
    @Override
    public @NotNull ArgumentType<Mirror> getNativeType() {
        return ArgumentTypes.templateMirror();
    }
}
