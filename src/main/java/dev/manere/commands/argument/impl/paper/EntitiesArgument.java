package dev.manere.commands.argument.impl.paper;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.manere.commands.argument.Argument;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.EntitySelectorArgumentResolver;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class EntitiesArgument implements Argument<List<Entity>, EntitySelectorArgumentResolver> {
    @Override
    public @NotNull List<Entity> convert(@NotNull CommandSourceStack stack, @NotNull EntitySelectorArgumentResolver nativeValue) throws CommandSyntaxException {
        return nativeValue.resolve(stack);
    }

    @Override
    public @NotNull ArgumentType<EntitySelectorArgumentResolver> getNativeType() {
        return ArgumentTypes.entities();
    }
}
