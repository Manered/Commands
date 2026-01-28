package dev.manere.commands.api;

import dev.manere.commands.BasicCommandNode;
import dev.manere.commands.CommandContext;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class HealCommand extends BasicCommandNode {
    @NotNull
    @Override
    public String getLiteral() {
        return "heal";
    }

    @NotNull
    @Override
    public Optional<String> getPermission() {
        return Optional.of("myplugin.commands.heal");
    }

    @NotNull
    @Override
    public Collection<String> getAliases() {
        return List.of("doctor");
    }

    @Override
    public void execute(final @NotNull CommandContext<? extends CommandSender> context) {
        if (context.getAsPlayer().isEmpty()) {
            context.sendRichMessage("Only players can heal themselves :)");
            return;
        }

        final Player player = context.getAsPlayer().get();

        player.setHealth(20.0);
        player.sendRichMessage("You have been healed");
    }
}
