package net.lenni0451.commons.functional.functions;

@FunctionalInterface
public interface Function<O, R> {

    R apply(final O o);

    /**
     * Executes the other function after this one.<br>
     * {@code a.before(b);} <i>{@code //a is executed before b}</i>
     *
     * @param other The other function to execute
     * @param <X>   The return type of the other function
     * @return The wrapper function
     */
    default <X> Function<O, X> before(final Function<? super R, ? extends X> other) {
        return (o) -> other.apply(this.apply(o));
    }

    /**
     * Executes the other function before this one.<br>
     * {@code a.after(b);} <i>{@code //b is executed before a}</i>
     *
     * @param other The other function to execute
     * @param <X>   The argument type of the other function
     * @return The wrapper function
     */
    default <X> Function<X, R> after(final Function<? super X, ? extends O> other) {
        return (o) -> this.apply(other.apply(o));
    }

    /**
     * A function that always returns the argument value.
     *
     * @param <T> The type of the argument
     * @return The identity function
     */
    default <T> Function<T, T> identity() {
        return (t) -> t;
    }

}
