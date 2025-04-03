package net.lenni0451.commons.collections.pool;

import javax.annotation.Nullable;

public abstract class FixedObjectPool<T> extends ObjectPool<T> {

    public FixedObjectPool(final int size) {
        for (int i = 0; i < size; i++) {
            this.free.add(this.create());
        }
    }

    @Override
    public T borrowObject() {
        if (this.free.isEmpty()) throw new IllegalStateException("No objects available in the pool");

        T object = this.free.remove(0);
        this.used.add(object);
        return object;
    }

    @Nullable
    public T tryBorrowObject() {
        if (this.free.isEmpty()) return null;

        T object = this.free.remove(0);
        this.used.add(object);
        return object;
    }

    @Override
    public void returnObject(final T object) {
        if (this.used.remove(object)) {
            this.free.add(object);
        } else {
            throw new IllegalArgumentException("Object not borrowed from this pool");
        }
    }

}
