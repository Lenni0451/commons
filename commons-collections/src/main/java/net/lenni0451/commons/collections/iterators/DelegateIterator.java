package net.lenni0451.commons.collections.iterators;

import java.util.Iterator;

/**
 * An iterator which delegates all calls to the given iterator.
 *
 * @param <E> The type of the elements in this iterator
 */
public class DelegateIterator<E> implements Iterator<E> {

    private final Iterator<E> delegate;

    public DelegateIterator(final Iterator<E> delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean hasNext() {
        return this.delegate.hasNext();
    }

    @Override
    public E next() {
        return this.delegate.next();
    }

    @Override
    public void remove() {
        this.delegate.remove();
    }

}
