package net.lenni0451.commons.functional.functions.throwing;

@FunctionalInterface
public interface ThrowingFunction<O, R, T extends Throwable> {

    R apply(final O o);

    /**
     * Executes the other function after this one.<br>
     * {@code a.before(b);} <i>{@code //a is executed before b}</i>
     *
     * @param other The other function to execute
     * @param <X>   The return type of the other function
     * @return The wrapper function
     */
    default <X> ThrowingFunction<O, X, T> before(final ThrowingFunction<? super R, ? extends X, T> other) {
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
    default <X> ThrowingFunction<X, R, T> after(final ThrowingFunction<? super X, ? extends O, T> other) {
        return (o) -> this.apply(other.apply(o));
    }

    /**
     * A function that always returns the argument value.
     *
     * @param <X> The type of the argument
     * @param <E> The type of the exception
     * @return The identity function
     */
    default <X, E extends Throwable> ThrowingFunction<X, X, E> identity() {
        return (x) -> x;
    }

}
