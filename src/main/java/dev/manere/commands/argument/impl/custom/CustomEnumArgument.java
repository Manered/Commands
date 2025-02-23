package dev.manere.commands.argument.impl.custom;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.manere.commands.argument.Argument;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("UnstableApiUsage")
public class CustomEnumArgument<E extends Enum<E>> implements Argument<E, E> {
    private final Class<E> enumClass;

    public CustomEnumArgument(final @NotNull Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public @NotNull ArgumentType<E> getNativeType() {
        return new CustomArgumentType.Converted<E, String>() {
            @Override
            public @NotNull ArgumentType<String> getNativeType() {
                return StringArgumentType.word();
            }

            @Override
            public <S> @NotNull CompletableFuture<Suggestions> listSuggestions(@NotNull CommandContext<S> context, @NotNull SuggestionsBuilder builder) {
                final String input = builder.getRemaining().toLowerCase();

                for (final E e : enumClass.getEnumConstants()) {
                    final String name = e.name();
                    final String lowerCase = name.toLowerCase();

                    if (lowerCase.startsWith(input)) {
                        builder.suggest(lowerCase);
                    }
                }

                return builder.buildFuture();
            }

            @Override
            public @NotNull E convert(@NotNull String input) throws CommandSyntaxException {
                try {
                    return Enum.valueOf(enumClass, input.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException()
                        .create("Invalid value: " + input);
                }
            }
        };
    }
}
