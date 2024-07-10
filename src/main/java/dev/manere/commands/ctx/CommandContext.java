package dev.manere.commands.ctx;

import com.google.common.collect.ImmutableList;
import dev.manere.commands.CommandNode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandContext {
    private final CommandSource source;
    private final CommandNode command;
    private final String alias;
    private final CommandArguments arguments;
    private final String rootAlias;
    private final List<String> argumentsList;

    public CommandContext(final @NotNull CommandSource source, final @NotNull CommandNode command, final @NotNull String alias, final @NotNull String rootAlias, final @NotNull List<String> arguments) {
        this.source = source;
        this.command = command;
        this.alias = alias;
        this.arguments = new CommandArguments(this);
        this.argumentsList = new ArrayList<>(arguments);

        /* This is not offset */
        this.rootAlias = rootAlias;
    }

    @NotNull
    public CommandSource source() {
        return source;
    }

    @NotNull
    public CommandNode command() {
        return command;
    }

    @NotNull
    public String alias() {
        return alias;
    }

    @NotNull
    public CommandArguments arguments() {
        return arguments;
    }

    @NotNull
    public String rootAlias() {
        return rootAlias;
    }

    @NotNull
    private List<String> rawArguments() {
        return argumentsList;
    }

    @NotNull
    List<String> argumentsFromOffset() {
        final List<String> rawArgumentsCopy = ImmutableList.sortedCopyOf(rawArguments());
        final int offset = command.argumentOffset();

        // Ensure the offset is within the bounds of the list
        if (offset >= rawArgumentsCopy.size()) {
            return new ArrayList<>();
        }

        // Return the sublist from the offset to the end of the list
        return new ArrayList<>(rawArgumentsCopy.subList(offset, rawArgumentsCopy.size()));
    }
}
