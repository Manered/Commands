package dev.manere.commands.argument.impl.paper;

import com.mojang.brigadier.arguments.ArgumentType;
import dev.manere.commands.argument.Argument;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.SignedMessageResolver;
import net.kyori.adventure.chat.SignedMessage;
import org.jetbrains.annotations.NotNull;

public class SignedMessageArgument implements Argument<SignedMessage, SignedMessageResolver> {
    @Override
    public @NotNull ArgumentType<SignedMessageResolver> getNativeType() {
        return ArgumentTypes.signedMessage();
    }
}
