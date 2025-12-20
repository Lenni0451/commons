package net.lenni0451.commons.unchecked;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.lenni0451.commons.unchecked.function.ThrowingFunction;
import net.lenni0451.commons.unchecked.function.ThrowingSupplier;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A utility to initialize fields with various strategies.
 *
 * @param <T> The value type
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class FieldInitializer<T> {

    /**
     * Create a FieldInitializer with a constant value.
     *
     * @param value The value
     * @param <T>   The value type
     * @return The FieldInitializer
     */
    public static <T> FieldInitializer<T> of(final T value) {
        return attempt(() -> value);
    }

    /**
     * Create a FieldInitializer that attempts to get the value from the supplier.
     *
     * @param supplier The supplier
     * @param <T>      The value type
     * @return The FieldInitializer
     */
    public static <T> FieldInitializer<T> attempt(final ThrowingSupplier<T> supplier) {
        return new FieldInitializer<>(supplier);
    }

    /**
     * Create a FieldInitializer that tries multiple suppliers and returns the first non-null value.<br>
     * If all suppliers throw an exception or return null, an IllegalStateException is thrown.
     *
     * @param suppliers The suppliers to try
     * @param <T>       The value type
     * @return The FieldInitializer
     * @throws IllegalStateException If all suppliers fail
     */
    @SafeVarargs
    public static <T> FieldInitializer<T> firstOf(final ThrowingSupplier<T>... suppliers) {
        return attempt(() -> {
            Throwable cause = null;
            for (ThrowingSupplier<T> supplier : suppliers) {
                try {
                    T value = supplier.get();
                    if (value != null) return value;
                } catch (Throwable t) {
                    if (cause == null) cause = new IllegalStateException("All suppliers failed to provide a value");
                    cause.addSuppressed(t);
                }
            }
            if (cause != null) throw cause;
            throw new IllegalStateException("All suppliers returned null");
        });
    }


    private final ThrowingSupplier<T> supplier;

    /**
     * Map the value of this FieldInitializer.
     *
     * @param mapper The mapper function
     * @param <R>    The new value type
     * @return A new FieldInitializer with the mapped value
     */
    public <R> FieldInitializer<R> map(final ThrowingFunction<T, R> mapper) {
        return attempt(() -> {
            T value = this.supplier.get();
            if (value == null) return null;
            return mapper.apply(value);
        });
    }

    /**
     * Try to get the value from this FieldInitializer, if it is null or throws an exception, try the fallback supplier.
     *
     * @param supplier The fallback supplier
     * @return A new FieldInitializer with the fallback
     */
    public FieldInitializer<T> or(final ThrowingSupplier<T> supplier) {
        return attempt(() -> {
            Throwable caught = null;
            try {
                T value = this.supplier.get();
                if (value != null) return value;
            } catch (Throwable t) {
                caught = t;
            }
            try {
                return supplier.get();
            } catch (Throwable t) {
                if (caught != null) t.addSuppressed(caught);
                throw t;
            }
        });
    }

    /**
     * Only get the value if the condition is true, otherwise return null.
     *
     * @param condition The condition
     * @return A new FieldInitializer with the condition
     */
    public FieldInitializer<T> onlyIf(final boolean condition) {
        return attempt(() -> {
            if (!condition) return null;
            return this.supplier.get();
        });
    }

    /**
     * Only get the value if the condition supplier returns true, otherwise return null.
     *
     * @param conditionSupplier The condition supplier
     * @return A new FieldInitializer with the condition
     */
    public FieldInitializer<T> onlyIf(final ThrowingSupplier<Boolean> conditionSupplier) {
        return attempt(() -> {
            if (!Boolean.TRUE.equals(conditionSupplier.get())) return null;
            return this.supplier.get();
        });
    }

    /**
     * Ensure that the value is not null, otherwise throw the provided exception.
     *
     * @param exceptionSupplier The exception supplier
     * @return A new FieldInitializer that ensures the value is not null
     */
    public FieldInitializer<T> ensure(final Supplier<Throwable> exceptionSupplier) {
        return attempt(() -> {
            T value = this.supplier.get();
            if (value == null) throw exceptionSupplier.get();
            return value;
        });
    }

    /**
     * Handle exceptions thrown by the supplier using the provided exception mapper.
     *
     * @param exceptionMapper The exception mapper
     * @return A new FieldInitializer that handles exceptions
     */
    public FieldInitializer<T> handleException(final Function<Throwable, Throwable> exceptionMapper) {
        return attempt(() -> {
            try {
                return this.supplier.get();
            } catch (Throwable t) {
                throw exceptionMapper.apply(t);
            }
        });
    }

    /**
     * Silently catch any exceptions thrown by the supplier and return null instead.
     *
     * @return A new FieldInitializer that silently catches exceptions
     */
    public FieldInitializer<T> silent() {
        return attempt(() -> {
            try {
                return this.supplier.get();
            } catch (Throwable t) {
                return null;
            }
        });
    }

    /**
     * Unchecked cast of this FieldInitializer to a different type.<br>
     * Beware of potential ClassCastExceptions when using this method.
     *
     * @param <R> The new value type
     * @return A new FieldInitializer with the casted type
     */
    public <R> FieldInitializer<R> uncheckedCast() {
        return (FieldInitializer<R>) this;
    }


    /**
     * Get the value from this FieldInitializer.<br>
     * May return null or throw an exception.
     *
     * @return The value (may be null)
     */
    public T get() {
        return this.supplier.tryGet();
    }

    /**
     * Get the value from this FieldInitializer, if it is null, throw the provided exception.<br>
     * May also throw exceptions from the supplier.
     *
     * @param exceptionSupplier The exception supplier
     * @return The value
     */
    @SneakyThrows
    public T require(final Supplier<Throwable> exceptionSupplier) {
        T value = this.supplier.tryGet();
        if (value == null) throw exceptionSupplier.get();
        return value;
    }

    /**
     * Get the value from this FieldInitializer, if it is null, return the provided other value.<br>
     * May also throw exceptions from the supplier.
     *
     * @param other The other value
     * @return The value or the other value
     */
    public T orElse(final T other) {
        T value = this.supplier.tryGet();
        return value != null ? value : other;
    }

}
