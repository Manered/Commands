package dev.manere.commands;

import com.google.common.collect.ImmutableList;
import dev.manere.commands.api.APIHolder;
import dev.manere.commands.api.CommandManager;
import dev.manere.commands.api.CommandsAPI;
import dev.manere.commands.argument.CommandArgument;
import dev.manere.commands.handler.ExecutionHandler;
import dev.manere.commands.info.CommandInfo;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;

/**
 * Represents a command node in a command tree structure.
 * A basic command node may contain information about the command, its children,
 * and the command's literal and arguments.
 * This interface also extends {@link ExecutionHandler}, allowing for command execution handling.
 *
 * @see CommandManager
 * @see CommandsAPI
 * @see CommandNode
 */
public interface BasicCommandNode extends ExecutionHandler {
    /**
     * Registers a command node with the provided {@link CommandsAPI}.
     *
     * @param api  The CommandsAPI instance used to manage command nodes.
     * @param root The root command node to register.
     */
    static void register(final @NotNull CommandsAPI api, final @NotNull BasicCommandNode root) {
        api.manager().register(root);
    }

    /**
     * Registers a command node by its class type with the provided {@link CommandsAPI}.
     * <p>
     * If the root class does not have an empty constructor, this method attempts to find
     * a constructor that accepts a {@link JavaPlugin} instance and registers using it.
     * </p>
     *
     * @param api  The CommandsAPI instance used to manage command nodes.
     * @param root The class type of the root command node to register.
     * @throws RuntimeException If no suitable constructor is found.
     */
    static void register(final @NotNull CommandsAPI api, final @NotNull Class<? super BasicCommandNode> root) {
        try {
            register(api, (BasicCommandNode) root.getDeclaredConstructor().newInstance());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            api.plugin().getLogger().severe("Attempted to register a basic command node. Couldn't find a empty constructor, attempting to provide a plugin inside the parameters.");

            try {
                register(api, (BasicCommandNode) root.getDeclaredConstructor(api.plugin().getClass()).newInstance(api.plugin()));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                api.plugin().getLogger().severe("Attempted to register a basic command node. Couldn't find a empty constructor, attempting to provide a plugin inside the parameters.");
                throw new RuntimeException("Attempted to register a basic command node, couldn't find empty constructor and constructor of parameters JavaPlugin/Plugin/" + api.plugin().getClass().getSimpleName());
            }

            throw new RuntimeException(e);
        }
    }

    /**
     * Registers a command node using the default {@link CommandsAPI} instance.
     *
     * @param root The root command node to register.
     */
    static void register(final @NotNull BasicCommandNode root) {
        register(APIHolder.api(), root);
    }

    /**
     * Registers a command node by its class type using the default {@link CommandsAPI} instance.
     *
     * @param root The class type of the root command node to register.
     */
    static void register(final @NotNull Class<? super BasicCommandNode> root) {
        register(APIHolder.api(), root);
    }

    /**
     * Registers this command node using the default {@link CommandsAPI} instance.
     */
    default void register() {
        register(this);
    }

    /**
     * Registers this command node with the provided {@link CommandsAPI} instance.
     *
     * @param api The CommandsAPI instance used to manage command nodes.
     */
    default void register(final @NotNull CommandsAPI api) {
        register(api, this);
    }

    /**
     * Gets the command information associated with this command node.
     *
     * @return the {@link CommandInfo} object containing the command's metadata,
     *         or {@code null} if no information is associated with this command node.
     */
    @Nullable
    CommandInfo info();

    /**
     * Gets the literal string representing this command node.
     * The literal is typically the name or identifier of the command.
     *
     * @return a non-null string representing the literal of this command node.
     */
    @NotNull
    String literal();

    /**
     * Gets an unmodifiable list of arguments that this command node accepts.
     * This method returns an empty list by default, which can be overridden by implementing classes.
     *
     * @return a non-null unmodifiable list of {@link CommandArgument} objects.
     */
    @NotNull
    @Unmodifiable
    default List<CommandArgument<?>> arguments() {
        return ImmutableList.of();
    }

    /**
     * Gets an unmodifiable list of child command nodes for this command node.
     * Child nodes represent sub-commands or additional layers in the command hierarchy.
     * This method returns an empty list by default, which can be overridden by implementing classes.
     *
     * @return a non-null unmodifiable list of {@link BasicCommandNode} objects representing the child nodes.
     */
    @NotNull
    @Unmodifiable
    default List<BasicCommandNode> children() {
        return ImmutableList.of();
    }
}
