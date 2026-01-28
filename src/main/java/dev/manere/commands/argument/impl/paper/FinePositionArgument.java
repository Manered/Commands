package dev.manere.commands.argument.impl.paper;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.manere.commands.argument.Argument;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.FinePositionResolver;
import io.papermc.paper.math.FinePosition;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class FinePositionArgument implements Argument<FinePosition, FinePositionResolver> {
    @Override
    public @NotNull FinePosition convert(@NotNull CommandSourceStack stack, @NotNull FinePositionResolver nativeValue) throws CommandSyntaxException {
        return nativeValue.resolve(stack);
    }

    @Override
    public @NotNull ArgumentType<FinePositionResolver> getNativeType() {
        return ArgumentTypes.finePosition();
    }
}
