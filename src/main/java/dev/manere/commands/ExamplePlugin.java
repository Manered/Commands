package dev.manere.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import dev.manere.commands.api.CommandAPI;
import dev.manere.commands.api.CommandAPIOptions;
import dev.manere.commands.argument.CommandArgument;
import dev.manere.commands.argument.impl.paper.PlayerArgument;
import dev.manere.commands.argument.impl.vanilla.StringArgument;
import dev.manere.commands.completion.Completion;
import dev.manere.commands.completion.CompletionProvider;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

public class ExamplePlugin extends JavaPlugin {
    @Override
    public void onLoad() {

        final CommandAPI api = CommandAPI.register(this, config -> config.set(CommandAPIOptions.SILENT_LOGS, false));

        api.register(new CommandNode("duel")
            .aliases("1v1")
            .description("Fight a selected player")
            .permission("example.commands.duel")

            // Subcommand /duel accept [<player>]

            .subcommand(new CommandNode("accept")
                .argument(CommandArgument.optional("sender", PlayerArgument::new))
                .executesPlayer((target, ctx) -> {
                    final Optional<Player> optionalSender = ctx.getOptionalArgument("sender", Player.class);

                    optionalSender.ifPresentOrElse(
                        sender -> target.sendRichMessage("Sender: " + sender),
                        () -> target.sendRichMessage("Sender: None")
                    );
                })
            )

            // Root /duel

            .argument(CommandArgument.required("target", PlayerArgument::new))
            .argument(CommandArgument.optional("biome", StringArgument::new)
                .completions(CompletionProvider.sync(
                    new Completion("Plains"),
                    new Completion("Mushroom"),
                    new Completion("Desert"),
                    new Completion("Badlands"),
                    new Completion("Snow")
                ))
            )
            .executesPlayer((player, ctx) -> {
                final Player target = ctx.getRequiredArgument("target", Player.class);
                final Optional<String> optionalBiome = ctx.getOptionalArgument("biome", String.class);

                player.sendRichMessage("Target: " + target.getName());

                optionalBiome.ifPresentOrElse(
                    biome -> player.sendRichMessage("Biome: " + biome),
                    () -> player.sendRichMessage("Biome: None")
                );
            })
        );
    }
}
