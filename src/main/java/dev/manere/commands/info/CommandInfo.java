package dev.manere.commands.info;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CommandInfo {
    private String description = null;
    private String permission = null;

    private Component permissionMessage = null;

    private final Set<String> aliases = new HashSet<>();

    @NotNull
    public static CommandInfo info() {
        return new CommandInfo();
    }

    @NotNull
    public static CommandInfo info(final @Nullable String description, final @Nullable String permission, final @Nullable Component permissionMessage, final @Nullable String @Nullable ... aliases) {
        final CommandInfo info = info();

        if (description != null) info.description(description);
        if (permission != null) info.permission(permission);
        if (permissionMessage != null) info.permissionMessage(permissionMessage);

        info.aliases(aliases);

        return info;
    }

    @Nullable
    public String description() {
        return description;
    }

    @NotNull
    @CanIgnoreReturnValue
    public CommandInfo description(final @NotNull String description) {
        this.description = description;
        return this;
    }

    @Nullable
    public String permission() {
        return permission;
    }

    @NotNull
    @CanIgnoreReturnValue
    public CommandInfo permission(final @NotNull String permission) {
        this.permission = permission;
        return this;
    }

    @NotNull
    @CanIgnoreReturnValue
    public CommandInfo permission(final @NotNull String permission, final @NotNull Component message) {
        return permission(permission).permissionMessage(message);
    }

    @Nullable
    public Component permissionMessage() {
        return permissionMessage;
    }

    @NotNull
    public Set<String> aliases() {
        return aliases;
    }

    @NotNull
    @CanIgnoreReturnValue
    public CommandInfo aliases(final @Nullable Collection<String> aliases) {
        this.aliases.clear();
        if (aliases != null) this.aliases.addAll(aliases);
        return this;
    }

    @NotNull
    @CanIgnoreReturnValue
    public CommandInfo aliases(final @NotNull String @Nullable... aliases) {
        this.aliases.clear();
        if (aliases != null) this.aliases.addAll(Arrays.asList(aliases));
        return this;
    }

    @NotNull
    @CanIgnoreReturnValue
    public CommandInfo addAlias(final @NotNull String alias) {
        return addAliases(alias);
    }

    @NotNull
    @CanIgnoreReturnValue
    public CommandInfo addAliases(final @NotNull String @NotNull ... aliases) {
        final Set<String> set = aliases();
        set.addAll(Arrays.asList(aliases));
        return this;
    }

    @NotNull
    @CanIgnoreReturnValue
    public CommandInfo permissionMessage(final @NotNull Component permissionMessage) {
        this.permissionMessage = permissionMessage;
        return this;
    }
}
