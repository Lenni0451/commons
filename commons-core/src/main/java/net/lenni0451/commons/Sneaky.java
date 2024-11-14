package net.lenni0451.commons;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class Sneaky {

    /**
     * Fake throw an exception to have to declare it in the method signature.<br>
     * Can be used to suppress warnings when using {@link #sneak}
     *
     * @param exceptionType The type of the exception to fake throw
     * @param <T>           The type of the exception
     * @throws T The exception
     */
    public static <T extends Throwable> void fake(final Class<T> exceptionType) throws T {
    }

    /**
     * Throw a throwable without having to declare it in the method signature.
     *
     * @param t The throwable to throw
     */
    @SneakyThrows
    public static void sneak(final Throwable t) {
        throw t;
    }

    /**
     * Run a runnable without having to declare the throwable in the method signature.
     *
     * @param runnable The runnable to run
     */
    @SneakyThrows
    public static void sneak(final SneakyRunnable runnable) {
        runnable.run();
    }

    /**
     * Get a value from a supplier without having to declare the throwable in the method signature.
     *
     * @param supplier The supplier to get the value from
     * @param <O>      The type of the value
     * @return The value
     */
    @SneakyThrows
    public static <O> O sneak(final SneakySupplier<O> supplier) {
        return supplier.get();
    }

    /**
     * Convert a {@link SneakyRunnable} to a {@link Runnable}.
     *
     * @param runnable The sneaky runnable to convert
     * @return The converted runnable
     */
    public Runnable toRunnable(final SneakyRunnable runnable) {
        return () -> sneak(runnable);
    }

    /**
     * Convert a {@link SneakySupplier} to a {@link Supplier}.
     *
     * @param supplier The sneaky supplier to convert
     * @param <O>      The type of the value
     * @return The converted supplier
     */
    public <O> O toSupplier(final SneakySupplier<O> supplier) {
        return sneak(supplier);
    }


    @FunctionalInterface
    public interface SneakyRunnable {
        void run() throws Throwable;
    }

    @FunctionalInterface
    public interface SneakySupplier<T> {
        T get() throws Throwable;
    }

}
