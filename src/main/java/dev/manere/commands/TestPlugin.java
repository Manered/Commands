package dev.manere.commands;

import dev.manere.commands.api.CommandManager;
import dev.manere.commands.api.CommandsAPI;
import dev.manere.commands.argument.CommandArgument;
import dev.manere.commands.argument.impl.IntegerArgument;
import dev.manere.commands.argument.impl.OfflinePlayerArgument;
import dev.manere.commands.argument.impl.StringArgument;
import dev.manere.commands.ctx.CommandArguments;
import dev.manere.commands.ctx.CommandSource;
import dev.manere.commands.handler.Suggestion;
import dev.manere.commands.info.CommandInfo;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TestPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        super.onEnable();

        final CommandsAPI api = CommandsAPI.api(this);
        final CommandManager manager = api.manager();

        final CommandNode testCommand = CommandNode.builder()
            .literal("test")
            .info(info -> {})
            .build()
            .argument(CommandArgument.argument(StringArgument.class, "str"))
            .argument(CommandArgument.argument(StringArgument.class, "test"))
            .handler(context -> {
                final CommandSource source = context.source();
                final CommandArguments arguments = context.arguments();

                source.sendRichMessage("Test!");

                final String argument = arguments.argumentOr(String.class, "str", () -> {
                    source.sendRichMessage("bad usage");
                    return null;
                });

                final String test = arguments.argumentOr(String.class, "test", () -> {
                    source.sendRichMessage("test bad");
                    return "hello";
                });

                source.sendRichMessage("ARGUMENTS");
                source.sendRichMessage("1 (R): " + argument);
                source.sendRichMessage("2 (O): " + test);
            });

        final CommandNode command = CommandNode.builder()
            .literal("economy")
            .info(() -> CommandInfo.info()
                .aliases("eco")
                .description("Economy command.")
                .permission("core.economy.command", Component.text(
                    "Insufficient permissions.",
                    NamedTextColor.RED
                ))
            )
            .build()
            // /economy - Displays usage
            .handler(context -> {
                final CommandSource source = context.source();
                source.sendRichMessage("<red>Usage: /" + context.alias() + " <add | remove> <money> [player]");
            })
            .suggestAsync(context -> CompletableFuture.supplyAsync(
                () -> List.of(
                    Suggestion.suggestion(
                        "add",
                        Component.text("Adds money to a player's balance.", NamedTextColor.AQUA)
                    ),
                    Suggestion.suggestion(
                        "remove",
                        Component.text("Removes money from a player's balance.", NamedTextColor.AQUA)
                    )
                ))
            )
            .subcommand(CommandNode.literal("add")
                // Subcommands can only have a literal (name of subcommand branch), no CommandInfo.
                // We don't use indexes. No messing around with argument positions and sizes.
                .argument(CommandArgument.argument(IntegerArgument.class, "money"))
                .argument(CommandArgument.argument(OfflinePlayerArgument.class, "player"))
                .handler(context -> {
                    final CommandSource source = context.source();
                    final CommandArguments arguments = context.arguments();

                    final int balance = arguments.argumentOr(Integer.class, "money", () -> {
                        // rootAlias() will return eco or economy, depending on what the player executed
                        // alias() will return the subcommand literal.
                        source.sendRichMessage("<red>Usage: /" + context.rootAlias() + " " + context.alias() + " <money> [player]");

                        // You can provide a default value here or null to stop execution
                        return null;
                    });

                    final OfflinePlayer target = arguments.argumentOr(OfflinePlayer.class, "player", () -> {
                        final Player player = source.asPlayer();

                        if (player == null || !source.isPlayer()) {
                            source.sendRichMessage("<red>Usage: /" + context.rootAlias() + " " + context.alias() + " <money> [player]");
                            return null;
                        }

                        source.sendRichMessage("Optional argument default is you");
                        return player;
                    });

                    // TODO: Add some money to target
                    source.sendRichMessage("Added " + balance + " to balance of " + target.getName());
                })
                .suggestAsync(context -> CompletableFuture.supplyAsync(() -> {
                    final CommandArguments arguments = context.arguments();

                    final CommandArgument<?> argument = arguments.current();
                    if (argument == null) return new ArrayList<>();

                    final List<Suggestion> suggestions = new ArrayList<>();

                    if (argument.name().equals("player")) {
                        for (final OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
                            offlinePlayer.getPlayerProfile().update().thenAcceptAsync(profile -> {
                                final String name = profile.getName();
                                if (name == null) return;

                                suggestions.add(Suggestion.suggestion(name));
                            });
                        }

                        Bukkit.getScheduler().runTask(this, () -> {
                            for (final Player player : Bukkit.getOnlinePlayers()) suggestions.add(Suggestion.suggestion(player.getName()));
                        });

                        return suggestions;
                    } else if (argument.name().equals("money")) {
                        return List.of(Suggestion.suggestion(
                            "<money>",
                            Component.text("money: Int", NamedTextColor.DARK_GRAY)
                        ));
                    }

                    return new ArrayList<>();
                }))
            )
            .subcommand(CommandNode.literal("remove")
                // Subcommands can only have a literal (name of subcommand branch), no CommandInfo.
                // We don't use indexes. No messing around with argument positions and sizes.
                .argument(CommandArgument.argument(IntegerArgument.class, "money"))
                .argument(CommandArgument.argument(OfflinePlayerArgument.class, "player"))
                .handler(context -> {
                    final CommandSource source = context.source();
                    final CommandArguments arguments = context.arguments();

                    final int balance = arguments.argumentOr(Integer.class, "money", () -> {
                        // rootAlias() will return eco or economy, depending on what the player executed
                        // alias() will return the subcommand literal.
                        source.sendRichMessage("<red>Usage: /" + context.rootAlias() + " " + context.alias() + " <money> [player]");

                        // You can provide a default value here or null to stop execution
                        return null;
                    });

                    final OfflinePlayer target = arguments.argumentOr(OfflinePlayer.class, "player", () -> {
                        final Player player = source.asPlayer();

                        if (player == null || !source.isPlayer()) {
                            source.sendRichMessage("<red>Usage: /" + context.rootAlias() + " " + context.alias() + " <money> [player]");
                            return null;
                        }

                        source.sendRichMessage("Optional argument default is you");
                        return player;
                    });

                    // TODO: Remove some money from target
                    source.sendRichMessage("Removed " + balance + "balance from " + target.getName());
                })
                .suggestAsync(context -> CompletableFuture.supplyAsync(() -> {
                    final CommandArguments arguments = context.arguments();

                    final CommandArgument<?> argument = arguments.current();
                    if (argument == null) return new ArrayList<>();

                    final List<Suggestion> suggestions = new ArrayList<>();

                    if (argument.name().equals("player")) {
                        for (final OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
                            offlinePlayer.getPlayerProfile().update().thenAcceptAsync(profile -> {
                                final String name = profile.getName();
                                if (name == null) return;

                                suggestions.add(Suggestion.suggestion(name));
                            });
                        }

                        Bukkit.getScheduler().runTask(this, () -> {
                            for (final Player player : Bukkit.getOnlinePlayers())
                                suggestions.add(Suggestion.suggestion(player.getName()));
                        });

                        return suggestions;
                    } else if (argument.name().equals("money")) {
                        return List.of(Suggestion.suggestion(
                            "<money>",
                            Component.text("money: Int", NamedTextColor.DARK_GRAY)
                        ));
                    }

                    return new ArrayList<>();
                }))
            );

        final CommandNode permissionTest = CommandNode.builder(b -> b.literal("permissions").info(info -> info.aliases("perms").permission("permissions.use")))
            .build()
            .handler(context -> context.source().sendRichMessage("usage: permissions suggestion <add|remove>"))
            .suggest(context -> new ArrayList<>(List.of(Suggestion.suggestion("suggestion"))))
            .subcommand(CommandNode.literal("suggestion")
                .handler(context -> context.source().sendRichMessage("usage: permissions suggestion <add|remove>"))
                .suggest(context -> new ArrayList<>(List.of(Suggestion.suggestion("add"), Suggestion.suggestion("remove"))))
                .subcommand(CommandNode.literal("add")
                    .handler(context -> context.source().sendRichMessage("added"))
                )
                .subcommand(CommandNode.literal("remove")
                    .handler(context -> context.source().sendRichMessage("removed"))
                )
            );


        final CommandNode penis = CommandNode.builder()
            // name
            .literal("penis")
            .info(() -> CommandInfo.info()
                .description("Penis...")
                .permission(
                    "penis.use",
                    Component.text("Toilet ananas nasdas", NamedTextColor.RED)
                )
                .aliases("penis2", "penis3")
            )
            .build()
            .handler(context -> context.source().sendRichMessage("<red>Usage: /penis <increase | decrease> <amount> [player]"))
            .suggest(context -> List.of(
                Suggestion.suggestion("increase", Component.text("Subcommand 1", NamedTextColor.AQUA)),
                Suggestion.suggestion("decrease", Component.text("Subcommand 2", NamedTextColor.AQUA))
            ))
            .subcommand(CommandNode.literal("increase")
                .argument(CommandArgument.argument(IntegerArgument.class, "amount"))
                .argument(CommandArgument.argument(OfflinePlayerArgument.class, "player"))
                .handler(context -> {
                    final int amount = context.arguments().argumentOr(Integer.class, "amount", () -> {
                        context.source().sendRichMessage("<red>Usage: /penis increase <amount> [player]");
                        return null;
                    });

                    final OfflinePlayer target = context.arguments().argumentOr(OfflinePlayer.class, "player", () -> context.source().asPlayer());

                    context.source().sendRichMessage("Increased " + target + "'s cock size by " + amount);
                })
                .suggest(context -> {
                    final CommandArgument<?> argument = context.arguments().current();

                    if (argument == null) return new ArrayList<>();

                    if (argument.name().equals("amount")) {
                        return List.of(Suggestion.suggestion("<amount>", Component.text("", NamedTextColor.AQUA)));
                    } else {
                        final List<Suggestion> suggestions = new ArrayList<>(List.of(Suggestion.suggestion("<player>")));

                        for (final Player player : Bukkit.getOnlinePlayers()) {
                            suggestions.add(Suggestion.suggestion(player.getName(), player.displayName()));
                        }

                        return suggestions;
                    }
                })
            )
            .subcommand(CommandNode.literal("decrease")
                .argument(CommandArgument.argument(IntegerArgument.class, "amount"))
                .argument(CommandArgument.argument(OfflinePlayerArgument.class, "player"))
                .handler(context -> {
                    final int amount = context.arguments().argumentOr(Integer.class, "amount", () -> {
                        context.source().sendRichMessage("<red>Usage: /penis decrease <amount> [player]");
                        return null;
                    });

                    final OfflinePlayer target = context.arguments().argumentOr(OfflinePlayer.class, "player", () -> context.source().asPlayer());

                    context.source().sendRichMessage("Decreased " + target + "'s cock size by " + amount);
                })
                .suggest(context -> {
                    final CommandArgument<?> argument = context.arguments().current();

                    if (argument == null) return new ArrayList<>();

                    if (argument.name().equals("amount")) {
                        return List.of(Suggestion.suggestion("<amount>", Component.text("", NamedTextColor.AQUA)));
                    } else {
                        final List<Suggestion> suggestions = new ArrayList<>(List.of(Suggestion.suggestion("<player>")));

                        for (final Player player : Bukkit.getOnlinePlayers()) {
                            suggestions.add(Suggestion.suggestion(player.getName(), player.displayName()));
                        }

                        return suggestions;
                    }
                })
            );

        manager.register(command, testCommand, permissionTest, penis);
    }
}
