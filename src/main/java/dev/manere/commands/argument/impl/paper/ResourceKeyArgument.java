package dev.manere.commands.argument.impl.paper;

import com.mojang.brigadier.arguments.ArgumentType;
import dev.manere.commands.argument.Argument;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import org.jetbrains.annotations.NotNull;

public class ResourceKeyArgument<T> implements Argument<T, TypedKey<T>> {
    private final RegistryKey<T> registryKey;

    public ResourceKeyArgument(final @NotNull RegistryKey<T> registryKey) {
        this.registryKey = registryKey;
    }

    @Override
    public @NotNull ArgumentType<TypedKey<T>> getNativeType() {
        return ArgumentTypes.resourceKey(registryKey);
    }
}
