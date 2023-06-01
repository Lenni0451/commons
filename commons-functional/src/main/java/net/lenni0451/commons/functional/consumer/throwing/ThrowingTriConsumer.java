package net.lenni0451.commons.functional.consumer.throwing;

@FunctionalInterface
public interface ThrowingTriConsumer<A, B, C, T extends Throwable> {

    void accept(final A a, final B b, final C c) throws T;

    /**
     * Executes the other consumer after this one.<br>
     * {@code a.before(b);} <i>{@code //a is executed before b}</i>
     *
     * @param other The other consumer to execute
     * @return The wrapper consumer
     */
    default ThrowingTriConsumer<A, B, C, T> before(final ThrowingTriConsumer<A, B, C, T> other) {
        return (a, b, c) -> {
            this.accept(a, b, c);
            other.accept(a, b, c);
        };
    }

    /**
     * Executes the other consumer before this one.<br>
     * {@code a.after(b);} <i>{@code //b is executed before a}</i>
     *
     * @param other The other consumer to execute
     * @return The wrapper consumer
     */
    default ThrowingTriConsumer<A, B, C, T> after(final ThrowingTriConsumer<A, B, C, T> other) {
        return (a, b, c) -> {
            other.accept(a, b, c);
            this.accept(a, b, c);
        };
    }

}
