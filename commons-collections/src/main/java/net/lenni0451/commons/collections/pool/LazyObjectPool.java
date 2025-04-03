package net.lenni0451.commons.collections.pool;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class LazyObjectPool<T> extends ObjectPool<T> {

    protected final Map<T, Long> timestamps = this.createMap();

    protected <K, V> Map<K, V> createMap() {
        return new HashMap<>();
    }

    protected void cleanup(final long timeoutMs) {
        Iterator<Map.Entry<T, Long>> it = this.timestamps.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<T, Long> entry = it.next();
            if (!this.free.contains(entry.getKey())) continue;
            if (System.nanoTime() - entry.getValue() > timeoutMs * 1_000_000) {
                it.remove();
                this.free.remove(entry.getKey());
                this.onCleanup(entry.getKey());
            }
        }
    }

    protected void onCleanup(final T object) {
        // Override this method to perform any actions after cleanup
    }

    @Override
    public T borrowObject() {
        T object;
        if (this.free.isEmpty()) object = this.create();
        else object = this.free.remove(0);
        this.used.add(object);
        this.timestamps.put(object, System.nanoTime());
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
