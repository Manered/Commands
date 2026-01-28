package dev.manere.commands.argument.impl.paper;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.manere.commands.argument.Argument;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import org.bukkit.block.structure.StructureRotation;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class TemplateRotationArgument implements Argument<StructureRotation, StructureRotation> {
    @Override
    public @NotNull StructureRotation convert(@NotNull CommandSourceStack stack, @NotNull StructureRotation nativeValue) throws CommandSyntaxException {
        return nativeValue;
    }

    @Override
    public @NotNull ArgumentType<StructureRotation> getNativeType() {
        return ArgumentTypes.templateRotation();
    }
}
