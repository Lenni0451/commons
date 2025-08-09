package net.lenni0451.commons.collections.sets;

import net.lenni0451.commons.collections.iterators.DelegateIterator;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;

public class DelegateListenerSet<E> extends DelegateSet<E> {

    private final Consumer<E> addListener;
    private final Consumer<E> removeListener;

    public DelegateListenerSet(final Set<E> delegate, final Consumer<E> addListener, final Consumer<E> removeListener) {
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
    public boolean retainAll(@Nonnull Collection<?> c) {
        for (Object o : this) {
            if (!c.contains(o)) {
                this.removeListener.accept((E) o);
            }
        }
        return super.retainAll(c);
    }

    @Override
    public boolean removeAll(@Nonnull Collection<?> c) {
        for (Object o : c) {
            this.removeListener.accept((E) o);
        }
        return super.removeAll(c);
    }

    @Override
    public void clear() {
        this.forEach(this.removeListener);
        super.clear();
    }

}
