package dev.manere.commands;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import dev.manere.commands.argument.CommandArgument;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class CommandNode {
    @NotNull
    private final String literal;

    @Nullable
    private String permission = null;

    @NotNull
    private final Set<String> aliases = new HashSet<>();

    @NotNull
    private final List<Predicate<CommandSender>> filters;

    @NotNull
    private final List<CommandArgument> arguments = new LinkedList<>();

    @NotNull
    private final List<CommandNode> children = new ArrayList<>();

    @NotNull
    private final Map<Class<? extends CommandSender>, Consumer<CommandContext<? extends CommandSender>>> executors = new ConcurrentHashMap<>();

    @Nullable
    private CommandNode parent = null;

    @Nullable
    private String description = null;

    public CommandNode(final @NotNull String literal) {
        this.literal = literal;
        this.filters = new ArrayList<>();
        this.filters.add(sender -> {
            final String permission = permission().orElse(null);
            return permission != null && !sender.hasPermission(permission);
        });
    }

    @NotNull
    public static CommandNode node(final @NotNull String literal) {
        return new CommandNode(literal);
    }

    @NotNull
    public static CommandNode literal(final @NotNull String literal) {
        return node(literal);
    }

    @NotNull
    public static CommandNode of(final @NotNull String literal) {
        return literal(literal);
    }

    @NotNull
    public String literal() {
        return literal;
    }

    @NotNull
    public Optional<String> permission() {
        return permission == null ? parent != null ? Optional.ofNullable(parent.permission) : Optional.empty() : Optional.of(permission);
    }

    @NotNull
    @CanIgnoreReturnValue
    public CommandNode permission(final @Nullable String permission) {
        this.permission = permission;
        return this;
    }

    @NotNull
    @Unmodifiable
    public Set<String> aliases() {
        return Set.copyOf(aliases);
    }

    @NotNull
    @CanIgnoreReturnValue
    public CommandNode aliases(final @NotNull Collection<String> aliases) {
        this.aliases.addAll(aliases);
        return this;
    }

    @NotNull
    @CanIgnoreReturnValue
    public CommandNode aliases(final @NotNull String @NotNull ... aliases) {
        this.aliases.addAll(Set.of(aliases));
        return this;
    }

    @NotNull
    @Unmodifiable
    public List<Predicate<CommandSender>> filters() {
        return List.copyOf(filters);
    }

    @NotNull
    @Unmodifiable
    public List<Predicate<CommandSender>> requirements() {
        return filters();
    }

    @NotNull
    @CanIgnoreReturnValue
    public CommandNode filter(final @NotNull Predicate<CommandSender> filter) {
        this.filters.add(filter);
        return this;
    }

    @NotNull
    @CanIgnoreReturnValue
    public CommandNode requires(final @NotNull Predicate<CommandSender> requires) {
        return filter(requires);
    }

    @NotNull
    @Unmodifiable
    public List<? extends CommandArgument> arguments() {
        return List.copyOf(arguments);
    }

    @NotNull
    @CanIgnoreReturnValue
    public CommandNode argument(final @NotNull CommandArgument argument) {
        this.arguments.add(argument);
        return this;
    }

    @NotNull
    @Unmodifiable
    public List<CommandNode> children() {
        return List.copyOf(children);
    }

    @NotNull
    @Unmodifiable
    public List<CommandNode> subcommands() {
        return children();
    }

    @NotNull
    @CanIgnoreReturnValue
    public CommandNode subcommand(final @NotNull CommandNode child) {
        this.children.add(child);
        return this;
    }

    @NotNull
    @Unmodifiable
    public Map<Class<? extends CommandSender>, Consumer<CommandContext<? extends CommandSender>>> executors() {
        return Map.copyOf(executors);
    }

    @NotNull
    @CanIgnoreReturnValue
    public CommandNode executes(final @NotNull Consumer<CommandContext<CommandSender>> executor) {
        return executes(CommandSender.class, executor);
    }

    @NotNull
    @CanIgnoreReturnValue
    @SuppressWarnings("unchecked")
    public <S extends CommandSender> CommandNode executes(final @NotNull Class<S> senderType, final @NotNull Consumer<CommandContext<S>> executor) {
        this.executors.put(senderType, ctx -> executor.accept((CommandContext<S>) ctx));
        return this;
    }

    @NotNull
    @CanIgnoreReturnValue
    public <S extends CommandSender> CommandNode executes(final @NotNull Class<S> senderType, final @NotNull BiConsumer<S, CommandContext<S>> executor) {
        return executes(senderType, ctx -> executor.accept(ctx.getSource(), ctx));
    }

    @NotNull
    public Optional<CommandNode> parent() {
        return Optional.ofNullable(parent);
    }

    @NotNull
    @ApiStatus.Internal
    @CanIgnoreReturnValue
    public CommandNode parent(final @NotNull CommandNode parent) {
        this.parent = parent;
        return this;
    }

    @NotNull
    @CanIgnoreReturnValue
    public CommandNode description(final @Nullable String description) {
        this.description = description;
        return this;
    }

    @NotNull
    public Optional<String> description() {
        return Optional.ofNullable(description);
    }

    @NotNull
    public CommandNode root() {
        CommandNode current = this;
        while (current.parent != null) current = current.parent;
        return current;
    }

    public boolean isRoot() {
        return parent().isEmpty();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CommandNode other)) return false;
        return other.literal().equals(this.literal()) && other.parent().equals(this.parent());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName()
            + "["
            + "literal = " + literal
            + ", permission = " + permission
            + ", aliases = " + aliases
            + ", filters.size = " + filters.size()
            + ", arguments = " + arguments
            + ", children.size = " + children.size()
            + ", executors = " + executors
            + ", description = " + description
            + "]";
    }
}
