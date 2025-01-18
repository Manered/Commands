package dev.manere.commands.argument.impl.paper;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.manere.commands.argument.Argument;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.BlockPositionResolver;
import io.papermc.paper.math.BlockPosition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockPositionArgument implements Argument<BlockPosition, BlockPositionResolver> {
    @Override
    public @Nullable BlockPosition convert(@NotNull CommandSourceStack stack, @NotNull BlockPositionResolver nativeValue) throws CommandSyntaxException {
        return nativeValue.resolve(stack);
    }

    @Override
    public @NotNull ArgumentType<BlockPositionResolver> getNativeType() {
        return ArgumentTypes.blockPosition();
    }
}
