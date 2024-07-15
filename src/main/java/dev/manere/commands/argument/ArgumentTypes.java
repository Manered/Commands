package dev.manere.commands.argument;

import dev.manere.commands.argument.impl.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for managing different types of arguments.
 */
public class ArgumentTypes {
    public static final Class<? extends Argument<?>> DOUBLE = DoubleArgument.class;
    public static final Class<? extends Argument<?>> ENTITY_TYPE = EntityTypeArgument.class;
    public static final Class<? extends Argument<?>> FLOAT = FloatArgument.class;
    public static final Class<? extends Argument<?>> GAME_MODE = GameModeArgument.class;
    public static final Class<? extends Argument<?>> GREEDY_STRING = GreedyStringArgument.class;
    public static final Class<? extends Argument<?>> GREEDY_TEXT = GreedyTextArgument.class;
    public static final Class<? extends Argument<?>> INTEGER = IntegerArgument.class;
    public static final Class<? extends Argument<?>> LONG = LongArgument.class;
    public static final Class<? extends Argument<?>> OFFLINE_PLAYER = OfflinePlayerArgument.class;
    public static final Class<? extends Argument<?>> PLAYER = PlayerArgument.class;
    public static final Class<? extends Argument<?>> STRING = StringArgument.class;
    public static final Class<? extends Argument<?>> TEXT = TextArgument.class;

    /**
     * Gets a list of all argument types.
     *
     * @return a list of classes representing all argument types.
     */
    @NotNull
    public static List<Class<? extends Argument<?>>> values() {
        final List<Class<? extends Argument<?>>> values = new ArrayList<>();

        for (final Field field : ArgumentTypes.class.getDeclaredFields()) {
            try {
                @SuppressWarnings("unchecked")
                final Class<? extends Argument<?>> value = (Class<? extends Argument<?>>) field.get(null);

                values.add(value);
            } catch (IllegalAccessException ignored) {}
        }

        return values;
    }

    /**
     * Finds the argument class that can handle the specified argument type.
     *
     * @param argumentType the type of the argument to find.
     * @param <T>          the type of the argument.
     * @return the class representing the argument type, or null if not found.
     */
    @Nullable
    @SuppressWarnings("unchecked")
    public static <T> Class<? extends Argument<T>> find(final @NotNull Class<T> argumentType) {
        final List<Class<? extends Argument<?>>> values = values();

        for (final Class<? extends Argument<?>> value : values) {
            try {
                final Argument<?> argument = value.getDeclaredConstructor().newInstance();
                if (argument.types().contains(argumentType)) return (Class<? extends Argument<T>>) value;
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                return null;
            }
        }

        return null;
    }

    /**
     * Creates a new instance of the specified class.
     *
     * @param clazz the class to instantiate.
     * @param <T>   the type of the class.
     * @return a new instance of the class.
     */
    @NotNull
    public static <T> T newInstance(final @NotNull Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public static Class<? extends Argument<?>> doubleArgument() {
        return DOUBLE;
    }

    @NotNull
    public static Class<? extends Argument<?>> entityType() {
        return ENTITY_TYPE;
    }

    @NotNull
    public static Class<? extends Argument<?>> floatArgument() {
        return FLOAT;
    }

    @NotNull
    public static Class<? extends Argument<?>> gameMode() {
        return GAME_MODE;
    }

    @NotNull
    public static Class<? extends Argument<?>> greedyString() {
        return GREEDY_STRING;
    }

    @NotNull
    public static Class<? extends Argument<?>> greedyText() {
        return GREEDY_TEXT;
    }

    @NotNull
    public static Class<? extends Argument<?>> integer() {
        return INTEGER;
    }

    @NotNull
    public static Class<? extends Argument<?>> longArgument() {
        return LONG;
    }

    @NotNull
    public static Class<? extends Argument<?>> offlinePlayer() {
        return OFFLINE_PLAYER;
    }

    @NotNull
    public static Class<? extends Argument<?>> player() {
        return PLAYER;
    }

    @NotNull
    public static Class<? extends Argument<?>> string() {
        return STRING;
    }

    @NotNull
    public static Class<? extends Argument<?>> text() {
        return TEXT;
    }
}
