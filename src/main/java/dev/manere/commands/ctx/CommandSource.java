package dev.manere.commands.ctx;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CommandSource implements ForwardingAudience {
    private final CommandSender sender;
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    private CommandSource(final @NotNull CommandSender sender) {
        this.sender = sender;
    }

    @NotNull
    public static CommandSource source(final @NotNull CommandSender sender) {
        return new CommandSource(sender);
    }

    @Nullable
    public Player asPlayer() {
        return sender instanceof Player player ? player : null;
    }

    public boolean isPlayer() {
        return asPlayer() != null;
    }

    @NotNull
    public CommandSender sender() {
        return sender;
    }

    public void sendRichMessage(final @NotNull String input) {
        sendMessage(miniMessage.deserialize(input));
    }

    public void sendRichMessage(final @NotNull String input, final @NotNull TagResolver tagResolver) {
        sendMessage(miniMessage.deserialize(input, tagResolver));
    }

    public void sendRichMessage(final @NotNull String input, final @NotNull TagResolver... tagResolvers) {
        sendMessage(miniMessage.deserialize(input, tagResolvers));
    }

    @NotNull
    @Override
    public Iterable<? extends Audience> audiences() {
        return new ArrayList<>(List.of(
            sender
        ));
    }
}
