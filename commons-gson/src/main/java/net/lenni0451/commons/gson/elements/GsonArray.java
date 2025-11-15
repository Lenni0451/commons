package net.lenni0451.commons.gson.elements;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class GsonArray extends GsonElement implements Iterable<GsonElement> {

    private final JsonArray array;

    public GsonArray() {
        this(new JsonArray());
    }

    public GsonArray(final JsonArray array) {
        super(array);
        this.array = array;
    }

    public JsonArray getJsonArray() {
        return this.array;
    }

    public GsonArray add(@Nullable final JsonElement element) {
        this.array.add(element);
        return this;
    }

    public GsonArray add(@Nullable final GsonElement element) {
        this.array.add(element == null ? null : element.getJsonElement());
        return this;
    }

    public GsonArray add(final boolean b) {
        this.array.add(b);
        return this;
    }

    public GsonArray add(@Nullable final Number number) {
        this.array.add(number);
        return this;
    }

    public GsonArray add(@Nullable final String s) {
        this.array.add(s);
        return this;
    }

    public GsonArray addAll(final JsonArray other) {
        this.array.addAll(other);
        return this;
    }

    public GsonArray addAll(final GsonArray other) {
        this.array.addAll(other.getJsonArray());
        return this;
    }

    public GsonElement set(final int index, final JsonElement element) {
        JsonElement previous = this.array.set(index, element);
        if (previous == null) return null;
        return GsonElement.wrap(previous);
    }

    public GsonElement set(final int index, final GsonElement element) {
        JsonElement previous = this.array.set(index, element.getJsonElement());
        if (previous == null) return null;
        return GsonElement.wrap(previous);
    }

    public GsonElement set(final int index, final boolean b) {
        JsonElement previous = this.array.set(index, new JsonPrimitive(b));
        if (previous == null) return null;
        return GsonElement.wrap(previous);
    }

    public GsonElement set(final int index, final Number number) {
        JsonElement previous = this.array.set(index, new JsonPrimitive(number));
        if (previous == null) return null;
        return GsonElement.wrap(previous);
    }

    public GsonElement set(final int index, final String s) {
        JsonElement previous = this.array.set(index, new JsonPrimitive(s));
        if (previous == null) return null;
        return GsonElement.wrap(previous);
    }

    public GsonElement get(final int index) {
        return GsonElement.wrap(this.array.get(index));
    }

    public GsonElement remove(final int index) {
        JsonElement previous = this.array.remove(index);
        if (previous == null) return null;
        return GsonElement.wrap(previous);
    }

    public boolean remove(@Nullable final JsonElement element) {
        return this.array.remove(element == null ? JsonNull.INSTANCE : element);
    }

    public boolean remove(@Nullable final GsonElement element) {
        return this.array.remove(element == null ? JsonNull.INSTANCE : element.getJsonElement());
    }

    public boolean removeBoolean(final boolean b) {
        return this.remove(new JsonPrimitive(b));
    }

    public boolean removeNumber(final Number number) {
        return this.remove(new JsonPrimitive(number));
    }

    public boolean removeString(final String s) {
        return this.remove(new JsonPrimitive(s));
    }

    public boolean removeAll(final JsonArray other) {
        boolean changed = false;
        for (JsonElement element : other) {
            changed |= this.array.remove(element);
        }
        return changed;
    }

    public boolean removeAll(final GsonArray other) {
        return this.removeAll(other.getJsonArray());
    }

    public GsonArray clear() {
        while (!this.array.isEmpty()) this.array.remove(0);
        return this;
    }

    public boolean has(final int index) {
        return index >= 0 && index < this.array.size();
    }

    public boolean has(@Nullable final JsonElement element) {
        return this.array.contains(element == null ? JsonNull.INSTANCE : element);
    }

    public boolean has(@Nullable final GsonElement element) {
        return this.array.contains(element == null ? JsonNull.INSTANCE : element.getJsonElement());
    }

    public int size() {
        return this.array.size();
    }

    public boolean isEmpty() {
        return this.array.isEmpty();
    }

    @Nonnull
    @Override
    public Iterator<GsonElement> iterator() {
        return new WrappedIterator(this.array.iterator());
    }

    public Stream<GsonElement> stream() {
        return StreamSupport.stream(this.spliterator(), false);
    }

    public List<GsonElement> asList() {
        List<GsonElement> list = new ArrayList<>();
        for (JsonElement element : this.array) {
            list.add(GsonElement.wrap(element));
        }
        return list;
    }

    public <T> List<T> asList(final Function<GsonElement, T> mapper) {
        List<T> list = new ArrayList<>();
        for (JsonElement element : this.array) {
            list.add(mapper.apply(GsonElement.wrap(element)));
        }
        return list;
    }


    private static class WrappedIterator implements Iterator<GsonElement> {
        private final Iterator<JsonElement> iterator;

        public WrappedIterator(final Iterator<JsonElement> iterator) {
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext() {
            return this.iterator.hasNext();
        }

        @Override
        public GsonElement next() {
            return GsonElement.wrap(this.iterator.next());
        }

        @Override
        public void remove() {
            this.iterator.remove();
        }
    }

}
