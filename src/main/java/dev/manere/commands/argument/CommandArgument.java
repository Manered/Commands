package dev.manere.commands.argument;

import dev.manere.commands.completion.Suggestions;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class CommandArgument<A extends Argument<?, ?>> {

    @NotNull
    private final A argumentInstance;

    @NotNull
    private final String key;

    @Nullable
    private final Suggestions<?> completions;

    private final boolean required;

    @Nullable
    private final String permission;

    @NotNull
    private final List<Predicate<CommandSender>> requirements;

    private CommandArgument(
        final @NotNull Supplier<A> argument,
        final @NotNull String key,
        final @Nullable Suggestions<?> completions,
        final boolean required,
        final @Nullable String permission,
        final @NotNull List<Predicate<CommandSender>> requirements
    ) {
        this.argumentInstance = Objects.requireNonNull(argument.get(), "argument instance must not be null");
        this.key = key;

        this.completions = completions != null ? completions : this.argumentInstance.getDefaultCompletions();

        this.required = required;
        this.permission = permission;

        this.requirements = new ArrayList<>(requirements);
        this.requirements.add(sender -> {
            final String perm = permission().orElse(null);
            return perm == null || sender.hasPermission(perm);
        });
    }

    @NotNull
    public static <A extends Argument<?, ?>> CommandArgument.Builder<A> requiredArgument(final @NotNull Supplier<A> argument, final @NotNull String key) {
        return new Builder<>(argument, key, true);
    }

    @NotNull
    public static <A extends Argument<?, ?>> CommandArgument.Builder<A> requiredArgument(final @NotNull String key, final @NotNull Supplier<A> argument) {
        return requiredArgument(argument, key);
    }

    @NotNull
    public static <A extends Argument<?, ?>> CommandArgument.Builder<A> optionalArgument(final @NotNull Supplier<A> argument, final @NotNull String key) {
        return new Builder<>(argument, key, false);
    }

    @NotNull
    public static <A extends Argument<?, ?>> CommandArgument.Builder<A> optionalArgument(final @NotNull String key, final @NotNull Supplier<A> argument) {
        return optionalArgument(argument, key);
    }

    @NotNull
    public Supplier<A> getArgument() {
        return () -> argumentInstance;
    }

    @NotNull
    public A argumentInstance() {
        return argumentInstance;
    }

    @NotNull
    public String getKey() {
        return key;
    }

    @NotNull
    public Optional<Suggestions<?>> getCompletions() {
        return Optional.ofNullable(completions);
    }

    public boolean isRequired() {
        return required;
    }

    public boolean isOptional() {
        return !required;
    }

    @NotNull
    public Optional<String> permission() {
        return Optional.ofNullable(permission);
    }

    @NotNull
    @Unmodifiable
    public List<Predicate<CommandSender>> requirements() {
        return List.copyOf(requirements);
    }

    @NotNull
    @Unmodifiable
    public List<Predicate<CommandSender>> filters() {
        return requirements();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CommandArgument<?> other && other.getKey().equals(this.getKey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getKey());
    }

    @Override
    public String toString() {
        return "CommandArgument[key = " + getKey() + ", required = " + required + "]";
    }

    public static final class Builder<A extends Argument<?, ?>> {
        @NotNull
        private final Supplier<A> argument;

        @NotNull
        private final String key;

        @Nullable
        private Suggestions<?> completions = null;

        private final boolean required;

        @Nullable
        private String permission = null;

        @NotNull
        private final List<Predicate<CommandSender>> requirements = new ArrayList<>();

        private Builder(final @NotNull Supplier<A> argument, final @NotNull String key, final boolean required) {
            this.argument = argument;
            this.key = key;
            this.required = required;
        }

        @NotNull
        public Builder<A> completions(final @Nullable Suggestions<?> completions) {
            this.completions = completions;
            return this;
        }

        @NotNull
        public Builder<A> permission(final @Nullable String permission) {
            this.permission = permission;
            return this;
        }

        @NotNull
        public Builder<A> permission(final @Nullable Permission permission) {
            return permission == null ? permission((String) null) : permission(permission.getName());
        }

        @NotNull
        public Builder<A> requires(final @NotNull Predicate<CommandSender> requirement) {
            this.requirements.add(requirement);
            return this;
        }

        @NotNull
        public Builder<A> filter(final @NotNull Predicate<CommandSender> filter) {
            return requires(filter);
        }

        @NotNull
        public CommandArgument<A> build() {
            return new CommandArgument<>(argument, key, completions, required, permission, requirements);
        }
    }
}
