package dev.manere.commands.argument.impl.paper;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.manere.commands.argument.Argument;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import org.bukkit.block.structure.Mirror;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class TemplateMirrorArgument implements Argument<Mirror, Mirror> {
    @Override
    public @NotNull Mirror convert(@NotNull CommandSourceStack stack, @NotNull Mirror nativeValue) throws CommandSyntaxException {
        return nativeValue;
    }

    @Override
    public @NotNull ArgumentType<Mirror> getNativeType() {
        return ArgumentTypes.templateMirror();
    }
}
