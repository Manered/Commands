package dev.manere.commands.api;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent;
import dev.manere.commands.CommandNode;
import dev.manere.commands.argument.CommandArgument;
import dev.manere.commands.ctx.CommandContext;
import dev.manere.commands.ctx.CommandSource;
import dev.manere.commands.exception.ArgumentParseException;
import dev.manere.commands.exception.IgnorableCommandException;
import dev.manere.commands.handler.*;
import dev.manere.commands.info.CommandData;
import dev.manere.commands.info.CommandInfo;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

@ApiStatus.Internal
public class CommandRegistrar implements Listener {
    @ApiStatus.Internal
    public static void register(final @NotNull CommandData data) {
        final JavaPlugin plugin = data.api().plugin();
        final CommandNode root = data.command();

        final CommandMap commandMap = Bukkit.getCommandMap();

        commandMap.register(
            root.literal(),
            plugin.getName().toLowerCase(Locale.ROOT).replaceAll(" ", "_"),
            command(root)
        );

        data.registered(true);
    }

    @NotNull
    @ApiStatus.Internal
    public static Command command(final @NotNull CommandNode root) {
        final Command command = new Command(root.literal()) {
            @NotNull
            @Override
            public List<String> tabComplete(final @NotNull CommandSender sender, final @NotNull String alias, final @NotNull String[] args) throws IllegalArgumentException {
                final List<String> completions = new ArrayList<>();
                final CommandNode node = node(root, new ArrayList<>(Arrays.asList(args)));

                final CommandContext context = new CommandContext(CommandSource.source(sender), node, node.literal(), alias, new ArrayList<>(Arrays.asList(args)));

                final String permission = root.info().permission();
                if (permission != null && !context.source().sender().hasPermission(permission)) {
                    final Component permissionMessage = root.info().permissionMessage();
                    if (permissionMessage != null) context.source().sendMessage(permissionMessage);
                    return completions;
                }

                // Handle subcommands
                final List<CommandNode> subcommands = node.subcommands();
                if (!subcommands.isEmpty() && args.length > 0) {
                    final String lastArg = args[args.length - 1];

                    for (final CommandNode subcommand : subcommands) if (subcommand.literal().startsWith(lastArg)) completions.add(subcommand.literal());
                    if (!completions.isEmpty()) return completions;
                }

                // Handle arguments
                CommandArgument<?> currentArgument = null;

                if (!context.command().arguments().isEmpty() && args.length > context.command().argumentOffset()) {
                    int index = (args.length - 1) - context.command().argumentOffset();
                    if (index >= 0 && index < context.command().arguments().size()) {
                        currentArgument = context.command().arguments().get(index);
                    }
                }

                if (currentArgument != null) {
                    SuggestionHandler<?> handler = null;

                    if (currentArgument.argument().get() instanceof SuggestionHandler<?> s) {
                        handler = s;
                    }

                    if (handler instanceof SyncSuggestionHandler syncHandler) {
                        final List<Suggestion> suggestions = currentArgument.suggestions() instanceof SyncSuggestionHandler
                            ? ((SyncSuggestionHandler) currentArgument.suggestions()).suggestions(context)
                            : syncHandler.suggestions(context);

                        final String lastArg = args.length > 0 ? args[args.length - 1] : "";
                        for (final Suggestion suggestion : suggestions) {
                            if (!suggestion.sticky() && suggestion.suggestion().toLowerCase().startsWith(lastArg.toLowerCase())) {
                                completions.add(suggestion.suggestion());
                                continue;
                            }

                            if (suggestion.sticky()) {
                                completions.add(suggestion.suggestion());
                            }
                        }
                    }
                }

                return completions;
            }

            @Override
            public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
                handle(root, CommandSource.source(sender), label, new ArrayList<>(Arrays.asList(args)));
                return true;
            }
        };

        final CommandInfo info = root.info();

        command.setAliases(new ArrayList<>(info.aliases()));
        command.setPermission(info.permission());

        final String description = info.description();
        if (description != null) command.setDescription(description);

