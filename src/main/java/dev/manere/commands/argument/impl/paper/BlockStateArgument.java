package dev.manere.commands.argument.impl.paper;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.manere.commands.argument.Argument;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import org.bukkit.block.BlockState;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class BlockStateArgument implements Argument<BlockState, BlockState> {
    @Override
    public @NotNull BlockState convert(@NotNull CommandSourceStack stack, @NotNull BlockState nativeValue) throws CommandSyntaxException {
        return nativeValue;
    }

    @Override
    public @NotNull ArgumentType<BlockState> getNativeType() {
        return ArgumentTypes.blockState();
    }
}
