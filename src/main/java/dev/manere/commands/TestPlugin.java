package dev.manere.commands;

import dev.manere.commands.api.CommandManager;
import dev.manere.commands.api.CommandsAPI;
import dev.manere.commands.argument.ArgumentTypes;
import dev.manere.commands.argument.CommandArgument;
import dev.manere.commands.argument.impl.OfflinePlayerArgument;
import dev.manere.commands.argument.impl.PlayerArgument;
import dev.manere.commands.argument.impl.StringArgument;
import dev.manere.commands.handler.AsyncSuggestionHandler;
import dev.manere.commands.handler.Suggestion;
import dev.manere.commands.handler.SyncSuggestionHandler;
import dev.manere.commands.info.CommandInfo;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

import javax.management.ListenerNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class TestPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        super.onEnable();

        final CommandsAPI api = CommandsAPI.api(this);
        final CommandManager manager = api.manager();

        final CommandNode broadcastCommand = CommandNode.literal("broadcast")
            .argument(CommandArgument.argument(ArgumentTypes.greedyText(), "text"))
            .handler(context -> {
                final Component text = context.arguments().argumentOr(Component.class, "text", () -> {
                    context.source().sendRichMessage("<red>Usage: /broadcast <text>");
                    return null;
                });

                for (final Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage(Component.text("BROADCAST ", Style.style(NamedTextColor.DARK_RED, TextDecoration.BOLD)).append(text).decoration(TextDecoration.BOLD, false));
                }
            });


        final CommandNode node = CommandNode.literal("permissions")
            .handler(context -> context.source().sendRichMessage("<red>Incorrect Usage"))
            .subcommand(CommandNode.literal("anything")
                .subcommand(CommandNode.literal("add")
                    .argument(CommandArgument.builder(StringArgument.class)
                        .name("permission")
                        .suggest((SyncSuggestionHandler) context -> {
                            final List<Suggestion> permissions = new ArrayList<>();

                            for (final Permission permission : Bukkit.getPluginManager().getPermissions()) {
                                permissions.add(Suggestion.suggestion(permission.getName(), Component.text(
                                    permission.getDescription(),
                                    NamedTextColor.GRAY
                                )));
                            }

                            return permissions;
                        })
                        .build()
                    )
                    .argument(CommandArgument.argument(PlayerArgument.class, "target"))
                    .handler(context -> {
                        final String permission = context.arguments().argumentOr(String.class, "permission", () -> {
                            context.source().sendRichMessage("<red>Incorrect Usage");
                            return null;
                        });

                        final Player target = context.arguments().argumentOr(Player.class, "target", () -> context.source().asPlayer());

                        context.source().sendRichMessage("Added permission " + permission + " to player " + target.getName());
                    })
                )
                .subcommand(CommandNode.literal("remove")
                    .argument(CommandArgument.builder(StringArgument.class)
                        .name("permission")
                        .suggest((SyncSuggestionHandler) context -> {
                            final List<Suggestion> permissions = new ArrayList<>();

                            for (final Permission permission : Bukkit.getPluginManager().getPermissions()) {
                                permissions.add(Suggestion.suggestion(permission.getName(), Component.text(
                                    permission.getDescription(),
                                    NamedTextColor.GRAY
                                )));
                            }

                            return permissions;
                        })
                        .build()
                    )
                    .argument(CommandArgument.argument(OfflinePlayerArgument.class, "target"))
                    .handler(context -> {
                        final String permission = context.arguments().argumentOr(String.class, "permission", () -> {
                            context.source().sendRichMessage("<red>Incorrect Usage");
                            return null;
                        });

                        final Player target = (Player) context.arguments().argumentOr("target", (Supplier<Object>) () -> context.source().asPlayer());

                        context.source().sendRichMessage("Removed permission " + permission + " from player " + target.getName());
                    })
                )
            );


        manager.register(node, broadcastCommand);
    }
}
