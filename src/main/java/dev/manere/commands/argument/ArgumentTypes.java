package dev.manere.commands.argument;

import com.google.common.collect.ImmutableList;
import dev.manere.commands.argument.impl.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Utility class for managing different types of arguments.
 *
 * @see Argument
 * @see CommandArgument
 */
public class ArgumentTypes implements Iterable<Supplier<Argument<?>>> {
    public static final Supplier<Argument<?>> DOUBLE = DoubleArgument::new;
    public static final Supplier<Argument<?>> ENTITY_TYPE = EntityTypeArgument::new;
    public static final Supplier<Argument<?>> FLOAT = FloatArgument::new;
    public static final Supplier<Argument<?>> GAME_MODE = GameModeArgument::new;
    public static final Supplier<Argument<?>> GREEDY_STRING = GreedyStringArgument::new;
    public static final Supplier<Argument<?>> GREEDY_TEXT = GreedyTextArgument::new;
    public static final Supplier<Argument<?>> INTEGER = IntegerArgument::new;
    public static final Supplier<Argument<?>> LONG = LongArgument::new;
    public static final Supplier<Argument<?>> MATERIAL = MaterialArgument::new;
    public static final Supplier<Argument<?>> OFFLINE_PLAYER = OfflinePlayerArgument::new;
    public static final Supplier<Argument<?>> PLAYER = PlayerArgument::new;
    public static final Supplier<Argument<?>> STRING = StringArgument::new;
    public static final Supplier<Argument<?>> TEXT = TextArgument::new;

    /**
     * Gets a list of all argument types.
     *
     * @return a list of classes representing all argument types.
     */
    @NotNull
    public static List<Supplier<Argument<?>>> values() {
        final List<Supplier<Argument<?>>> values = new ArrayList<>();

        for (final Field field : ArgumentTypes.class.getDeclaredFields()) {
            try {
                @SuppressWarnings("unchecked")
                final Supplier<Argument<?>> value = (Supplier<Argument<?>>) field.get(null);

                values.add(value);
            } catch (IllegalAccessException ignored) {}
        }

        return values;
    }

    @NotNull
    public static Supplier<Argument<?>> doubleArgument() {
        return DOUBLE;
    }

    @NotNull
    public static Supplier<Argument<?>> entityType() {
        return ENTITY_TYPE;
    }

    @NotNull
    public static Supplier<Argument<?>> floatArgument() {
        return FLOAT;
    }

    @NotNull
    public static Supplier<Argument<?>> gameMode() {
        return GAME_MODE;
    }

    @NotNull
    public static Supplier<Argument<?>> greedyString() {
        return GREEDY_STRING;
    }

    @NotNull
    public static Supplier<Argument<?>> greedyText() {
        return GREEDY_TEXT;
    }

    @NotNull
    public static Supplier<Argument<?>> integer() {
        return INTEGER;
    }

    @NotNull
    public static Supplier<Argument<?>> longArgument() {
        return LONG;
    }

    @NotNull
    public static Supplier<Argument<?>> material() {
        return MATERIAL;
    }

    @NotNull
    public static Supplier<Argument<?>> offlinePlayer() {
        return OFFLINE_PLAYER;
    }

    @NotNull
    public static Supplier<Argument<?>> player() {
        return PLAYER;
    }

    @NotNull
    public static Supplier<Argument<?>> string() {
        return STRING;
    }

    @NotNull
    public static Supplier<Argument<?>> text() {
        return TEXT;
    }

    @NotNull
    @Override
    public Iterator<Supplier<Argument<?>>> iterator() {
        return values().iterator();
    }

    @NotNull
    public Stream<Supplier<Argument<?>>> stream() {
        return ImmutableList.copyOf(iterator()).stream();
    }

    @NotNull
    public Stream<Supplier<Argument<?>>> parallelStream() {
        return ImmutableList.copyOf(iterator()).parallelStream();
    }
}
