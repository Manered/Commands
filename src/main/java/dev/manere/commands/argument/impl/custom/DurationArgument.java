package dev.manere.commands.argument.impl.custom;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import dev.manere.commands.argument.Argument;
import dev.manere.commands.completion.Completion;
import dev.manere.commands.completion.Suggestions;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("UnstableApiUsage")
public class DurationArgument implements Argument<Duration, String> {
    private static final Pattern COMPONENT_PATTERN = Pattern.compile("(\\d+)\\s*(y|mo|w|d|h|m|s)", Pattern.CASE_INSENSITIVE);

    @NotNull
    @Override
    public Duration convert(final @NotNull CommandSourceStack stack, final @NotNull String nativeValue) throws CommandSyntaxException {
        final Matcher matcher = COMPONENT_PATTERN.matcher(nativeValue.trim());
        Duration total = Duration.ZERO;
        int lastEnd = 0;

        while (matcher.find()) {
            if (matcher.start() != lastEnd) {
                throw new SimpleCommandExceptionType(() -> "Invalid duration format. Example: 1d12h, 30m, 5s").create();
            }
            final long amount = Long.parseLong(matcher.group(1));
            final String unit = matcher.group(2).toLowerCase();

            final Duration segment = switch (unit) {
                case "y" -> Duration.ofDays(amount * 365);
                case "mo" -> Duration.ofDays(amount * 30);
                case "w" -> Duration.ofDays(amount * 7);
                case "d" -> Duration.ofDays(amount);
                case "h" -> Duration.ofHours(amount);
                case "m" -> Duration.ofMinutes(amount);
                case "s" -> Duration.ofSeconds(amount);
                default -> throw new SimpleCommandExceptionType(() -> "Unknown duration unit: " + unit).create();
            };

            total = total.plus(segment);
            lastEnd = matcher.end();
        }

        if (lastEnd != nativeValue.trim().length() || total.isZero()) {
            throw new SimpleCommandExceptionType(() -> "Invalid duration format. Example: 1d12h, 30m, 5s").create();
        }

        return total;
    }

    @NotNull
    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

    @NotNull
    @Override
    public Suggestions<?> getDefaultCompletions() {
        return Suggestions.suggest(
            Completion.completion("1h"),
            Completion.completion("1d"),
            Completion.completion("7d"),
            Completion.completion("30d")
        );
    }
}