        return command;
    }

    @ApiStatus.Internal
    private static void handle(final @NotNull CommandNode root, final @NotNull CommandSource source, final @NotNull String command, final @NotNull List<String> args) {
        final CommandNode node = node(root, args);
        final CommandContext context = new CommandContext(source, node, node.literal(), command, args);

        for (final CommandRequirement requirement : node.requirements()) if (requirement.require(context).equals(RequirementResult.failed())) return;

        final String permission = root.info().permission();
        if (permission != null && !context.source().sender().hasPermission(permission)) {
            final Component permissionMessage = root.info().permissionMessage();
            if (permissionMessage != null) context.source().sendMessage(permissionMessage);
            context.source().sendMessage(Bukkit.permissionMessage());
            return;
        }

        try {
            node.execution().run(context);
        } catch (final IgnorableCommandException ignored) {
            // Do nothing. An IgnorableCommandException is ignorable for a reason.
        } catch (final ArgumentParseException e) {
            throw ArgumentParseException.exception(e.context(), e.text());
        }
    }

    @NotNull
    @ApiStatus.Internal
    private static CommandNode node(final @NotNull CommandNode node, final @NotNull List<String> args) {
        CommandNode currentNode = node;
        CommandNode lastValidNode = node;

        for (final String arg : args) {
            boolean found = false;

            for (CommandNode subcommand : currentNode.subcommands())
                if (subcommand.literal().equals(arg)) {
                    lastValidNode = subcommand;
                    currentNode = subcommand;
                    found = true;
                    break;
                }

            if (!found) break;
        }

        return lastValidNode;
    }

    @NotNull
    @ApiStatus.Internal
    public AsyncTabCompleteEvent.Completion convert(final @NotNull Suggestion suggestion) {
        return AsyncTabCompleteEvent.Completion.completion(suggestion.suggestion(), suggestion.tooltip());
    }

    @EventHandler
    @ApiStatus.Internal
    private void handle(final @NotNull AsyncTabCompleteEvent event) {
        final CommandSource source = CommandSource.source(event.getSender());
        final String buffer = event.getBuffer();

        if (!event.isCommand() || !buffer.startsWith("/") || buffer.indexOf(' ') == -1) return;

        String[] rawArgs = Pattern.compile(" ").split(buffer, -1);
        rawArgs = rawArgs.length > 1 ? Arrays.copyOfRange(rawArgs, 1, rawArgs.length) : new String[]{""};

        String alias = rawArgs[0];
        if (alias.startsWith("/")) alias = alias.substring(1);

        final CommandNode root = APIHolder.api().manager().find(buffer.split(" ")[0].replaceFirst("/", ""));
        if (root == null) return;

        final List<String> args = new ArrayList<>(Arrays.asList(rawArgs));

        final List<AsyncTabCompleteEvent.Completion> completions = new ArrayList<>();
        final CommandNode node = node(root, args);
        final CommandContext context = new CommandContext(source, node, node.literal(), alias, args);

        CommandArgument<?> currentArgument = null;

        if (!context.command().arguments().isEmpty() && args.size() > context.command().argumentOffset()) {
            int index = (args.size() - 1) - context.command().argumentOffset();
            if (index >= 0 && index < context.command().arguments().size()) currentArgument = context.command().arguments().get(index);
        }

        if (currentArgument != null) {
            SuggestionHandler<?> handler = null;

            final Object object = currentArgument.argument().get();

            if (object instanceof SuggestionHandler<?> s) handler = s;
            if (handler == null) handler = currentArgument.suggestions();

            if (handler instanceof AsyncSuggestionHandler asyncHandler) {
                final CompletableFuture<List<Suggestion>> suggestions = currentArgument.suggestions() instanceof AsyncSuggestionHandler
                    ? ((AsyncSuggestionHandler) currentArgument.suggestions()).suggestions(context)
                    : asyncHandler.suggestions(context);

                final String lastArg = !args.isEmpty() ? args.getLast() : "";

                final List<AsyncTabCompleteEvent.Completion> newCompletions = new ArrayList<>();

                final List<Suggestion> suggestionsJoin = new ArrayList<>(suggestions.join());

                for (final Suggestion suggestion : suggestionsJoin) {
                    if (!suggestion.sticky() && suggestion.suggestion().toLowerCase().startsWith(lastArg.toLowerCase())) {
                        newCompletions.add(convert(suggestion));
                        continue;
                    }

                    if (suggestion.sticky()) {
                        newCompletions.add(convert(suggestion));
                    }
                }

                completions.addAll(newCompletions);
                event.completions(completions);
            }
        }
    }
}
