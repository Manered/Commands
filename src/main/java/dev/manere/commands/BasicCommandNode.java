package dev.manere.commands;

import dev.manere.commands.argument.CommandArgument;
import dev.manere.commands.handler.ExecutionHandler;
import dev.manere.commands.info.CommandInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.List;

/**
 * Represents a command node in a command tree structure.
 * A basic command node may contain information about the command, its children,
 * and the command's literal and arguments.
 * This interface also extends {@link ExecutionHandler}, allowing for command execution handling.
 */
public interface BasicCommandNode extends ExecutionHandler {
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
        return Collections.emptyList();
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
        return Collections.emptyList();
    }
}
