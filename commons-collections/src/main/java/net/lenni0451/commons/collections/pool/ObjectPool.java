package net.lenni0451.commons.collections.pool;

import java.util.ArrayList;
import java.util.List;

public abstract class ObjectPool<T> {

    protected final List<T> free = this.createList();
    protected final List<T> used = this.createList();

    protected <L> List<L> createList() {
        return new ArrayList<>();
    }

    public abstract T borrowObject();

    public abstract void returnObject(final T object);

    public int freeSize() {
        return this.free.size();
    }

    public int usedSize() {
        return this.used.size();
    }

    public int size() {
        return this.free.size() + this.used.size();
    }

    protected abstract T create();

}
