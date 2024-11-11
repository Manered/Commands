package dev.manere.commands;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import dev.manere.commands.api.CommandManager;
import dev.manere.commands.api.CommandsAPI;
import dev.manere.commands.argument.CommandArgument;
import dev.manere.commands.handler.ExecutionHandler;
import dev.manere.commands.handler.CommandRequirement;
import dev.manere.commands.info.CommandInfo;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Represents a command node in a command structure, holding its literal,
 * execution handler, subcommands, and requirements.
 *
 * @see CommandManager
 * @see CommandsAPI
 * @see CommandNode
 */
public class CommandNode {
    private final String literal;
    private final CommandInfo info;

    private ExecutionHandler execution = ctx -> {};

    private final List<CommandNode> subcommands = new ArrayList<>();
    private final Set<CommandRequirement> requirements = new HashSet<>();

    @Nullable
    private CommandNode parent = null;

    private final List<CommandArgument<?>> arguments = new ArrayList<>();

    private CommandNode(final @NotNull String literal, final @NotNull CommandInfo info) {
        this.literal = literal;
        this.info = info;
    }

    /**
     * Creates a CommandNode with the specified literal and default CommandInfo.
     *
     * @param literal the command literal.
     * @return a new CommandNode instance.
     */
    @NotNull
    public static CommandNode literal(final @NotNull String literal) {
        return of(literal, CommandInfo.info());
    }

    /**
     * Creates a CommandNode with the specified literal and default CommandInfo.
     *
     * @param literal the command literal.
     * @return a new CommandNode instance.
     */
    @NotNull
    public static CommandNode node(final @NotNull String literal) {
        return literal(literal);
    }

    /**
     * Creates a CommandNode with the specified literal and CommandInfo.
     *
     * @param literal the command literal.
     * @param info    the command info.
     * @return a new CommandNode instance.
     */
    @NotNull
    public static CommandNode of(final @NotNull String literal, final @NotNull CommandInfo info) {
        return new CommandNode(literal, info);
    }

    /**
     * Creates a new Builder for constructing a CommandNode.
     *
     * @return a new Builder instance.
     */
    @NotNull
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Creates a new Builder and applies a consumer to it.
     *
     * @param consumer a consumer to configure the builder.
     * @return a new CommandNode instance configured by the consumer.
     */
    @NotNull
    public static Builder builder(final @NotNull Consumer<Builder> consumer) {
        final Builder builder = builder();
        consumer.accept(builder);
        return builder;
    }

    @NotNull
    @Unmodifiable
    public List<CommandNode> subcommands() {
        return ImmutableList.copyOf(subcommands);
    }

    @NotNull
    @Unmodifiable
    public List<CommandArgument<?>> arguments() {
        return ImmutableList.copyOf(arguments);
    }

    @NotNull
    public String literal() {
        return literal;
    }

    @NotNull
    public CommandInfo info() {
        return info;
    }

    /**
     * Adds a command requirement to this node.
     *
     * @param requirement the command requirement to add.
     * @return this CommandNode instance.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandNode require(final @NotNull CommandRequirement requirement) {
        this.requirements.add(requirement);
        return this;
    }

    /**
     * Adds a command argument to this node.
     *
     * @param argument the command argument to add.
     * @return this CommandNode instance.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandNode argument(final @NotNull CommandArgument<?> argument) {
        this.arguments.add(argument);
        return this;
    }

    /**
     * Adds a subcommand to this command node.
     *
     * @param subcommand the subcommand to add.
     * @return this CommandNode instance.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandNode subcommand(final @NotNull CommandNode subcommand) {
        subcommand.parent(this);
        this.subcommands.add(subcommand);
        return this;
    }

    /**
     * Adds a subcommand to this command node.
     *
     * @param bcn the subcommand to add.
     * @return this CommandNode instance.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandNode subcommand(final @NotNull BasicCommandNode bcn) {
        final CommandNode subcommand = CommandManager.convert(bcn);

        subcommand.parent(this);
        this.subcommands.add(subcommand);

        return this;
    }

    /**
     * Sets the execution handler for this command node.
     *
     * @param execution the execution handler to set.
     * @return this CommandNode instance.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandNode handler(final @NotNull ExecutionHandler execution) {
        this.execution = execution;
        return this;
    }

    /**
     * Sets the execution handler for this command node.
     *
     * @param execution the execution handler to set.
     * @return this CommandNode instance.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandNode executes(final @NotNull ExecutionHandler execution) {
        return handler(execution);
    }

    @NotNull
    public ExecutionHandler execution() {
        return execution;
    }

    @Nullable
    public CommandNode parent() {
        return parent;
    }

    @NotNull
    @CanIgnoreReturnValue
    CommandNode parent(final @NotNull CommandNode parent) {
        this.parent = parent;
        return this;
    }

    /**
     * Retrieves all nodes at the specified position in the command hierarchy.
     *
     * @param position the position in the command hierarchy.
     * @return a list of command nodes at the specified position.
     */
    @NotNull
    @Unmodifiable
    public List<CommandNode> nodesAtPosition(final int position) {
        if (position < 0) {
            if (position == -1) return Collections.singletonList(this.root());
            else throw new IllegalArgumentException("Position must be non-negative or -1 for root.");
        }

        return nodesAtPosition(root(), position);
    }

