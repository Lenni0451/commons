package net.lenni0451.commons.brigadier;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ParsedArgument;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.CommandNode;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class LineArgumentBuilder<S> extends ArgumentBuilder<S, LineArgumentBuilder<S>> {

    private static final Field ARGUMENTS_FIELD = ((Supplier<Field>) () -> {
        try {
            Field f = CommandContext.class.getDeclaredField("arguments");
            f.setAccessible(true);
            return f;
        } catch (Throwable t) {
            throw new IllegalStateException("Could not find the arguments field in CommandContext", t);
        }
    }).get();

    /**
     * Create a new line argument builder.<br>
     * Since no parent is provided, the first node <b>can not</b> be optional.<br>
     * Use {@link #create(ArgumentBuilder)} to create a line with a parent.
     *
     * @param <S> The sender type
     * @return The created line argument builder
     */
    public static <S> LineArgumentBuilder<S> create() {
        return new LineArgumentBuilder<>();
    }

    /**
     * Create a new line argument builder with the given parent.<br>
     * This is only required if the first node should be optional.
     *
     * @param parent The parent of the line
     * @param <S>    The sender type
     * @return The parent argument builder
     */
    public static <S> LineArgumentBuilder<S> create(final ArgumentBuilder<S, ?> parent) {
        return new LineArgumentBuilder<>(parent);
    }


    private final ArgumentBuilder<S, ?> parent;
    private final List<LineNode<S>> nodes = new ArrayList<>();
    private Command<S> executor;

    private LineArgumentBuilder() {
        this(null);
    }

    private LineArgumentBuilder(@Nullable final ArgumentBuilder<S, ?> parent) {
        this.parent = parent;
    }

    /**
     * Add a new node to the line.
     *
     * @param node The node to add
     * @return The line argument builder
     */
    public LineArgumentBuilder<S> node(final ArgumentBuilder<S, ?> node) {
        this.checkMutable();
        this.nodes.add(new LineNode<>(null, node));
        return this;
    }

    /**
     * Add a new literal node to the line.
     *
     * @param name The name of the literal
     * @return The line argument builder
     */
    public LineArgumentBuilder<S> literal(final String name) {
        return this.node(LiteralArgumentBuilder.literal(name));
    }

    /**
     * Add a new argument node to the line.
     *
     * @param name The name of the argument
     * @param type The type of the argument
     * @return The line argument builder
     */
    public LineArgumentBuilder<S> argument(final String name, final ArgumentType<?> type) {
        this.checkMutable();
        this.nodes.add(new LineNode<>(name, RequiredArgumentBuilder.argument(name, type)));
        return this;
    }

    /**
     * Add a requirement to the most recent node.
     *
     * @param requirement The requirement
     * @return The line argument builder
     */
    public LineArgumentBuilder<S> require(final Predicate<S> requirement) {
        this.checkMutable();
        this.requires(ArgumentBuilder.class).requires(requirement);
        return this;
    }

    /**
     * Add a suggestion provider to the most recent node.
     *
     * @param provider The suggestion provider
     * @return The line argument builder
     */
    public LineArgumentBuilder<S> suggest(final SuggestionProvider<S> provider) {
        this.checkMutable();
        this.requires(RequiredArgumentBuilder.class).suggests(provider);
        return this;
    }

    /**
     * Make the most recent <b>literal</b> node optional.
     *
     * @return The line argument builder
     */
    public LineArgumentBuilder<S> defaultValue() {
        this.checkMutable();
        this.requires(LiteralArgumentBuilder.class);
        if (this.nodes.size() == 1 && this.parent == null) throw new IllegalStateException("A parent node must be set to use a default value on the first node");
        this.getLastNode().defaultValue = "";
        return this;
    }

    /**
     * Make the most recent <b>argument</b> node optional with the given default value.
     *
     * @param defaultValue The default value
     * @return The line argument builder
     */
    public LineArgumentBuilder<S> defaultValue(final Object defaultValue) {
        this.checkMutable();
        this.requires(RequiredArgumentBuilder.class);
        if (this.nodes.size() == 1 && this.parent == null) throw new IllegalStateException("A parent node must be set to use a default value on the first node");
        this.getLastNode().defaultValue = defaultValue;
        return this;
    }

    /**
     * Set the executor for the line and finalize it.<br>
     * The line can no longer be modified after this method has been called.
     *
     * @param executor The executor
     * @param <MS>     The sender type
     * @param <T>      The type of the parent
     * @return The line argument builder
     */
    public <MS, T extends ArgumentBuilder<MS, T>> ArgumentBuilder<MS, T> execute(final Consumer<CommandContext<S>> executor) {
        return this.execute(context -> {
            executor.accept(context);
            return 1;
        });
    }

    /**
     * Set the executor for the line and finalize it.<br>
     * The line can no longer be modified after this method has been called.
     *
     * @param executor The executor
     * @param <MS>     The sender type
     * @param <T>      The type of the parent
     * @return The line argument builder
     */
    public <MS, T extends ArgumentBuilder<MS, T>> ArgumentBuilder<MS, T> execute(final Command<S> executor) {
        this.checkMutable();
        if (this.nodes.isEmpty()) throw new IllegalStateException("Cannot add an executor to a builder with no nodes");
        this.executor = executor;
        return (ArgumentBuilder<MS, T>) this;
    }

    @Override
    protected LineArgumentBuilder<S> getThis() {
        return this;
    }

    @Override
    public CommandNode<S> build() {
        if (this.nodes.isEmpty()) throw new IllegalStateException("Cannot build a builder with no nodes");

        CommandNode<S> root = null;
        CommandNode<S> current = null;
        for (int i = 0; i < this.nodes.size(); i++) {
            LineNode<S> node = this.nodes.get(i);
            LineNode<S> next = i + 1 < this.nodes.size() ? this.nodes.get(i + 1) : null;

            if (i == 0 && node.defaultValue != null) this.parent.executes(this.makeDefaultExecutor(0));
            if (next == null || next.defaultValue != null) {
                List<LineNode<S>> missing = this.nodes.subList(i + 1, this.nodes.size());
                node.node.executes(this.makeDefaultExecutor(i + 1));
            }
            CommandNode<S> newNode = node.node.build();
            if (root == null) root = newNode;
            if (current != null) current.addChild(newNode);
            current = newNode;
        }
        return root;
    }

    private Command<S> makeDefaultExecutor(final int start) {
        return ctx -> {
            Map<String, ParsedArgument<S, ?>> arguments;
            try {
                arguments = (Map<String, ParsedArgument<S, ?>>) ARGUMENTS_FIELD.get(ctx);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Could not get arguments from CommandContext", e);
            }
            List<LineNode<S>> missing = this.nodes.subList(start, this.nodes.size());
            for (LineNode<S> missingNode : missing) {
                if (missingNode.name != null && missingNode.defaultValue != null) {
                    arguments.put(missingNode.name, new ParsedArgument<>(-1, -1, missingNode.defaultValue));
                }
            }
            return this.executor.run(ctx);
        };
    }


    private void checkMutable() {
        if (this.executor != null) throw new IllegalStateException("Cannot modify a builder that already has an executor");
    }

    private LineNode<S> getLastNode() {
        if (this.nodes.isEmpty()) throw new IllegalStateException("No nodes added yet");
        return this.nodes.get(this.nodes.size() - 1);
    }

    private <T> T requires(final Class<T> type) {
        LineNode<S> node = this.getLastNode();
        if (!type.isAssignableFrom(node.node.getClass())) throw new IllegalStateException("Last node is not of type " + type.getSimpleName());
        return type.cast(node.node);
    }


    private static class LineNode<S> {
        @Nullable
        private final String name;
        private final ArgumentBuilder<S, ?> node;
        @Nullable
        private Object defaultValue;

        private LineNode(@Nullable final String name, final ArgumentBuilder<S, ?> node) {
            this.name = name;
            this.node = node;
        }
    }

}
