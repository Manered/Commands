package dev.manere.commands.argument.impl.paper;

import com.mojang.brigadier.arguments.ArgumentType;
import dev.manere.commands.argument.Argument;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class ItemStackArgument implements Argument<ItemStack, ItemStack> {
    @Override
    public @NotNull ArgumentType<ItemStack> getNativeType() {
        return ArgumentTypes.itemStack();
    }
}
