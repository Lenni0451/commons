package net.lenni0451.commons.unchecked.function;

import lombok.SneakyThrows;

import java.util.function.Function;

/**
 * A function that can throw checked exceptions.
 *
 * @param <T> The input type
 * @param <R> The return type
 */
@FunctionalInterface
public interface ThrowingFunction<T, R> {

    /**
     * Convert a ThrowingFunction to a standard Function.
     *
     * @param function The ThrowingFunction
     * @param <T>      The input type
     * @param <R>      The return type
     * @return The standard Function
     */
    static <T, R> Function<T, R> toFunction(final ThrowingFunction<T, R> function) {
        return function.toFunction();
    }


    /**
     * Apply the function to the input.
     *
     * @param t The input
     * @return The result
     * @throws Throwable If an error occurs
     */
    R apply(final T t) throws Throwable;

    /**
     * Apply the function to the input and sneakily throw any exceptions.
     *
     * @param t The input
     * @return The result
     */
    @SneakyThrows
    default R tryApply(final T t) {
        return this.apply(t);
    }

    /**
     * Convert this ThrowingFunction to a standard Function.
     *
     * @return The standard Function
     */
    default Function<T, R> toFunction() {
        return this::tryApply;
    }

}
