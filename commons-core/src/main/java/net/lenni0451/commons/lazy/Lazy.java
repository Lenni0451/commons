package net.lenni0451.commons.lazy;

import javax.annotation.concurrent.ThreadSafe;
import java.util.function.Supplier;

/**
 * An object to store a value that is only initialized when required.<br>
 * Useful for objects that are expensive to create and are not always needed.
 *
 * @param <T> The type of the object
 */
@ThreadSafe
public abstract class Lazy<T> {

    /**
     * Create a new lazy object.
     *
     * @param supplier The supplier to create the object
     * @param <T>      The type of the object
     * @return The lazy object
     */
    public static <T> Lazy<T> of(final Supplier<T> supplier) {
        return new Lazy<T>() {
            @Override
            protected T calculate() {
                return supplier.get();
            }
        };
    }


    protected final Object lock = new Object();
    private T value;
    private volatile boolean initialized = false;

    public boolean isInitialized() {
        return this.initialized;
    }

    /**
     * @return The value of the object
     */
    public T get() {
        if (!this.initialized) {
            synchronized (this.lock) {
                if (!this.initialized) {
                    this.value = this.calculate();
                    this.initialized = true;
                }
            }
        }
        return this.value;
    }

    /**
     * Invalidate the current value so that it will be recalculated on the next call of {@link #get()}.<br>
     * This method only sets the {@code initialized} flag to {@code false} and does not reset the value itself.
     */
    public void invalidate() {
        this.initialized = false;
    }

    protected abstract T calculate();

}
