package net.lenni0451.commons.gson.elements;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class GsonObject extends GsonElement implements Iterable<Map.Entry<String, GsonElement>> {

    private final JsonObject object;

    public GsonObject() {
        this(new JsonObject());
    }

    public GsonObject(final JsonObject object) {
        super(object);
        this.object = object;
    }

    public JsonObject getJsonObject() {
        return this.object;
    }

    public GsonObject add(final String key, @Nullable final JsonElement element) {
        this.object.add(key, element);
        return this;
    }

    public GsonObject add(final String key, @Nullable final GsonElement element) {
        this.object.add(key, element == null ? null : element.getJsonElement());
        return this;
    }

    public GsonObject add(final String key, final boolean b) {
        this.object.addProperty(key, b);
        return this;
    }

    public GsonObject add(final String key, @Nullable final Number number) {
        this.object.addProperty(key, number);
        return this;
    }

    public GsonObject add(final String key, @Nullable final String s) {
        this.object.addProperty(key, s);
        return this;
    }

    public GsonElement remove(final String key) {
        JsonElement previous = this.object.remove(key);
        if (previous == null) return null;
        return GsonElement.wrap(previous);
    }

    public GsonObject clear() {
        this.object.asMap().clear();
        return this;
    }

    public boolean has(final String key) {
        return this.object.has(key);
    }

    public boolean hasObject(final String key) {
        return this.object.has(key) && this.object.get(key).isJsonObject();
    }

    public boolean hasArray(final String key) {
        return this.object.has(key) && this.object.get(key).isJsonArray();
    }

    public boolean hasPrimitive(final String key) {
        return this.object.has(key) && this.object.get(key).isJsonPrimitive();
    }

    public boolean hasBoolean(final String key) {
        return this.object.has(key) && this.object.get(key).isJsonPrimitive() && this.object.get(key).getAsJsonPrimitive().isBoolean();
    }

    public boolean hasNumber(final String key) {
        return this.object.has(key) && this.object.get(key).isJsonPrimitive() && this.object.get(key).getAsJsonPrimitive().isNumber();
    }

    public boolean hasString(final String key) {
        return this.object.has(key) && this.object.get(key).isJsonPrimitive() && this.object.get(key).getAsJsonPrimitive().isString();
    }

    public GsonElement get(final String key) {
        return this.get(key, null);
    }

    public GsonElement get(final String key, @Nullable final GsonElement defaultValue) {
        return this.has(key) ? GsonElement.wrap(this.object.get(key)) : defaultValue;
    }

    public Optional<GsonElement> opt(final String key) {
        return Optional.ofNullable(this.get(key, null));
    }

    @Nonnull
    public GsonElement req(final String key) {
        GsonElement element = this.get(key, null);
        if (element == null) throw new NoSuchElementException("No element found for key: " + key);
        return element;
    }

    public GsonObject getObject(final String key) {
        return this.getObject(key, null);
    }

    public GsonObject getObject(final String key, @Nullable final GsonObject defaultValue) {
        return this.hasObject(key) ? this.get(key, defaultValue).asObject() : defaultValue;
    }

    public Optional<GsonObject> optObject(final String key) {
        return Optional.ofNullable(this.getObject(key, null));
    }

    @Nonnull
    public GsonObject reqObject(final String key) {
        GsonObject object = this.getObject(key, null);
        if (object == null) throw new NoSuchElementException("No object found for key: " + key);
        return object;
    }

    public GsonArray getArray(final String key) {
        return this.getArray(key, null);
    }

    public GsonArray getArray(final String key, @Nullable final GsonArray defaultValue) {
        return this.hasArray(key) ? this.get(key, defaultValue).asArray() : defaultValue;
    }

    public Optional<GsonArray> optArray(final String key) {
        return Optional.ofNullable(this.getArray(key, null));
    }

    @Nonnull
    public GsonArray reqArray(final String key) {
        GsonArray array = this.getArray(key, null);
        if (array == null) throw new NoSuchElementException("No array found for key: " + key);
        return array;
    }

    public GsonPrimitive getPrimitive(final String key) {
        return this.getPrimitive(key, null);
    }

    public GsonPrimitive getPrimitive(final String key, @Nullable final GsonPrimitive defaultValue) {
        return this.hasPrimitive(key) ? this.get(key, defaultValue).asPrimitive() : defaultValue;
    }

    public Optional<GsonPrimitive> optPrimitive(final String key) {
        return Optional.ofNullable(this.getPrimitive(key, null));
    }

    @Nonnull
    public GsonPrimitive reqPrimitive(final String key) {
        GsonPrimitive primitive = this.getPrimitive(key, null);
        if (primitive == null) throw new NoSuchElementException("No primitive found for key: " + key);
        return primitive;
    }

    public boolean getBoolean(final String key) {
        return this.getBoolean(key, false);
    }

    public boolean getBoolean(final String key, final boolean defaultValue) {
        return this.hasBoolean(key) ? this.object.get(key).getAsBoolean() : defaultValue;
    }

    public boolean reqBoolean(final String key) {
        if (!this.hasBoolean(key)) throw new NoSuchElementException("No boolean found for key: " + key);
        return this.getBoolean(key);
    }

    public byte getByte(final String key) {
        return this.getByte(key, (byte) 0);
    }

    public byte getByte(final String key, final byte defaultValue) {
        return this.hasNumber(key) ? this.object.get(key).getAsByte() : defaultValue;
    }

    public byte reqByte(final String key) {
        if (!this.hasNumber(key)) throw new NoSuchElementException("No byte found for key: " + key);
        return this.getByte(key);
    }

    public short getShort(final String key) {
        return this.getShort(key, (short) 0);
    }

    public short getShort(final String key, final short defaultValue) {
        return this.hasNumber(key) ? this.object.get(key).getAsShort() : defaultValue;
    }

    public short reqShort(final String key) {
        if (!this.hasNumber(key)) throw new NoSuchElementException("No short found for key: " + key);
        return this.getShort(key);
    }

    public int getInt(final String key) {
        return this.getInt(key, 0);
    }

    public int getInt(final String key, final int defaultValue) {
        return this.hasNumber(key) ? this.object.get(key).getAsInt() : defaultValue;
    }

    public OptionalInt optInt(final String key) {
        return this.hasNumber(key) ? OptionalInt.of(this.object.get(key).getAsInt()) : OptionalInt.empty();
    }

    public int reqInt(final String key) {
        if (!this.hasNumber(key)) throw new NoSuchElementException("No int found for key: " + key);
        return this.getInt(key);
    }

    public long getLong(final String key) {
        return this.getLong(key, 0);
    }

    public long getLong(final String key, final long defaultValue) {
        return this.hasNumber(key) ? this.object.get(key).getAsLong() : defaultValue;
    }

    public OptionalLong optLong(final String key) {
        return this.hasNumber(key) ? OptionalLong.of(this.object.get(key).getAsLong()) : OptionalLong.empty();
    }

    public long reqLong(final String key) {
        if (!this.hasNumber(key)) throw new NoSuchElementException("No long found for key: " + key);
        return this.getLong(key);
    }

    public float getFloat(final String key) {
        return this.getFloat(key, 0);
    }

    public float getFloat(final String key, final float defaultValue) {
        return this.hasNumber(key) ? this.object.get(key).getAsFloat() : defaultValue;
    }

    public float reqFloat(final String key) {
        if (!this.hasNumber(key)) throw new NoSuchElementException("No float found for key: " + key);
        return this.getFloat(key);
    }

    public double getDouble(final String key) {
        return this.getDouble(key, 0);
    }

    public double getDouble(final String key, final double defaultValue) {
        return this.hasNumber(key) ? this.object.get(key).getAsDouble() : defaultValue;
    }

    public OptionalDouble optDouble(final String key) {
        return this.hasNumber(key) ? OptionalDouble.of(this.object.get(key).getAsDouble()) : OptionalDouble.empty();
    }

    public double reqDouble(final String key) {
        if (!this.hasNumber(key)) throw new NoSuchElementException("No double found for key: " + key);
        return this.getDouble(key);
    }

    public Number getNumber(final String key) {
        return this.getNumber(key, null);
    }

    public Number getNumber(final String key, @Nullable final Number defaultValue) {
        return this.hasNumber(key) ? this.object.get(key).getAsNumber() : defaultValue;
    }

    public Optional<Number> optNumber(final String key) {
        return Optional.ofNullable(this.getNumber(key, null));
    }

    @Nonnull
    public Number reqNumber(final String key) {
        if (!this.hasNumber(key)) throw new NoSuchElementException("No number found for key: " + key);
        return this.getNumber(key);
    }

    public String getString(final String key) {
        return this.getString(key, null);
    }

    public String getString(final String key, @Nullable final String defaultValue) {
        return this.hasString(key) ? this.object.get(key).getAsString() : defaultValue;
    }

    public Optional<String> optString(final String key) {
        return Optional.ofNullable(this.getString(key, null));
    }

    @Nonnull
    public String reqString(final String key) {
        String value = this.getString(key, null);
        if (value == null) throw new NoSuchElementException("No string found for key: " + key);
        return value;
    }

    public int size() {
        return this.object.size();
    }

    public boolean isEmpty() {
        return this.object.isEmpty();
    }

    public Set<String> keySet() {
        return this.object.keySet();
    }

    public Set<Map.Entry<String, GsonElement>> entrySet() {
        return this.object.entrySet().stream().map(WrappedMapEntry::new).collect(Collectors.toSet());
    }

    @Nonnull
    @Override
    public Iterator<Map.Entry<String, GsonElement>> iterator() {
        return new WrappedIterator(this.object.entrySet().iterator());
    }

    public Stream<Map.Entry<String, GsonElement>> stream() {
        return StreamSupport.stream(this.spliterator(), false);
    }

    public Map<String, GsonElement> asMap() {
        Map<String, GsonElement> map = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : this.object.entrySet()) {
            map.put(entry.getKey(), GsonElement.wrap(entry.getValue()));
        }
        return map;
    }


    private static class WrappedMapEntry implements Map.Entry<String, GsonElement> {
        private final Map.Entry<String, JsonElement> entry;

        public WrappedMapEntry(final Map.Entry<String, JsonElement> entry) {
            this.entry = entry;
        }

        @Override
        public String getKey() {
            return this.entry.getKey();
        }

        @Override
        public GsonElement getValue() {
            return GsonElement.wrap(this.entry.getValue());
        }

        @Override
        public GsonElement setValue(GsonElement value) {
            JsonElement previous = this.entry.setValue(value.getJsonElement());
            if (previous == null) return null;
            return GsonElement.wrap(previous);
        }
    }

    private static class WrappedIterator implements Iterator<Map.Entry<String, GsonElement>> {
        private final Iterator<Map.Entry<String, JsonElement>> iterator;

        public WrappedIterator(final Iterator<Map.Entry<String, JsonElement>> iterator) {
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext() {
            return this.iterator.hasNext();
        }

        @Override
        public Map.Entry<String, GsonElement> next() {
            return new WrappedMapEntry(this.iterator.next());
        }

        @Override
        public void remove() {
            this.iterator.remove();
        }
    }

}
