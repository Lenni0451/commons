package net.lenni0451.commons.unchecked.function;

import lombok.SneakyThrows;

/**
 * A runnable that can throw checked exceptions.
 */
@FunctionalInterface
public interface ThrowingRunnable {

    /**
     * Run the runnable and sneakily throw any exceptions.
     *
     * @param runnable The ThrowingRunnable
     */
    static void tryRun(final ThrowingRunnable runnable) {
        runnable.tryRun();
    }

    /**
     * Convert a ThrowingRunnable to a standard Runnable.
     *
     * @param runnable The ThrowingRunnable
     * @return The standard Runnable
     */
    static Runnable toRunnable(final ThrowingRunnable runnable) {
        return runnable.toRunnable();
    }


    /**
     * Run the runnable.
     *
     * @throws Throwable If an error occurs
     */
    void run() throws Throwable;

    /**
     * Run the runnable and sneakily throw any exceptions.
     */
    @SneakyThrows
    default void tryRun() {
        this.run();
    }

    /**
     * Convert this ThrowingRunnable to a standard Runnable.
     *
     * @return The standard Runnable
     */
    default Runnable toRunnable() {
        return this::tryRun;
    }

}
