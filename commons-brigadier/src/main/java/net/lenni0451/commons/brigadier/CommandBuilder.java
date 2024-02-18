package net.lenni0451.commons.brigadier;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;

import java.util.function.Consumer;

/**
 * A utility interface to make creating brigadier commands easier.
 *
 * @param <S> The sender type
 */
public interface CommandBuilder<S> {

    /**
     * Create a new literal argument builder with the given literal.
     *
     * @param literal The literal
     * @return The created literal argument builder
     */
    default LiteralArgumentBuilder<S> literal(final String literal) {
        return LiteralArgumentBuilder.literal(literal);
    }

    /**
     * Create a new required argument builder with the given name and type.
     *
     * @param name The name of the argument
     * @param type The argument type
     * @param <T>  The type of the argument
     * @return The created required argument builder
     */
    default <T> RequiredArgumentBuilder<S, T> argument(final String name, final ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }

    /**
     * Create a new line argument builder.<br>
     * Since no parent is provided, the first node <b>can not</b> be optional.<br>
     * Use {@link #line(ArgumentBuilder, Consumer)} to create a line with a parent.
     *
     * @return The created line argument builder
     */
    default LineArgumentBuilder<S> line() {
        return LineArgumentBuilder.create();
    }

    /**
     * Create a new line argument builder with the given parent.<br>
     * This is only required if the first node should be optional.
     *
     * @param parent   The parent of the line
     * @param consumer The consumer to configure the line
     * @param <B>      The type of the parent
     * @return The parent argument builder
     */
    default <B extends ArgumentBuilder<S, ?>> B line(final B parent, final Consumer<LineArgumentBuilder<S>> consumer) {
        LineArgumentBuilder<S> line = LineArgumentBuilder.create(parent);
        consumer.accept(line);
        parent.then(line);
        return parent;
    }

}
