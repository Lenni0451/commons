package net.lenni0451.commons.collections.maps;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * A map which delegates all calls to the given map.
 *
 * @param <K> The type of the keys in this map
 * @param <V> The type of the values in this map
 */
public class DelegateMap<K, V> implements Map<K, V> {

    private final Map<K, V> delegate;

    public DelegateMap(final Map<K, V> delegate) {
        this.delegate = delegate;
    }

    @Override
    public int size() {
        return this.delegate.size();
    }

    @Override
    public boolean isEmpty() {
        return this.delegate.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return this.delegate.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.delegate.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return this.delegate.get(key);
    }

    @Nullable
    @Override
    public V put(K key, V value) {
        return this.delegate.put(key, value);
    }

    @Override
    public V remove(Object key) {
        return this.delegate.remove(key);
    }

    @Override
    public void putAll(@Nonnull Map<? extends K, ? extends V> m) {
        this.delegate.putAll(m);
    }

    @Override
    public void clear() {
        this.delegate.clear();
    }

    @Nonnull
    @Override
    public Set<K> keySet() {
        return this.delegate.keySet();
    }

    @Nonnull
    @Override
    public Collection<V> values() {
        return this.delegate.values();
    }

    @Nonnull
    @Override
    public Set<Entry<K, V>> entrySet() {
        return this.delegate.entrySet();
    }

}
