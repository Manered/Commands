package dev.manere.commands.argument.impl.paper;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.manere.commands.argument.Argument;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class ResourceKeyArgument<T> implements Argument<TypedKey<T>, TypedKey<T>> {
    private final RegistryKey<T> registryKey;

    public ResourceKeyArgument(RegistryKey<T> registryKey) {
        this.registryKey = registryKey;
    }

    @Override
    public @NotNull TypedKey<T> convert(@NotNull CommandSourceStack stack, @NotNull TypedKey<T> nativeValue) throws CommandSyntaxException {
        return nativeValue;
    }

    @Override
    public @NotNull ArgumentType<TypedKey<T>> getNativeType() {
        return ArgumentTypes.resourceKey(registryKey);
    }
}
