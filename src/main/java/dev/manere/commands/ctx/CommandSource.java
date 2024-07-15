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

/**
 * Represents the source of a command, typically a player or console sender.
 */
public class CommandSource implements ForwardingAudience {
    private final CommandSender sender;
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    /**
     * Constructs a CommandSource with the specified sender.
     *
     * @param sender the command sender.
     */
    private CommandSource(final @NotNull CommandSender sender) {
        this.sender = sender;
    }

    /**
     * Creates a new CommandSource instance with the specified sender.
     *
     * @param sender the command sender.
     * @return a new CommandSource instance.
     */
    @NotNull
    public static CommandSource source(final @NotNull CommandSender sender) {
        return new CommandSource(sender);
    }

    /**
     * Converts the CommandSource to a Player if possible.
     *
     * @return the Player if the sender is a Player, otherwise null.
     */
    @Nullable
    public Player asPlayer() {
        return sender instanceof Player player ? player : null;
    }

    /**
     * Checks if the CommandSource is a Player.
     *
     * @return true if the sender is a Player, otherwise false.
     */
    public boolean isPlayer() {
        return asPlayer() != null;
    }

    /**
     * Gets the command sender.
     *
     * @return the command sender.
     */
    @NotNull
    public CommandSender sender() {
        return sender;
    }

    /**
     * Sends a rich text message to the sender.
     *
     * @param input the rich text message.
     */
    public void sendRichMessage(final @NotNull String input) {
        sendMessage(miniMessage.deserialize(input));
    }

    /**
     * Sends a rich text message with tag resolvers to the sender.
     *
     * @param input       the rich text message.
     * @param tagResolver the tag resolver.
     */
    public void sendRichMessage(final @NotNull String input, final @NotNull TagResolver tagResolver) {
        sendMessage(miniMessage.deserialize(input, tagResolver));
    }

    /**
     * Sends a rich text message with multiple tag resolvers to the sender.
     *
     * @param input        the rich text message.
     * @param tagResolvers the tag resolvers.
     */
    public void sendRichMessage(final @NotNull String input, final @NotNull TagResolver... tagResolvers) {
        sendMessage(miniMessage.deserialize(input, tagResolvers));
    }

    /**
     * Gets the audiences for the command source.
     *
     * @return the audiences.
     */
    @NotNull
    @Override
    public Iterable<? extends Audience> audiences() {
        return new ArrayList<>(List.of(
            sender
        ));
    }
}