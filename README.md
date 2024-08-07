# Commands
Brigadier ripoff but technically done with brigadier (this uses Bukkit's command system and then Bukkit internally uses Brigadier) [Commands -> Bukkit -> Brigadier]

Slight inspiration from CommandAPI by Jorel

---

https://jitpack.io/#Manered/Commands/v1.0.0

Example:

```java
public class TestPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        final CommandsAPI api = CommandsAPI.api(() -> this);
        final CommandManager manager = api.manager();

        final CommandNode command = CommandNode.builder()
            .literal("broadcast")
            .info(info -> info
                .aliases("bc")
                .description("Broadcast Command")
                .permission("broadcast.use", Component.text(
                    "Insufficient permissions.", NamedTextColor.RED
                ))
            )
            .build()
            .handler(context -> {
                context.source().sendRichMessage("<red>Usage:");
                context.source().sendRichMessage("<red>/<command> global <minimessage text>"
                    .replaceAll("<command>", context.rootAlias())
                );
                context.source().sendRichMessage("<red>/<command> world <world> <minimessage text>"
                    .replaceAll("<command>", context.rootAlias())
                );
            })
            .subcommand(CommandNode.literal("global")
                .argument(CommandArgument.argument(GreedyTextArgument.class, "text", (SyncSuggestionHandler) context -> List.of(
                    Suggestion.suggestion("Restarting in 1 minute..."),
                    Suggestion.suggestion("Restarting in 30 seconds..."),
                    Suggestion.suggestion("Restarting in 15 seconds..."),
                    Suggestion.suggestion("Restarting in 10 seconds..."),
                    Suggestion.suggestion("Restarting in 5 seconds...")
                    Suggestion.suggestion("Restarting in 3 seconds..."),
                    Suggestion.suggestion("Restarting in 2 seconds..."),
                    Suggestion.suggestion("Restarting in 1 second..."),
                    Suggestion.suggestion("Restarting...")
                )))
                .handler(context -> {
                    final Component text = context.arguments().argumentOr(Component.class, "text", () -> {
                        context.source().sendRichMessage("<red>Usage: /<command> global <minimessage text>"
                            .replaceAll("<command>", context.rootAlias())
                        );
                        return null;
                    });

                    Bukkit.broadcast(Component.text()
                        .append(Component.text("[", NamedTextColor.GRAY))
                        .append(Component.text("Broadcast", NamedTextColor.DARK_RED))
                        .append(Component.text("]", NamedTextColor.GRAY))
                        .append(Component.text(" ", NamedTextColor.WHITE))
                        .append(text)
                        .build()
                    );
                })
            )
            .subcommand(CommandNode.literal("world")
                .argument(CommandArgument.argument(WorldArgument.class, "world"))
                .argument(CommandArgument.argument(GreedyTextArgument.class, "text", (SyncSuggestionHandler) context -> List.of(
                    Suggestion.suggestion("Restarting in 1 minute..."),
                    Suggestion.suggestion("Restarting in 30 seconds..."),
                    Suggestion.suggestion("Restarting in 15 seconds..."),
                    Suggestion.suggestion("Restarting in 10 seconds..."),
                    Suggestion.suggestion("Restarting in 5 seconds...")
                    Suggestion.suggestion("Restarting in 3 seconds..."),
                    Suggestion.suggestion("Restarting in 2 seconds..."),
                    Suggestion.suggestion("Restarting in 1 second..."),
                    Suggestion.suggestion("Restarting...")
                )))
                .handler(context -> {
                    final World world = context.arguments().argumentOr(World.class, "world", () -> Objects.requireNonNull(context.source().asPlayer()).getWorld());

                    final Component text = context.arguments().argumentOr(Component.class, "text", () -> {
                        context.source().sendRichMessage("<red>Usage: /<command> world <world> <minimessage text>"
                            .replaceAll("<command>", context.rootAlias())
                        );
                        return null;
                    });

                    world.sendMessage(Component.text()
                        .append(Component.text("[", NamedTextColor.GRAY))
                        .append(Component.text("Broadcast", NamedTextColor.DARK_RED))
                        .append(Component.text("]", NamedTextColor.GRAY))
                        .append(Component.text(" "))
                        .append(text)
                        .build()
                    );
                })
            );

        manager.register(command);
    }
}
```
