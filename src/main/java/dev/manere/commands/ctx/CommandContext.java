package dev.manere.commands.ctx;

import com.google.common.collect.ImmutableList;
import dev.manere.commands.CommandNode;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Represents the context in which a command is executed.
 *
 * @see CommandSource
 * @see CommandNode
 * @see CommandArguments
 */
public class CommandContext {
    private final CommandSource source;
    private final CommandNode command;
    private final String alias;
    private final CommandArguments arguments;
    private final String rootAlias;
    private final List<String> argumentsList;

    /**
     * Constructs a CommandContext with the specified parameters.
     *
     * @param source      the source of the command.
     * @param command     the command node.
     * @param alias       the alias of the command.
     * @param rootAlias   the root alias of the command.
     * @param arguments   the list of arguments.
     */
    public CommandContext(final @NotNull CommandSource source, final @NotNull CommandNode command, final @NotNull String alias, final @NotNull String rootAlias, final @NotNull List<String> arguments) {
        this.source = source;
        this.command = command;
        this.alias = alias;
        this.arguments = new CommandArguments(this);
        this.argumentsList = new ArrayList<>(arguments);
        this.rootAlias = rootAlias;
    }

    /**
     * Gets the source of the command.
     *
     * @return the source of the command.
     */
    @NotNull
    public CommandSource source() {
        return source;
    }

    /**
     * Gets the command node.
     *
     * @return the command node.
     */
    @NotNull
    public CommandNode command() {
        return command;
    }

    /**
     * Gets the alias of the command.
     *
     * @return the alias of the command.
     */
    @NotNull
    public String alias() {
        return alias;
    }

    /**
     * Gets the arguments of the command.
     *
     * @return the command arguments.
     */
    @NotNull
    public CommandArguments arguments() {
        return arguments;
    }

    /**
     * Gets the root alias of the command.
     *
     * @return the root alias of the command.
     */
    @NotNull
    public String rootAlias() {
        return rootAlias;
    }

    /**
     * Gets the raw list of arguments.
     *
     * @return the raw list of arguments.
     */
    @NotNull
    @Unmodifiable
    @ApiStatus.Obsolete
    private List<String> rawArguments() {
        return ImmutableList.copyOf(argumentsList);
    }

    /**
     * Gets the list of arguments starting from the offset.
     *
     * @return the list of arguments from the offset.
     */
    @NotNull
    @Unmodifiable
    @ApiStatus.Internal
    public List<String> argumentsFromOffset() {
        final List<String> rawArgumentsCopy = ImmutableList.copyOf(rawArguments());
        final int offset = command.argumentOffset();

        // Ensure the offset is within the bounds of the list
        if (offset >= rawArgumentsCopy.size()) {
            return new ArrayList<>();
        }

        // Return the sublist from the offset to the end of the list
        return ImmutableList.copyOf(rawArgumentsCopy.subList(offset, rawArgumentsCopy.size()));
    }
}
