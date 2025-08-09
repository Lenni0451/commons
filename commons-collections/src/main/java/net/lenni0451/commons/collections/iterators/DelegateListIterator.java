package net.lenni0451.commons.collections.iterators;

import java.util.ListIterator;

/**
 * A list iterator which delegates all calls to the given list iterator.
 *
 * @param <E> The type of the elements in this list iterator
 */
public class DelegateListIterator<E> implements ListIterator<E> {

    private final ListIterator<E> delegate;

    public DelegateListIterator(final ListIterator<E> delegate) {
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
    public boolean hasPrevious() {
        return this.delegate.hasPrevious();
    }

    @Override
    public E previous() {
        return this.delegate.previous();
    }

    @Override
    public int nextIndex() {
        return this.delegate.nextIndex();
    }

    @Override
    public int previousIndex() {
        return this.delegate.previousIndex();
    }

    @Override
    public void remove() {
        this.delegate.remove();
    }

    @Override
    public void set(E e) {
        this.delegate.set(e);
    }

    @Override
    public void add(E e) {
        this.delegate.add(e);
    }

}
