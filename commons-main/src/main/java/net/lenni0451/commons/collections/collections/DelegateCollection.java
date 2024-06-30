package net.lenni0451.commons.collections.collections;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Iterator;

/**
 * A collection which delegates all calls to the given collection.
 *
 * @param <E> The type of the elements in this collection
 */
public class DelegateCollection<E> implements Collection<E> {

    private final Collection<E> delegate;

    public DelegateCollection(final Collection<E> delegate) {
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
    public boolean contains(Object o) {
        return this.delegate.contains(o);
    }

    @Nonnull
    @Override
    public Iterator<E> iterator() {
        return this.delegate.iterator();
    }

    @Nonnull
    @Override
    public Object[] toArray() {
        return this.delegate.toArray();
    }

    @Nonnull
    @Override
    public <T> T[] toArray(@Nonnull T[] a) {
        return this.delegate.toArray(a);
    }

    @Override
    public boolean add(E e) {
        return this.delegate.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return this.delegate.remove(o);
    }

    @Override
    public boolean containsAll(@Nonnull Collection<?> c) {
        return this.delegate.containsAll(c);
    }

    @Override
    public boolean addAll(@Nonnull Collection<? extends E> c) {
        return this.delegate.addAll(c);
    }

    @Override
    public boolean removeAll(@Nonnull Collection<?> c) {
        return this.delegate.removeAll(c);
    }

    @Override
    public boolean retainAll(@Nonnull Collection<?> c) {
        return this.delegate.retainAll(c);
    }

    @Override
    public void clear() {
        this.delegate.clear();
    }

}
