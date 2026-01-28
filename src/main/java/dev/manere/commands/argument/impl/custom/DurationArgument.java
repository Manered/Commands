package dev.manere.commands.argument.impl.custom;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import dev.manere.commands.argument.Argument;
import dev.manere.commands.completion.Suggestions;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("UnstableApiUsage")
public class DurationArgument implements Argument<Duration, String> {
    private static final Pattern DURATION_PATTERN = Pattern.compile("^(\\d+)([dhms])$");

    @NotNull
    @Override
    public Duration convert(final @NotNull CommandSourceStack stack, final @NotNull String nativeValue) throws CommandSyntaxException {
        final Matcher matcher = DURATION_PATTERN.matcher(nativeValue);
        if (!matcher.matches()) {
            throw new SimpleCommandExceptionType(() -> "Invalid duration format. Use format like 1d, 10h, 30m, 5s").create();
        }

        final long amount = Long.parseLong(matcher.group(1));
        final String unit = matcher.group(2);

        return switch (unit) {
            case "d" -> Duration.ofDays(amount);
            case "h" -> Duration.ofHours(amount);
            case "m" -> Duration.ofMinutes(amount);
            case "s" -> Duration.ofSeconds(amount);
            default -> Duration.ZERO;
        };
    }

    @NotNull
    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }
}