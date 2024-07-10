package dev.manere.commands;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import dev.manere.commands.argument.CommandArgument;
import dev.manere.commands.handler.ExecutionHandler;
import dev.manere.commands.handler.CommandRequirement;
import dev.manere.commands.handler.Suggestion;
import dev.manere.commands.handler.SuggestionHandler;
import dev.manere.commands.info.CommandInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CommandNode {
    private final String literal;
    private final CommandInfo info;

    private ExecutionHandler execution = ctx -> {};

    private final Set<CommandNode> subcommands = new HashSet<>();
    private final Set<CommandRequirement> requirements = new HashSet<>();

    @Nullable
    private CommandNode parent = null;

    private final List<CommandArgument<?>> arguments = new ArrayList<>();

    private CommandNode(final @NotNull String literal, final @NotNull CommandInfo info) {
        this.literal = literal;
        this.info = info;
    }

    @NotNull
    public static CommandNode literal(final @NotNull String literal) {
        return of(literal, CommandInfo.info());
    }

    @NotNull
    public static CommandNode of(final @NotNull String literal, final @NotNull CommandInfo info) {
        return new CommandNode(literal, info);
    }

    @NotNull
    public static Builder builder() {
        return new Builder();
    }

    @NotNull
    public static Builder builder(final @NotNull Consumer<Builder> consumer) {
        final Builder builder = builder();
        consumer.accept(builder);
        return builder;
    }

    @NotNull
    public Set<CommandNode> subcommands() {
        return subcommands;
    }

    @NotNull
    public List<CommandArgument<?>> arguments() {
        return arguments;
    }

    @NotNull
    public String literal() {
        return literal;
    }

    @NotNull
    public CommandInfo info() {
        return info;
    }

    @NotNull
    @CanIgnoreReturnValue
    public CommandNode require(final @NotNull CommandRequirement requirement) {
        this.requirements.add(requirement);
        return this;
    }

    @NotNull
    @CanIgnoreReturnValue
    public CommandNode argument(final @NotNull CommandArgument<?> argument) {
        this.arguments.add(argument);
        return this;
    }

    @NotNull
    @CanIgnoreReturnValue
    public CommandNode subcommand(final @NotNull CommandNode subcommand) {
        subcommand.parent(this);
        this.subcommands.add(subcommand);
        return this;
    }

    @NotNull
    @CanIgnoreReturnValue
    public CommandNode handler(final @NotNull ExecutionHandler execution) {
        this.execution = execution;
        return this;
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

    @NotNull
    public CommandNode root() {
        CommandNode current = this;
        while (current.parent != null) current = current.parent;
        return current;
    }

    public int argumentOffset() {
        if (parent == null) return 0;

        int offset = 0;
        CommandNode current = this;

        while (current.parent != null) {
            current = current.parent;
            offset += current.arguments.size();
        }

        return offset;
    }

    @NotNull
    public Set<CommandRequirement> requirements() {
        return requirements;
    }

    @NotNull
    public List<CommandNode> allSubcommands() {
        final List<CommandNode> allSubcommands = new ArrayList<>();

        collectSubcommands(this.root(), allSubcommands);
        return allSubcommands;
    }

    private void collectSubcommands(final @NotNull CommandNode node, final @NotNull List<CommandNode> allSubcommands) {
        for (final CommandNode subcommand : node.subcommands()) {
            allSubcommands.add(subcommand);
            collectSubcommands(subcommand, allSubcommands);
        }
    }

    @Override
    public String toString() {
        if (root().literal().equals(literal())) return "Root of command named " + literal;

        return "Node of literal " + literal + " with root being " + root().literal;
    }

    public static class Builder {
        private String literal = null;
        private CommandInfo info = CommandInfo.info();

        private Builder() {}

        @Nullable
        public String literal() {
            return literal;
        }

        @NotNull
        @CanIgnoreReturnValue
        public Builder literal(final @NotNull String literal) {
            this.literal = literal;
            return this;
        }

        @NotNull
        public CommandInfo info() {
            if (info == null) this.info = CommandInfo.info();
            return info;
        }

        @NotNull
        @CanIgnoreReturnValue
        public Builder info(final @NotNull CommandInfo info) {
            this.info = info;
            return this;
        }

        @NotNull
        @CanIgnoreReturnValue
        public Builder info(final @NotNull Supplier<CommandInfo> supplier) {
            this.info = supplier.get();
            return this;
        }

        @NotNull
        @CanIgnoreReturnValue
        public Builder info(final @NotNull Consumer<CommandInfo> consumer) {
            this.info = CommandInfo.info();
            consumer.accept(this.info);
            return this;
        }

        @NotNull
        public CommandNode build() {
            return CommandNode.of(literal, info);
        }
    }
}
