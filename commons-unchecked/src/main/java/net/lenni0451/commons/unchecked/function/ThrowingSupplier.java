package net.lenni0451.commons.unchecked.function;

import lombok.SneakyThrows;

import java.util.function.Supplier;

/**
 * A supplier that can throw checked exceptions.
 *
 * @param <T> The return type
 */
@FunctionalInterface
public interface ThrowingSupplier<T> {

    /**
     * Get the value from the supplier and sneakily throw any exceptions.
     *
     * @param supplier The ThrowingSupplier
     * @param <T>      The return type
     * @return The value
     */
    static <T> T tryGet(final ThrowingSupplier<T> supplier) {
        return supplier.tryGet();
    }

    /**
     * Convert a ThrowingSupplier to a standard Supplier.
     *
     * @param supplier The ThrowingSupplier
     * @param <T>      The return type
     * @return The standard Supplier
     */
    static <T> Supplier<T> toSupplier(final ThrowingSupplier<T> supplier) {
        return supplier.toSupplier();
    }


    /**
     * Get the value from the supplier.
     *
     * @return The value
     * @throws Throwable If an error occurs
     */
    T get() throws Throwable;

    /**
     * Get the value from the supplier and sneakily throw any exceptions.
     *
     * @return The value
     */
    @SneakyThrows
    default T tryGet() {
        return this.get();
    }

    /**
     * Convert this ThrowingSupplier to a standard Supplier.
     *
     * @return The standard Supplier
     */
    default Supplier<T> toSupplier() {
        return this::tryGet;
    }

}
