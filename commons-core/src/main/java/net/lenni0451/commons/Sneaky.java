package net.lenni0451.commons;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Consumer;
import java.util.function.Function;
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
     * Cast an object to any type without any checks.<br>
     * This method should be used with caution, as it can lead to {@link ClassCastException}s at runtime if the object is not of the expected type.
     *
     * @param object The object to cast
     * @param <T>    The type to cast the object to
     * @return The object casted to the specified type
     */
    public static <T> T unsafeCast(final Object object) {
        return (T) object;
    }

    /**
     * Use {@link SneakyRunnable#tryRun()} instead.
     */
    @Deprecated
    @SneakyThrows
    @ApiStatus.ScheduledForRemoval //23.12.2025
    public static void sneak(final SneakyRunnable runnable) {
        runnable.run();
    }

    /**
     * Use {@link SneakySupplier#tryGet()} instead.
     */
    @Deprecated
    @SneakyThrows
    @ApiStatus.ScheduledForRemoval //23.12.2025
    public static <O> O sneak(final SneakySupplier<O> supplier) {
        return supplier.get();
    }

    /**
     * Turn a sneaky runnable into a regular runnable.
     *
     * @param sneakyRunnable The sneaky runnable to convert
     * @return A regular runnable that will execute the sneaky runnable's run method
     * @see SneakyRunnable#toRunnable()
     */
    public Runnable toRunnable(final SneakyRunnable sneakyRunnable) {
        return sneakyRunnable.toRunnable();
    }

    /**
     * Turn a sneaky supplier into a regular supplier.
     *
     * @param sneakySupplier The sneaky supplier to convert
     * @param <T>            The type of the value returned by the supplier
     * @return A regular supplier that will execute the sneaky supplier's get method
     * @see SneakySupplier#toSupplier()
     */
    public <T> Supplier<T> toSupplier(final SneakySupplier<T> sneakySupplier) {
        return sneakySupplier.toSupplier();
    }

    /**
     * Turn a sneaky consumer into a regular consumer.
     *
     * @param sneakyConsumer The sneaky consumer to convert
     * @param <T>            The type of the value accepted by the consumer
     * @return A regular consumer that will execute the sneaky consumer's accept method
     * @see SneakyConsumer#toConsumer()
     */
    public <T> Consumer<T> toConsumer(final SneakyConsumer<T> sneakyConsumer) {
        return sneakyConsumer.toConsumer();
    }

    /**
     * Turn a sneaky function into a regular function.
     *
     * @param sneakyFunction The sneaky function to convert
     * @param <T>            The type of the input to the function
     * @param <R>            The type of the result of the function
     * @return A regular function that will execute the sneaky function's apply method
     * @see SneakyFunction#toFunction()
     */
    public <T, R> Function<T, R> toFunction(final SneakyFunction<T, R> sneakyFunction) {
        return sneakyFunction.toFunction();
    }


    @FunctionalInterface
    public interface SneakyRunnable {
        void run() throws Throwable;

        @SneakyThrows
        default void tryRun() {
            this.run();
        }

        default Runnable toRunnable() {
            return this::tryRun;
        }
    }

    @FunctionalInterface
    public interface SneakySupplier<T> {
        T get() throws Throwable;

        @SneakyThrows
        default T tryGet() {
            return this.get();
        }

        default Supplier<T> toSupplier() {
            return this::tryGet;
        }
    }

    @FunctionalInterface
    public interface SneakyConsumer<T> {
        void accept(final T t) throws Throwable;

        @SneakyThrows
        default void tryAccept(final T t) {
            this.accept(t);
        }

        default Consumer<T> toConsumer() {
            return this::tryAccept;
        }
    }

    @FunctionalInterface
    public interface SneakyFunction<T, R> {
        R apply(final T t) throws Throwable;

        @SneakyThrows
        default R tryApply(final T t) {
            return this.apply(t);
        }

        default Function<T, R> toFunction() {
            return this::tryApply;
        }
    }

}
