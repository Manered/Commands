package dev.manere.commands;

import com.mojang.brigadier.context.ParsedArgument;
import dev.manere.commands.argument.Argument;
import dev.manere.commands.argument.CommandArgument;
import dev.manere.commands.argument.SingleCommandArgument;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("UnstableApiUsage")
public final class CommandContext<S extends CommandSender> {
    private final S source;
    private final CommandNode node;

    private final String input;

    private final com.mojang.brigadier.context.CommandContext<CommandSourceStack> stack;

    public CommandContext(final @NotNull S source, final @NotNull CommandNode node, final @NotNull com.mojang.brigadier.context.CommandContext<CommandSourceStack> stack, final @NotNull String input) {
        this.source = source;
        this.node = node;
        this.input = input;
        this.stack = stack;
    }

    @NotNull
    public S getSource() {
        return source;
    }

    @NotNull
    public <T> T getRequiredArgument(final @NotNull String key, final @NotNull Class<T> type) {
        return getRequiredArgument(type, key);
    }

    @NotNull
    public <T> T getRequiredArgument(final @NotNull Class<T> type, final @NotNull String key) {
        return getOptionalArgument(type, key).orElseThrow(() -> {
            final String similarFound = findSimilar(key);
            String text = "Required argument " + key + " not found.";
            if (similarFound != null) {
                text += ", did you mean " + similarFound;
            }

            return new RuntimeException(text);
        });
    }

    @ApiStatus.Internal
    private boolean isSimilar(final @NotNull String a, final @NotNull String b) {
        if (a.equalsIgnoreCase(b)) return true;

        int maxLength = Math.max(a.length(), b.length());
        int distance = levenshteinDistance(a, b);

        return distance <= Math.max(1, maxLength / 4);
    }

    @ApiStatus.Internal
    private int levenshteinDistance(final @NotNull String a, final @NotNull String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];

        for (int i = 0; i <= a.length(); i++) {
            for (int j = 0; j <= b.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = Math.min(
                        Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                        dp[i - 1][j - 1] + (a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1)
                    );
                }
            }
        }

        return dp[a.length()][b.length()];
    }

    @Nullable
    @ApiStatus.Internal
    private String findSimilar(final @NotNull String provided) {
        for (final CommandArgument argument : getNode().arguments()) {
            final String key = argument.getKey();
            if (isSimilar(key, provided)) {
                return key;
            }
        }

        return null;
    }

    @NotNull
    public <T> Optional<T> getOptionalArgument(final @NotNull String key, final @NotNull Class<T> type) {
        return getOptionalArgument(type, key);
    }

    @NotNull
    @SuppressWarnings("unchecked")
    public <T> Optional<T> getOptionalArgument(final @NotNull Class<T> type, final @NotNull String key) {
        try {
            final Field field = stack.getClass().getDeclaredField("arguments");
            field.setAccessible(true);
            final Map<String, ParsedArgument<S, ?>> object = (Map<String, ParsedArgument<S, ?>>) field.get(stack);

            final var argument = (SingleCommandArgument<? extends Argument<Object, Object>>) getArgument(key);
            if (argument == null) return Optional.empty();

            final ParsedArgument<S, ?> parsed = object.get(key);
            if (parsed == null) return Optional.empty();

            final Object result = parsed.getResult();

            final Argument<Object, Object> converter = argument.getArgument().get();
            return Optional.ofNullable(type.cast(converter.convert(stack.getSource(), result)));
        } catch (final Exception e) {
            return Optional.empty();
        }
    }

    @Nullable
    @ApiStatus.Internal
    private SingleCommandArgument<? extends Argument<?, ?>> getArgument(final @NotNull String key) {
        for (final CommandArgument argument : node.arguments()) {
            if (argument instanceof SingleCommandArgument<?> single) {
                if (single.getKey().equals(key)) return single;
            }
        }

        return null;
    }

    @NotNull
    public CommandNode getNode() {
        return node;
    }

    @NotNull
    public String getInput() {
        return input;
    }

    @Override
    public String toString() {
        return "CommandContext[source = " + source + ", node = " + node + ", input = " + input + "]";
    }
}
