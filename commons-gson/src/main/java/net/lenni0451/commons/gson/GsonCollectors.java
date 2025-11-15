package net.lenni0451.commons.gson;

import net.lenni0451.commons.gson.elements.GsonArray;
import net.lenni0451.commons.gson.elements.GsonElement;
import net.lenni0451.commons.gson.elements.GsonObject;

import java.util.function.Function;
import java.util.stream.Collector;

public class GsonCollectors {

    public static <T extends GsonElement> Collector<T, ?, GsonArray> toArray() {
        return Collector.of(
                GsonArray::new,
                GsonArray::add,
                (elements, other) -> {
                    if (elements.isEmpty()) return other;
                    if (other.isEmpty()) return elements;
                    GsonArray result = new GsonArray();
                    result.addAll(elements);
                    result.addAll(other);
                    return result;
                }
        );
    }

    public static <P, V extends GsonElement> Collector<P, ?, GsonObject> toObject(final Function<P, String> keyMapper, final Function<P, V> valueMapper) {
        return Collector.of(
                GsonObject::new,
                (entries, p) -> entries.add(keyMapper.apply(p), valueMapper.apply(p)),
                (entries, entries2) -> {
                    if (entries.isEmpty()) return entries2;
                    if (entries2.isEmpty()) return entries;
                    GsonObject result = new GsonObject();
                    result.addAll(entries);
                    result.addAll(entries2);
                    return result;
                }
        );
    }

}
