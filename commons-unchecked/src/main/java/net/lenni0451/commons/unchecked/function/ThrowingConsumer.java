package net.lenni0451.commons.unchecked.function;

import lombok.SneakyThrows;

import java.util.function.Consumer;

/**
 * A consumer that can throw checked exceptions.
 *
 * @param <T> The input type
 */
@FunctionalInterface
public interface ThrowingConsumer<T> {

    /**
     * Convert a ThrowingConsumer to a standard Consumer.
     *
     * @param consumer The ThrowingConsumer
     * @param <T>      The input type
     * @return The standard Consumer
     */
    static <T> Consumer<T> toConsumer(final ThrowingConsumer<T> consumer) {
        return consumer.toConsumer();
    }


    /**
     * Accept the input.
     *
     * @param t The input
     * @throws Throwable If an error occurs
     */
    void accept(final T t) throws Throwable;

    /**
     * Accept the input and sneakily throw any exceptions.
     *
     * @param t The input
     */
    @SneakyThrows
    default void tryAccept(final T t) {
        this.accept(t);
    }

    /**
     * Convert this ThrowingConsumer to a standard Consumer.
     *
     * @return The standard Consumer
     */
    default Consumer<T> toConsumer() {
        return this::tryAccept;
    }

}
