package net.lenni0451.commons.collections.maps;

import net.lenni0451.commons.collections.collections.DelegateCollection;
import net.lenni0451.commons.collections.iterators.DelegateIterator;
import net.lenni0451.commons.collections.sets.DelegateSet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import java.util.*;

/**
 * A map which is size constrained.<br>
 * If more elements are added than the maximum size, the oldest elements will be removed.<br>
 * This map is not thread safe.
 *
 * @param <K> The type of the keys in this map
 * @param <V> The type of the values in this map
 */
@NotThreadSafe
public class SizeConstrainedMap<K, V> extends DelegateMap<K, V> {

    private final List<K> keys;
    private final int maxSize;

    private KeySet keySet;
    private Values values;
    private EntrySet entrySet;

    public SizeConstrainedMap(final Map<K, V> delegate, final int maxSize) {
        super(delegate);
        if (!delegate.isEmpty()) throw new IllegalArgumentException("Delegate map must be empty");
        this.keys = new ArrayList<>();
        this.maxSize = maxSize;
    }

    @Nullable
    @Override
    public V put(K key, V value) {
        V returnValue = super.put(key, value);
        if (this.keys.size() >= this.maxSize) super.remove(this.keys.remove(0));
        this.keys.add(key);
        return returnValue;
    }

    @Override
    public V remove(Object key) {
        V returnValue = super.remove(key);
        this.keys.remove(key);
        return returnValue;
    }

    @Override
    public void putAll(@Nonnull Map<? extends K, ? extends V> m) {
        for (Entry<? extends K, ? extends V> entry : m.entrySet()) {
            this.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        super.clear();
        this.keys.clear();
    }

    @Nonnull
    @Override
    public Set<K> keySet() {
        if (this.keySet == null) this.keySet = new KeySet(super.keySet());
        return this.keySet;
    }

    @Nonnull
    @Override
    public Collection<V> values() {
        if (this.values == null) this.values = new Values(super.values());
        return this.values;
    }

    @Nonnull
    @Override
    public Set<Entry<K, V>> entrySet() {
        if (this.entrySet == null) this.entrySet = new EntrySet(super.entrySet());
        return this.entrySet;
    }


    private class KeySet extends DelegateSet<K> {
        public KeySet(final Set<K> delegate) {
            super(delegate);
        }

        @Nonnull
        @Override
        public Iterator<K> iterator() {
            return new DelegateIterator<K>(super.iterator()) {
                private K lastKey;

                @Override
                public K next() {
                    this.lastKey = super.next();
                    return this.lastKey;
                }

                @Override
                public void remove() {
                    super.remove();
                    SizeConstrainedMap.this.keys.remove(this.lastKey);
                }
            };
        }

        @Override
        public boolean add(K k) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(@Nonnull Collection<? extends K> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean remove(Object o) {
            boolean returnValue = super.remove(o);
            if (returnValue) SizeConstrainedMap.this.keys.remove(o);
            return returnValue;
        }

        @Override
        public boolean retainAll(@Nonnull Collection<?> c) {
            boolean returnValue = super.retainAll(c);
            if (returnValue) SizeConstrainedMap.this.keys.retainAll(c);
            return returnValue;
        }

        @Override
        public boolean removeAll(@Nonnull Collection<?> c) {
            boolean returnValue = super.removeAll(c);
            if (returnValue) SizeConstrainedMap.this.keys.removeAll(c);
            return returnValue;
        }
    }

    private class Values extends DelegateCollection<V> {
        public Values(final Collection<V> delegate) {
            super(delegate);
        }

        @Nonnull
        @Override
        public Iterator<V> iterator() {
            return new DelegateIterator<V>(super.iterator()) {
                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }

        @Override
        public boolean add(V v) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(@Nonnull Collection<? extends V> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean remove(Object o) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean retainAll(@Nonnull Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeAll(@Nonnull Collection<?> c) {
            throw new UnsupportedOperationException();
        }
    }

    private class EntrySet extends DelegateSet<Entry<K, V>> {
        public EntrySet(final Set<Entry<K, V>> delegate) {
            super(delegate);
        }

        @Nonnull
        @Override
        public Iterator<Entry<K, V>> iterator() {
            return new DelegateIterator<Entry<K, V>>(super.iterator()) {
                private Entry<K, V> lastEntry;

                @Override
                public Entry<K, V> next() {
                    this.lastEntry = super.next();
                    return this.lastEntry;
                }

                @Override
                public void remove() {
                    super.remove();
                    SizeConstrainedMap.this.keys.remove(this.lastEntry.getKey());
                }
            };
        }

        @Override
        public boolean add(Entry<K, V> kvEntry) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(@Nonnull Collection<? extends Entry<K, V>> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean remove(Object o) {
            boolean returnValue = super.remove(o);
            if (returnValue) SizeConstrainedMap.this.keys.remove(((Entry<K, V>) o).getKey());
            return returnValue;
        }

        @Override
        public boolean retainAll(@Nonnull Collection<?> c) {
            boolean returnValue = super.retainAll(c);
            if (returnValue) {
                for (Entry<K, V> entry : this) {
                    if (!c.contains(entry)) SizeConstrainedMap.this.keys.remove(entry.getKey());
                }
            }
            return returnValue;
        }

        @Override
        public boolean removeAll(@Nonnull Collection<?> c) {
            boolean returnValue = super.removeAll(c);
            if (returnValue) {
                for (Object o : c) {
                    if (o instanceof Entry) SizeConstrainedMap.this.keys.remove(((Entry<K, V>) o).getKey());
                }
            }
            return returnValue;
        }
    }

}
