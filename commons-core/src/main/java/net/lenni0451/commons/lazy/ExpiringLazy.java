package net.lenni0451.commons.lazy;

import java.util.function.Supplier;

/**
 * A {@link Lazy} implementation with an expiration time.<br>
 * The value will be recalculated after the expiration time has passed.
 *
 * @param <T> The type of the object
 * @see Lazy
 */
public abstract class ExpiringLazy<T> extends Lazy<T> {

    /**
     * Create a new expiring lazy object using a {@link Supplier} to calculate the value.
     *
     * @param expirationType The type of the expiration
     * @param expirationTime The time in milliseconds after the value should be recalculated
     * @param supplier       The supplier to calculate the value
     * @param <T>            The type of the object
     * @return The expiring lazy object
     */
    public static <T> ExpiringLazy<T> of(final ExpirationType expirationType, final long expirationTime, final Supplier<T> supplier) {
        return new ExpiringLazy<T>(expirationType, expirationTime) {
            @Override
            protected T calculate() {
                return supplier.get();
            }
        };
    }


    private final ExpirationType expirationType;
    private final long expirationTime;
    private long lastAccessTime;

    public ExpiringLazy(final ExpirationType expirationType, final long expirationTime) {
        this.expirationType = expirationType;
        this.expirationTime = expirationTime;
        this.lastAccessTime = System.currentTimeMillis();
    }

    public ExpirationType getExpirationType() {
        return this.expirationType;
    }

    public long getExpirationTime() {
        return this.expirationTime;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() - this.lastAccessTime > this.expirationTime;
    }

    @Override
    public T get() {
        if (this.isExpired()) super.invalidate();
        if (this.expirationType == ExpirationType.READ) {
            this.lastAccessTime = System.currentTimeMillis();
        } else if (this.expirationType == ExpirationType.WRITE) {
            if (!super.isInitialized()) this.lastAccessTime = System.currentTimeMillis();
        }
        return super.get();
    }


    public enum ExpirationType {
        /**
         * Expire the value n milliseconds after the last {@link #get()} call.
         */
        READ,
        /**
         * Expire the value n milliseconds after the last initialization.
         */
        WRITE
    }

}
