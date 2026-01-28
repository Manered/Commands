package dev.manere.commands.argument.impl.custom;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.manere.commands.argument.Argument;
import dev.manere.commands.completion.Completion;
import dev.manere.commands.completion.Suggestions;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("UnstableApiUsage")
public class EnumArgument<E extends Enum<E>> implements Argument<E, E> {
    private final Class<E> enumClass;

    public EnumArgument(final @NotNull Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    @Nullable
    @Override
    public E convert(@NotNull CommandSourceStack stack, @NotNull E nativeValue) throws CommandSyntaxException {
        return nativeValue;
    }

    @NotNull
    @Override
    public ArgumentType<E> getNativeType() {
        return new CustomArgumentType.Converted<@NotNull E, @NotNull String>() {
            @NotNull
            @Override
            public ArgumentType<String> getNativeType() {
                return StringArgumentType.word();
            }

            @NotNull
            @Override
            public <S> CompletableFuture<com.mojang.brigadier.suggestion.Suggestions> listSuggestions(final @NotNull CommandContext<S> context, final @NotNull SuggestionsBuilder builder) {
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

            @NotNull
            @Override
            public E convert(final @NotNull String input) throws CommandSyntaxException {
                try {
                    return Enum.valueOf(enumClass, input.toUpperCase());
                } catch (final IllegalArgumentException e) {
                    throw new SimpleCommandExceptionType(() -> "Invalid value: " + input).create();
                }
            }
        };
    }

    @NotNull
    @Override
    public Suggestions<?> getDefaultCompletions() {
        return Suggestions.suggest(context -> {
            final List<Completion> completions = new ArrayList<>();

            for (final E e : enumClass.getEnumConstants()) {
                final String name = e.name();
                final String lowerCase = name.toLowerCase();

                completions.add(Completion.completion(lowerCase));
            }

            return completions;
        });
    }
}
