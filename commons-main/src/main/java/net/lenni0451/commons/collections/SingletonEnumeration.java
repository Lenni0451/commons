package net.lenni0451.commons.collections;

import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * An enumeration that only contains one element.
 *
 * @param <T> The type of the element
 */
public class SingletonEnumeration<T> implements Enumeration<T> {

    private final T element;
    private boolean hasMoreElements = true;

    public SingletonEnumeration(final T element) {
        this.element = element;
    }

    @Override
    public boolean hasMoreElements() {
        return this.hasMoreElements;
    }

    @Override
    public T nextElement() {
        if (!this.hasMoreElements) throw new NoSuchElementException();
        this.hasMoreElements = false;
        return this.element;
    }

}
