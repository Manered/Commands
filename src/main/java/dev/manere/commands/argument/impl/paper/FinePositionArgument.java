package dev.manere.commands.argument.impl.paper;

import com.mojang.brigadier.arguments.ArgumentType;
import dev.manere.commands.argument.Argument;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.FinePositionResolver;
import io.papermc.paper.math.FinePosition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FinePositionArgument implements Argument<FinePosition, FinePositionResolver> {
    private boolean centerIntegers = false;

    public FinePositionArgument(final boolean centerIntegers) {
        this.centerIntegers = centerIntegers;
    }

    public FinePositionArgument() {}

    @Override
    public @NotNull ArgumentType<FinePositionResolver> getNativeType() {
        return ArgumentTypes.finePosition(centerIntegers);
    }
}
