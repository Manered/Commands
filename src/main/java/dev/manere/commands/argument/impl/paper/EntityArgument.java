package dev.manere.commands.argument.impl.paper;

import com.mojang.brigadier.arguments.ArgumentType;
import dev.manere.commands.argument.Argument;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.EntitySelectorArgumentResolver;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class EntityArgument implements Argument<Entity, EntitySelectorArgumentResolver> {
    @Override
    public @NotNull ArgumentType<EntitySelectorArgumentResolver> getNativeType() {
        return ArgumentTypes.entity();
    }
}
