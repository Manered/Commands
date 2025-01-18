package dev.manere.commands.completion;

import dev.manere.commands.CommandContext;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface CompletionProvider<C> {
    @NotNull
    C completes(final @NotNull CommandContext<? extends CommandSender> context);

    @NotNull
    static AsyncCompletionProvider async(final @NotNull AsyncCompletionProvider lambda) {
        return lambda;
    }

    @NotNull
    static SyncCompletionProvider sync(final @NotNull SyncCompletionProvider lambda) {
        return lambda;
    }

    @NotNull
    static AsyncCompletionProvider async(final @NotNull Function<CommandContext<? extends CommandSender>, Collection<Completion>> function) {
        return context -> CompletableFuture.supplyAsync(() -> function.apply(context));
    }
}