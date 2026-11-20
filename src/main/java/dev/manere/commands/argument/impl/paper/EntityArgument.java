package dev.manere.commands.argument.impl.paper;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import dev.manere.commands.argument.Argument;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.EntitySelectorArgumentResolver;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class EntityArgument implements Argument<Entity, EntitySelectorArgumentResolver> {
    @Override
    public @NotNull Entity convert(@NotNull CommandSourceStack stack, @NotNull EntitySelectorArgumentResolver nativeValue) throws CommandSyntaxException {
        final var entities = nativeValue.resolve(stack);
        if (entities.isEmpty()) {
            throw new SimpleCommandExceptionType(() -> "No entity was found.").create();
        }
        return entities.get(0);
    }

    @Override
    public @NotNull ArgumentType<EntitySelectorArgumentResolver> getNativeType() {
        return ArgumentTypes.entity();
    }
}
