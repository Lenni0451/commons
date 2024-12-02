package net.lenni0451.commons;

import org.jetbrains.annotations.ApiStatus;

import javax.annotation.concurrent.ThreadSafe;
import java.util.function.Supplier;

@ThreadSafe
@Deprecated
@ApiStatus.ScheduledForRemoval //01.06.2025
public class Lazy<T> {

    public static <T> Lazy<T> of(final Supplier<T> supplier) {
        return new Lazy<>(supplier);
    }


    private final Object lock = new Object();
    private final Supplier<T> supplier;
    private T value;
    private volatile boolean initialized = false;

    public Lazy(final Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public T get() {
        if (!this.initialized) {
            synchronized (this.lock) {
                if (!this.initialized) {
                    this.value = this.supplier.get();
                    this.initialized = true;
                }
            }
        }
        return this.value;
    }

}
