package net.lenni0451.commons.brigadier;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;

import java.lang.reflect.Field;
import java.util.*;

public class HelpAppender {

    private static final Field COMMAND_FIELD;

    static {
        try {
            COMMAND_FIELD = CommandNode.class.getDeclaredField("command");
            COMMAND_FIELD.setAccessible(true);
        } catch (Throwable t) {
            throw new IllegalStateException("Unable to find 'command' field in CommandNode class", t);
        }
    }

    public static <S> CommandDispatcher<S> newAutoHelpAppendingCommandDispatcher(final HelpSender<S> helpSender) {
        return new CommandDispatcher<S>() {
            @Override
            public LiteralCommandNode<S> register(LiteralArgumentBuilder<S> command) {
                LiteralCommandNode<S> node = super.register(command);
                appendHelpExecutor(helpSender, this, node);
                return node;
            }
        };
    }

    public static <S> void appendHelpExecutor(final HelpSender<S> helpSender, final CommandDispatcher<S> dispatcher, final CommandNode<S> commandNode) {
        Set<CommandNode<S>> visited = Collections.newSetFromMap(new WeakHashMap<>());
        Stack<CommandNode<S>> toVisit = new Stack<>();
        toVisit.push(commandNode);
        while (!toVisit.isEmpty()) {
            CommandNode<S> node = toVisit.pop();
            if (visited.contains(node)) continue;
            visited.add(node);

            if (node.getCommand() == null) {
                Command<S> command = ctx -> {
                    if (node.getRedirect() != null) {
                        printHelp(helpSender, dispatcher, node.getRedirect(), ctx.getSource(), node.getName(), ctx.getInput());
                    } else {
                        printHelp(helpSender, dispatcher, node, ctx.getSource(), node.getName(), ctx.getInput());
                    }
                    return 1;
                };
                try {
                    COMMAND_FIELD.set(node, command);
                } catch (Throwable t) {
                    throw new IllegalStateException("Unable to set command field in CommandNode", t);
                }
            }
            node.getChildren().forEach(toVisit::push);
        }
    }

    public static <S> void printHelp(final HelpSender<S> helpSender, final CommandDispatcher<S> dispatcher, final CommandNode<S> commandNode, final S sender, final String name, final String prefix) {
        List<String> usages = new ArrayList<>();
        Map<CommandNode<S>, String> smartUsage = dispatcher.getSmartUsage(commandNode, sender);
        for (Map.Entry<CommandNode<S>, String> entry : smartUsage.entrySet()) {
            CommandNode<S> node = entry.getKey();
            String usage = entry.getValue();
            if (!node.canUse(sender)) continue;
            if (node.getRedirect() != null) {
                List<String> subChildren = new ArrayList<>();
                for (CommandNode<S> subChild : node.getRedirect().getChildren()) {
                    if (!subChild.canUse(sender)) continue;
                    subChildren.add(subChild.getName());
                }
                usage = node.getName() + " (" + String.join("|", subChildren) + ") ";
            }
            usages.add((prefix.isEmpty() ? "" : (prefix + " ")) + usage);
        }
        helpSender.send(sender, name, usages);
    }


    @FunctionalInterface
    public interface HelpSender<S> {
        void send(final S sender, final String name, final List<String> usages);
    }

}
