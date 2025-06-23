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
     * Use {@link SneakyRunnable#tryRun()} instead.
     */
    @Deprecated
    @SneakyThrows
    @ApiStatus.ScheduledForRemoval
    public static void sneak(final SneakyRunnable runnable) {
        runnable.run();
    }

    /**
     * Use {@link SneakySupplier#tryGet()} instead.
     */
    @Deprecated
    @SneakyThrows
    @ApiStatus.ScheduledForRemoval
    public static <O> O sneak(final SneakySupplier<O> supplier) {
        return supplier.get();
    }

    /**
     * Use {@link SneakyRunnable#toRunnable()} instead.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public Runnable toRunnable(final SneakyRunnable runnable) {
        return () -> sneak(runnable);
    }

    /**
     * Use {@link SneakySupplier#toSupplier()} instead.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public <O> O toSupplier(final SneakySupplier<O> supplier) {
        return sneak(supplier);
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
