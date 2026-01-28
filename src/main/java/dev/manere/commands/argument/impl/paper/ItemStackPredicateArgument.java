package dev.manere.commands.argument.impl.paper;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.manere.commands.argument.Argument;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.predicate.ItemStackPredicate;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class ItemStackPredicateArgument implements Argument<ItemStackPredicate, ItemStackPredicate> {
    @Override
    public @NotNull ItemStackPredicate convert(@NotNull CommandSourceStack stack, @NotNull ItemStackPredicate nativeValue) throws CommandSyntaxException {
        return nativeValue;
    }

    @Override
    public @NotNull ArgumentType<ItemStackPredicate> getNativeType() {
        return ArgumentTypes.itemPredicate();
    }
}
