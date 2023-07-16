package net.lenni0451.commons;

public class Sneaky {

    /**
     * Fake throw an exception to have to declare it in the method signature.<br>
     * Can be used to suppress warnings when using {@link #sneak}
     *
     * @param exceptionType The type of the exception to fake throw
     * @param <T>           The type of the exception
     * @throws T The exception
     */
    @SuppressWarnings({"unused", "RedundantThrows"})
    public static <T extends Throwable> void fake(final Class<T> exceptionType) throws T {
    }

    /**
     * Throw a throwable without having to declare it in the method signature.
     *
     * @param t   The throwable to throw
     * @param <T> The type of the throwable
     * @throws T The throwable
     */
    @SuppressWarnings("unchecked")
    public static <T extends Throwable> void sneak(final Throwable t) throws T {
        throw (T) t;
    }

    /**
     * Run a runnable without having to declare the throwable in the method signature.
     *
     * @param runnable The runnable to run
     * @param <T>      The type of the throwable
     * @throws T The throwable
     */
    @SuppressWarnings("unchecked")
    public static <T extends Throwable> void sneak(final SneakyRunnable runnable) throws T {
        try {
            runnable.run();
        } catch (Throwable t) {
            throw (T) t;
        }
    }

    /**
     * Get a value from a supplier without having to declare the throwable in the method signature.
     *
     * @param supplier The supplier to get the value from
     * @param <O>      The type of the value
     * @param <T>      The type of the throwable
     * @return The value
     * @throws T The throwable
     */
    @SuppressWarnings("unchecked")
    public static <O, T extends Throwable> O sneak(final SneakySupplier<O> supplier) throws T {
        try {
            return supplier.get();
        } catch (Throwable t) {
            throw (T) t;
        }
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
