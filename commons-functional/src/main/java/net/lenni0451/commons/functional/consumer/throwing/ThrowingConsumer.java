package net.lenni0451.commons.functional.consumer.throwing;

@FunctionalInterface
public interface ThrowingConsumer<O, T extends Throwable> {

    void accept(final O o) throws T;

    /**
     * Executes the other consumer after this one.<br>
     * {@code a.before(b);} <i>{@code //a is executed before b}</i>
     *
     * @param other The other consumer to execute
     * @return The wrapper consumer
     */
    default ThrowingConsumer<O, T> before(final ThrowingConsumer<O, T> other) {
        return (o) -> {
            this.accept(o);
            other.accept(o);
        };
    }

    /**
     * Executes the other consumer before this one.<br>
     * {@code a.after(b);} <i>{@code //b is executed before a}</i>
     *
     * @param other The other consumer to execute
     * @return The wrapper consumer
     */
    default ThrowingConsumer<O, T> after(final ThrowingConsumer<O, T> other) {
        return (o) -> {
            other.accept(o);
            this.accept(o);
        };
    }

}
