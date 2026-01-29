package dev.manere.commands.api;

import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class CommandTreeDebugger {
    public static void printTree(final com.mojang.brigadier.tree.CommandNode<?> node, final Consumer<String> logger) {
        logger.accept("┌─ Brigadier Command Tree");
        printNode(node, "", true, logger);
    }

    private static void printNode(final com.mojang.brigadier.tree.CommandNode<?> node, final String prefix, final boolean isLast, final Consumer<String> logger) {
        final String connector = isLast ? "└─ " : "├─ ";
        final String nextPrefix = prefix + (isLast ? "   " : "│  ");

        final String name = node instanceof LiteralCommandNode<?> literal ? literal.getLiteral() : "<" + node.getName() + ">";

        final boolean executes = node.getCommand() != null;

        logger.accept(prefix + connector + name + (executes ? " ✔" : ""));

        final List<CommandNode<?>> children = new ArrayList<>(node.getChildren());

        for (int i = 0; i < children.size(); i++) {
            printNode(children.get(i), nextPrefix, i == children.size() - 1, logger);
        }
    }

}
