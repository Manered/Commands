# Commands
Slight inspiration from CommandAPI by Jorel

---

https://jitpack.io/#Manered/Commands/v1.1.0

Example:

```java
public class TestPlugin extends JavaPlugin {
    private static CommandAPI API = null;

    @Override
    public void onEnable() {
        API = CommandAPI.init(this);

        API.register(new CommandNode("broadcast")
            .permission("broadcast.use")
            .aliases("bc", "announce")
            .executes((ctx) -> {
                ctx.getSource().sendRichMessage("<red>Usage:");
                ctx.getSource().sendRichMessage("<red>/broadcast world <world> <text>");
                ctx.getSource().sendRichMessage("<red>/broadcast everyone <text>");
            })
            .subcommand(new CommandNode("world")
                .permission("broadcast.world")
                .filter(sender -> sender instanceof Player)
                .argument(CommandArgument.required(
                    TextArgument::new, "text",
                    CompletionProvider.sync(context -> List.of(new Completion("Hello, World!", Component.text("Test")))))
                )
                .executes(Player.class, (player, ctx) -> {
                    final Component text = MiniMessage.miniMessage().deserialize(
                        "<gold><bold>BROADCAST</bold></gold> " + ctx.getArgument(String.class, "text").orElse("Error")
                    );

                    player.getWorld().sendMessage(text);
                    player.getWorld().sendActionBar(text);
                })
            )
            .subcommand(new CommandNode("everyone")
                .permission("broadcast.everyone")
                .argument(CommandArgument.required(
                    TextArgument::new, "text",
                    CompletionProvider.sync(context -> List.of(new Completion("Hello, World!", Component.text("Test")))))
                )
                .executes(Player.class, (player, ctx) -> {
                    final Component text = MiniMessage.miniMessage().deserialize(
                        "<gold><bold>BROADCAST</bold></gold> " + ctx.getArgument(String.class, "text").orElse("Error")
                    );

                    player.getServer().sendMessage(text);
                    player.getServer().sendActionBar(text);
                })
            )
        );
    }

    @NotNull
    public static CommandAPI getCommandAPI() {
        return API;
    }
}
```
