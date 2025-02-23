package dev.manere.commands.argument.impl.paper;

import com.mojang.brigadier.arguments.ArgumentType;
import dev.manere.commands.argument.Argument;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import org.bukkit.block.BlockState;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class BlockStateArgument implements Argument<BlockState, BlockState> {
    @Override
    public @NotNull ArgumentType<BlockState> getNativeType() {
        return ArgumentTypes.blockState();
    }
}
