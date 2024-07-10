package dev.manere.commands.api;

import dev.manere.commands.CommandNode;
import dev.manere.commands.argument.CommandArgument;
import dev.manere.commands.ctx.CommandArguments;
import dev.manere.commands.ctx.CommandContext;
import dev.manere.commands.ctx.CommandSource;
import dev.manere.commands.exception.ArgumentParseException;
import dev.manere.commands.exception.IgnorableCommandException;
import dev.manere.commands.handler.CommandRequirement;
import dev.manere.commands.handler.RequirementResult;
import dev.manere.commands.handler.Suggestion;
import dev.manere.commands.handler.SuggestionHandler;
import dev.manere.commands.info.CommandData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CommandRegistrar implements Listener {
    public static void register(final @NotNull CommandData data) {
        final JavaPlugin plugin = data.api().plugin();
        final CommandNode root = data.command();

        final CommandMap commandMap = Bukkit.getCommandMap();

        commandMap.register(
            root.literal(),
            plugin.getName().toLowerCase(Locale.ROOT).replaceAll(" ", "_"),
            command(root)
        );
    }

    @NotNull
    public static Command command(final @NotNull CommandNode root) {
        return new Command(root.literal()) {
            @NotNull
            @Override
            public List<String> tabComplete(final @NotNull CommandSender sender, final @NotNull String alias, final @NotNull String[] args) throws IllegalArgumentException {
                final CommandNode node = node(root, new ArrayList<>(Arrays.asList(args)));
                final CommandContext context = new CommandContext(CommandSource.source(sender), node, node.literal(), alias, new ArrayList<>(Arrays.asList(args)));

                /*
                final SuggestionHandler<?> unknown = node.suggestions();

                try {
                    final SuggestionHandler<List<Suggestion>> handler = (SuggestionHandler<List<Suggestion>>) unknown;
                    final List<Suggestion> suggestions = handler.suggestions(context);

                    final List<String> all = new ArrayList<>();

                    for (final Suggestion suggestion : suggestions) {
                        // if (suggestion.tooltip() != null) throw new UnsupportedOperationException("Only async suggestions can have tooltips! (" + node + ")");
                        all.add(suggestion.suggestion());
                    }

                    final List<String> matching = new ArrayList<>();

                    final CommandArguments arguments = context.arguments();
                    final CommandArgument<?> argument = arguments.current();

                    @Nullable
                    Integer currentArgument;
                    if (argument != null) currentArgument = arguments.position(argument);
                    else currentArgument = null;

                    for (final String suggestion : all) {
                        if (currentArgument != null) {
                            if (suggestion.toLowerCase().startsWith(node.argumentOffset() + args[currentArgument].toLowerCase())) {
                                matching.add(suggestion);
                            }
                        } else {
                            if (suggestion.toLowerCase().startsWith(node.argumentOffset() + args[Math.max(args.length - 1, 0)])) {
                                matching.add(suggestion);
                            }
                        }
                    }

                    return matching;
                } catch (final ClassCastException ignored) {}

                    */

                return new ArrayList<>();
            }

            @Override
            public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
                handle(root, CommandSource.source(sender), label, new ArrayList<>(Arrays.asList(args)));
                return true;
            }
        };
    }

    private static void handle(final @NotNull CommandNode root, final @NotNull CommandSource source, final @NotNull String command, final @NotNull List<String> args) {
        final CommandNode node = node(root, args);
        final CommandContext context = new CommandContext(source, node, node.literal(), command, args);

        for (final CommandRequirement requirement : node.requirements()) if (requirement.require(context).equals(RequirementResult.failed())) return;

        try {
            node.execution().run(context);
        } catch (final IgnorableCommandException ignored) {
            // Do nothing. An IgnorableCommandException is ignorable for a reason.
        } catch (final ArgumentParseException e) {
            throw ArgumentParseException.exception(e.context(), e.text());
        }
    }

    @NotNull
    private static CommandNode node(final @NotNull CommandNode node, final @NotNull List<String> args) {
        CommandNode currentNode = node;
        CommandNode lastValidNode = node;

        for (final String arg : args) {
            boolean found = false;

            for (CommandNode subcommand : currentNode.subcommands()) if (subcommand.literal().equals(arg)) {
                lastValidNode = subcommand;
                currentNode = subcommand;
                found = true;
                break;
            }

            if (!found) break;
        }

        return lastValidNode;
    }
}
