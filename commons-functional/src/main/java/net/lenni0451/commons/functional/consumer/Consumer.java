package net.lenni0451.commons.functional.consumer;

@FunctionalInterface
public interface Consumer<O> {

    void accept(final O o);

    /**
     * Executes the other consumer after this one.<br>
     * {@code a.before(b);} <i>{@code //a is executed before b}</i>
     *
     * @param other The other consumer to execute
     * @return The wrapper consumer
     */
    default Consumer<O> before(final Consumer<O> other) {
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
    default Consumer<O> after(final Consumer<O> other) {
        return (o) -> {
            other.accept(o);
            this.accept(o);
        };
    }

}