    @NotNull
    @Unmodifiable
    private List<CommandNode> nodesAtPosition(final @NotNull CommandNode node, final int position) {
        if (position == 0) return new ArrayList<>(node.subcommands());

        final List<CommandNode> result = new ArrayList<>();
        for (CommandNode subcommand : node.subcommands()) {
            result.addAll(nodesAtPosition(subcommand, position - 1));
        }

        return ImmutableList.copyOf(result);
    }

    /**
     * Retrieves the root command node of this command node.
     *
     * @return the root CommandNode.
     */
    @NotNull
    public CommandNode root() {
        CommandNode current = this;
        while (current.parent != null) current = current.parent;
        return current;
    }

    /**
     * Calculates the argument offset based on the parent nodes.
     *
     * @return the number of parent nodes.
     */
    public int argumentOffset() {
        if (parent == null) return 0;

        int offset = 0;
        CommandNode current = this;

        while (current.parent != null) {
            current = current.parent;
            offset++;
        }

        return offset;
    }

    @NotNull
    @Unmodifiable
    public Set<CommandRequirement> requirements() {
        return ImmutableSet.copyOf(requirements);
    }

    /**
     * Retrieves all subcommands within this command node and its children.
     *
     * @return a list of all subcommands.
     */
    @NotNull
    @Unmodifiable
    public List<CommandNode> allSubcommands() {
        final List<CommandNode> allSubcommands = new ArrayList<>();
        collectSubcommands(this.root(), allSubcommands);
        return ImmutableList.copyOf(allSubcommands);
    }

    @ApiStatus.Internal
    private void collectSubcommands(final @NotNull CommandNode node, final @NotNull List<CommandNode> allSubcommands) {
        for (final CommandNode subcommand : node.subcommands()) {
            allSubcommands.add(subcommand);
            collectSubcommands(subcommand, allSubcommands);
        }
    }

    @NotNull
    @Override
    public String toString() {
        if (root().literal().equals(literal())) return "Root of command named " + literal;

        return "Node of literal " + literal + " with root being " + root().literal;
    }

    /**
     * Builder for constructing a CommandNode.
     */
    public static class Builder {
        private String literal = null;
        private CommandInfo info = CommandInfo.info();

        private Builder() {}

        @Nullable
        public String literal() {
            return literal;
        }

        /**
         * Sets the literal for this command node.
         *
         * @param literal the command literal to set.
         * @return this Builder instance.
         */
        @NotNull
        @CanIgnoreReturnValue
        public Builder literal(final @NotNull String literal) {
            this.literal = literal;
            return this;
        }

        /**
         * Gets the command info for this builder.
         *
         * @return the command info.
         */
        @NotNull
        public CommandInfo info() {
            if (info == null) this.info = CommandInfo.info();
            return info;
        }

        /**
         * Sets the command info for this builder.
         *
         * @param info the command info to set.
         * @return this Builder instance.
         */
        @NotNull
        @CanIgnoreReturnValue
        public Builder info(final @NotNull CommandInfo info) {
            this.info = info;
            return this;
        }

        /**
         * Sets the command info using a supplier.
         *
         * @param supplier the supplier for command info.
         * @return this Builder instance.
         */
        @NotNull
        @CanIgnoreReturnValue
        public Builder info(final @NotNull Supplier<CommandInfo> supplier) {
            this.info = supplier.get();
            return this;
        }

        /**
         * Configures the command info using a consumer.
         *
         * @param consumer the consumer to configure the command info.
         * @return this Builder instance.
         */
        @NotNull
        @CanIgnoreReturnValue
        public Builder info(final @NotNull Consumer<CommandInfo> consumer) {
            this.info = CommandInfo.info();
            consumer.accept(this.info);
            return this;
        }

        /**
         * Builds and returns a new CommandNode instance.
         *
         * @return a new CommandNode instance.
         */
        @NotNull
        public CommandNode build() {
            return CommandNode.of(literal, info);
        }
    }
}
