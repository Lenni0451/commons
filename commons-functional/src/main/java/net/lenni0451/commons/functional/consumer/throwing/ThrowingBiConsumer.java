package net.lenni0451.commons.functional.consumer.throwing;

@FunctionalInterface
public interface ThrowingBiConsumer<A, B, T extends Throwable> {

    void accept(final A a, final B b) throws T;

    /**
     * Executes the other consumer after this one.<br>
     * {@code a.before(b);} <i>{@code //a is executed before b}</i>
     *
     * @param other The other consumer to execute
     * @return The wrapper consumer
     */
    default ThrowingBiConsumer<A, B, T> before(final ThrowingBiConsumer<A, B, T> other) {
        return (a, b) -> {
            this.accept(a, b);
            other.accept(a, b);
        };
    }

    /**
     * Executes the other consumer before this one.<br>
     * {@code a.after(b);} <i>{@code //b is executed before a}</i>
     *
     * @param other The other consumer to execute
     * @return The wrapper consumer
     */
    default ThrowingBiConsumer<A, B, T> after(final ThrowingBiConsumer<A, B, T> other) {
        return (a, b) -> {
            other.accept(a, b);
            this.accept(a, b);
        };
    }

}
