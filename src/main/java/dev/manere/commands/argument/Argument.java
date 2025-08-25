package dev.manere.commands.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.manere.commands.completion.CompletionProvider;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.resolvers.ArgumentResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public interface Argument<V, N> {
    @Nullable
    @SuppressWarnings("unchecked")
    default V convert(final @NotNull CommandSourceStack stack, final @NotNull N nativeValue) throws CommandSyntaxException {
        if (nativeValue instanceof ArgumentResolver<?> argumentResolver) {
            final var values = argumentResolver.resolve(stack);
            try {
                return values instanceof List<?> list ? list.isEmpty() ? null : (V) list.getFirst() : (V) values;
            } catch (final Exception e) {
                return null;
            }
        }

        try {
            return (V) nativeValue;
        } catch (final Exception e) {
            return null;
        }
    }

    @NotNull
    ArgumentType<N> getNativeType();

    @NotNull
    default CompletionProvider<?> getDefaultCompletions() {
        return CompletionProvider.EMPTY;
    }
}
