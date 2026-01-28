# Commands

A modern, flexible, and powerful command framework for Minecraft servers, built on top of Brigadier. Designed for Paper and its forks, Commands simplifies the creation of complex command structures with robust argument parsing, intelligent tab-completion, and fine-grained control over command execution.

## Features

*   **Brigadier Integration**: Leverages the power of Brigadier for efficient command parsing and execution.
*   **Declarative Command Definition**: Define commands with a fluent, easy-to-read API using `CommandNode` or `BasicCommandNode`.
*   **Simplified Registration**: A simple, centralized API for registering and unregistering commands.
*   **Rich Argument Types**: Support for various argument types, with easy extensibility for custom types.
*   **Intelligent Tab-Completion**: Provide dynamic and context-aware suggestions for arguments, including asynchronous completions.
*   **Flexible Command Structure**: Easily define subcommands, optional arguments, and command aliases.
*   **Permission & Filter System**: Control who can execute commands and arguments with built-in filtering capabilities.
*   **Configuration Options**: Fine-tune API behavior, such as logging verbosity.

## Installation

Commands is designed to be used as a library in your Paper (or compatible) plugin.

1.  **Add the dependency to your build script**:

    **Maven (`pom.xml`):**
    ```xml
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>

    <dependencies>
        <dependency>
            <groupId>com.github.Manered</groupId>
            <artifactId>Commands</artifactId>
            <version>2.0.0</version>
        </dependency>
    </dependencies>
    ```

    **Gradle (Kotlin DSL, `build.gradle.kts`):**
    ```kotlin
    repositories {
        maven("https://jitpack.io")
    }

    dependencies {
        implementation("com.github.Manered:Commands:2.0.0")
    }
    ```

2.  **Shade the library**: Ensure Commands is shaded into your plugin's final JAR to avoid classpath issues.

## Getting Started

### Initialization

To get started, initialize `CommandAPI` in your plugin's `onEnable` method. This will be your main entry point for registering commands and configuring the API.

```java
import dev.manere.commands.api.CommandAPI;
import dev.manere.commands.api.CommandAPIOptions;
import org.bukkit.plugin.java.JavaPlugin;

public class MyPlugin extends JavaPlugin {
    private CommandAPI commandAPI;

    @Override
    public void onEnable() {
        this.commandAPI = CommandAPI.register(this, config -> {
            config.set(CommandAPIOptions.SILENT_LOGS, true); // Verbose logging
        });

        // Now you can register your commands.
    }
    
    public CommandAPI getCommandAPI() {
        return commandAPI;
    }
}
```

### Configuration

You can configure `CommandAPI` during initialization.

*   `CommandAPIOptions.SILENT_LOGS`: A `Boolean` that controls whether the API logs command registrations and updates to the console. Defaults to `true` (silent). Set to `false` for debugging.

## Defining Commands

Commands offers two ways to define commands: `CommandNode` for complex, highly-customizable commands, and `BasicCommandNode` for simpler use cases.

### Using `CommandNode` (Advanced)

`CommandNode` provides a powerful, fluent builder for defining every aspect of your command.

#### Basic Command

```java
CommandNode helloCommand = CommandNode.node("hello")
    .executes(context -> {
        context.sendRichMessage("Hello, " + context.getSource().getName() + "!");
    });

helloCommand.register();
```

#### Command with Arguments

This example creates a `/msg <player> <message>` command.

```java
CommandNode messageCommand = CommandNode.node("msg")
    .aliases("message", "tell", "whisper")
    .argument(CommandArgument.requiredArgument("target", PlayerArgument::new))
    .argument(CommandArgument.optionalArgument("message", TextArgument::new))
    .executesPlayer((sender, context) -> {
        Player target = context.getRequiredArgument("target", Player.class);
        String message = context.getRequiredArgument("message", String.class);

        sender.sendRichMessage("You -> " + target.getName() + ": " + message);
        target.sendRichMessage(sender.getName() + " -> You: " + message);
    });
        
messageCommand.register();
```

### Using `BasicCommandNode` (Simple)

For commands that don't require complex logic or structure, `BasicCommandNode` offers a more concise way to define them.

```java
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


@Override
public void onEnable() {
    this.commandAPI.register(new HealCommand());
    
    // You can also do this:
    // new HealCommand().register();
}
```

## Command Lifecycle

### Registration

Once you have your `CommandNode` or `BasicCommandNode` instance, register it with `CommandAPI.register(...)`, `yourCommandNode.register(...)` or `yourBasicCommandNode.register(...)`.

### Unregistration

You can unregister commands at any time using their primary label. This will also remove any associated aliases.

```java
commandAPI.unregister("mycmd");
```

## Contributing

Contributions are welcome! Please feel free to open issues or pull requests on the GitHub repository.

## License

This project is licensed under the MIT License - see the `LICENSE` file for details.
