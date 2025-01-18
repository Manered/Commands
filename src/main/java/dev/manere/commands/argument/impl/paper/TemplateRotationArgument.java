package dev.manere.commands.argument.impl.paper;

import com.mojang.brigadier.arguments.ArgumentType;
import dev.manere.commands.argument.Argument;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import org.bukkit.block.structure.StructureRotation;
import org.jetbrains.annotations.NotNull;

public class TemplateRotationArgument implements Argument<StructureRotation, StructureRotation> {
    @Override
    public @NotNull ArgumentType<StructureRotation> getNativeType() {
        return ArgumentTypes.templateRotation();
    }
}
