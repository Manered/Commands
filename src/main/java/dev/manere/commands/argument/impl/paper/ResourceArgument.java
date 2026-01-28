package dev.manere.commands.argument.impl.paper;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.manere.commands.argument.Argument;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.registry.RegistryKey;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class ResourceArgument<T> implements Argument<T, T> {
    private final RegistryKey<T> registryKey;

    public ResourceArgument(RegistryKey<T> registryKey) {
        this.registryKey = registryKey;
    }

    @Override
    public @NotNull T convert(@NotNull CommandSourceStack stack, @NotNull T nativeValue) throws CommandSyntaxException {
        return nativeValue;
    }

    @Override
    public @NotNull ArgumentType<T> getNativeType() {
        return ArgumentTypes.resource(registryKey);
    }
}
