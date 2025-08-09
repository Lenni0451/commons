package net.lenni0451.commons.collections.lists;

import net.lenni0451.commons.collections.iterators.DelegateIterator;
import net.lenni0451.commons.collections.iterators.DelegateListIterator;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Consumer;

/**
 * A list which delegates all calls to the given list.<br>
 * All mutation methods will call the given listeners.<br>
 * The listeners will be called before the mutation is applied to the list.
 *
 * @param <E> The type of the elements in this list
 */
public class DelegateListenerList<E> extends DelegateList<E> {

    private final Consumer<E> addListener;
    private final Consumer<E> removeListener;

    public DelegateListenerList(final List<E> delegate, final Consumer<E> addListener, final Consumer<E> removeListener) {
        super(delegate);
        this.addListener = addListener;
        this.removeListener = removeListener;
    }

    @Nonnull
    @Override
    public Iterator<E> iterator() {
        return new DelegateIterator<E>(super.iterator()) {
            @Override
            public void remove() {
                throw new UnsupportedOperationException("Remove is not supported on this iterator");
            }
        };
    }

    @Override
    public boolean add(E e) {
        this.addListener.accept(e);
        return super.add(e);
    }

    @Override
    public boolean remove(Object o) {
        this.removeListener.accept((E) o);
        return super.remove(o);
    }

    @Override
    public boolean addAll(@Nonnull Collection<? extends E> c) {
        for (E e : c) {
            this.addListener.accept(e);
        }
        return super.addAll(c);
    }

    @Override
    public boolean addAll(int index, @Nonnull Collection<? extends E> c) {
        for (E e : c) {
            this.addListener.accept(e);
        }
        return super.addAll(index, c);
    }

    @Override
    public boolean removeAll(@Nonnull Collection<?> c) {
        for (Object o : c) {
            this.removeListener.accept((E) o);
        }
        return super.removeAll(c);
    }

    @Override
    public boolean retainAll(@Nonnull Collection<?> c) {
        for (E e : this) {
            if (!c.contains(e)) {
                this.removeListener.accept(e);
            }
        }
        return super.retainAll(c);
    }

    @Override
    public void clear() {
        for (E e : this) {
            this.removeListener.accept(e);
        }
        super.clear();
    }

    @Nonnull
    @Override
    public ListIterator<E> listIterator() {
        return new ListIteratorProxy(super.listIterator());
    }

    @Nonnull
    @Override
    public ListIterator<E> listIterator(int index) {
        return new ListIteratorProxy(super.listIterator(index));
    }

    @Nonnull
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return new DelegateListenerList<>(super.subList(fromIndex, toIndex), this.addListener, this.removeListener);
    }


    private class ListIteratorProxy extends DelegateListIterator<E> {
        public ListIteratorProxy(final ListIterator<E> delegate) {
            super(delegate);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove is not supported on this iterator");
        }

        @Override
        public void set(E e) {
            throw new UnsupportedOperationException("Set is not supported on this iterator");
        }

        @Override
        public void add(E e) {
            DelegateListenerList.this.addListener.accept(e);
            super.add(e);
        }
    }

}
