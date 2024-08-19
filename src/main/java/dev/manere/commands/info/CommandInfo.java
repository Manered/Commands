package dev.manere.commands.info;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import dev.manere.commands.CommandNode;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Contains metadata about a command, such as description, permission, and aliases.
 *
 * @see CommandNode
 */
public class CommandInfo {
    private String description = null;
    private String permission = null;
    private Component permissionMessage = null;
    private final Set<String> aliases = new HashSet<>();

    /**
     * Creates a new CommandInfo object.
     *
     * @return a new CommandInfo object.
     */
    @NotNull
    public static CommandInfo info() {
        return new CommandInfo();
    }

    /**
     * Gets the description of the command.
     *
     * @return the description of the command, or null if not set.
     */
    @Nullable
    public String description() {
        return description;
    }

    /**
     * Sets the description of the command.
     *
     * @param description the description to set.
     * @return this CommandInfo object.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandInfo description(final @NotNull String description) {
        this.description = description;
        return this;
    }

    /**
     * Gets the permission required to execute the command.
     *
     * @return the permission required, or null if not set.
     */
    @Nullable
    public String permission() {
        return permission;
    }

    /**
     * Sets the permission required to execute the command.
     *
     * @param permission the permission to set.
     * @return this CommandInfo object.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandInfo permission(final @NotNull String permission) {
        this.permission = permission;
        return this;
    }

    /**
     * Sets the permission required and the permission message to be shown if permission is denied.
     *
     * @param permission the permission to set.
     * @param message    the message to be shown if permission is denied.
     * @return this CommandInfo object.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandInfo permission(final @NotNull String permission, final @NotNull Component message) {
        return permission(permission).permissionMessage(message);
    }

    /**
     * Gets the permission message to be shown if permission is denied.
     *
     * @return the permission message, or null if not set.
     */
    @Nullable
    public Component permissionMessage() {
        return permissionMessage;
    }

    /**
     * Gets the aliases of the command.
     *
     * @return the aliases of the command.
     */
    @NotNull
    @Unmodifiable
    public Set<String> aliases() {
        return ImmutableSet.copyOf(aliases);
    }

    /**
     * Sets the aliases of the command.
     *
     * @param aliases the aliases to set.
     * @return this CommandInfo object.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandInfo aliases(final @Nullable Collection<String> aliases) {
        this.aliases.clear();
        if (aliases != null) this.aliases.addAll(aliases);
        return this;
    }

    /**
     * Sets the aliases of the command.
     *
     * @param aliases the aliases to set.
     * @return this CommandInfo object.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandInfo aliases(final @NotNull String @Nullable ... aliases) {
        this.aliases.clear();
        if (aliases != null) this.aliases.addAll(Arrays.asList(aliases));
        return this;
    }

    /**
     * Adds an alias to the command.
     *
     * @param alias the alias to add.
     * @return this CommandInfo object.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandInfo addAlias(final @NotNull String alias) {
        return addAliases(alias);
    }

    /**
     * Adds multiple aliases to the command.
     *
     * @param aliases the aliases to add.
     * @return this CommandInfo object.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandInfo addAliases(final @NotNull String @NotNull ... aliases) {
        this.aliases.addAll(Arrays.asList(aliases));
        return this;
    }

    /**
     * Sets the permission message to be shown if permission is denied.
     *
     * @param permissionMessage the permission message to set.
     * @return this CommandInfo object.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandInfo permissionMessage(final @NotNull Component permissionMessage) {
        this.permissionMessage = permissionMessage;
        return this;
    }
}
