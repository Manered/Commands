package dev.manere.commands.argument.impl.paper;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.manere.commands.argument.Argument;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.SignedMessageResolver;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class SignedMessageArgument implements Argument<SignedMessageResolver, SignedMessageResolver> {
    @Override
    public @NotNull SignedMessageResolver convert(@NotNull CommandSourceStack stack, @NotNull SignedMessageResolver nativeValue) throws CommandSyntaxException {
        return nativeValue;
    }

    @Override
    public @NotNull ArgumentType<SignedMessageResolver> getNativeType() {
        return ArgumentTypes.signedMessage();
    }
}
