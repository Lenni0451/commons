package net.lenni0451.commons.collections;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class MapOps {

    /**
     * Recursively put all values from other into map.<br>
     * This method recursively checks all values that are maps too.
     *
     * @param map   The map to put the values into
     * @param other The map to get the values from
     */
    public static void recursivePutAll(final Map<?, ?> map, final Map<?, ?> other) {
        recursivePutAll(map, other, Map.class::isInstance, Map.class::cast);
    }

    /**
     * Recursively put all values from other into map.<br>
     * This method recursively checks all values that are maps too.
     *
     * @param map           The map to put the values into
     * @param other         The map to get the values from
     * @param valueMapCheck A function to check if a value is a map
     * @param valueToMap    A function to cast a value to a map
     * @param <V>           The type of the casted values
     */
    public static <V> void recursivePutAll(final Map<?, ?> map, final Map<?, ?> other, final Function<Object, Boolean> valueMapCheck, final Function<V, Map<?, ?>> valueToMap) {
        for (Map.Entry<?, ?> entry : other.entrySet()) {
            if (map.containsKey(entry.getKey())) {
                Object rawValue1 = map.get(entry.getKey());
                Object rawValue2 = entry.getValue();
                if (valueMapCheck.apply(rawValue1) && valueMapCheck.apply(rawValue2)) {
                    recursivePutAll(valueToMap.apply((V) map.get(entry.getKey())), valueToMap.apply((V) entry.getValue()), valueMapCheck, valueToMap);
                } else {
                    uncheckedPut(map, entry.getKey(), entry.getValue());
                }
            } else {
                uncheckedPut(map, entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Get all duplicate values from map 1 that are also in map 2.<br>
     * This method recursively checks all values that are maps too.
     *
     * @param map1 The map to check
     * @param map2 The map to check against
     * @return A map with all duplicate values
     */
    public static Map<?, ?> getDuplicates(final Map<?, ?> map1, final Map<?, ?> map2) {
        return getDuplicates(map1, map2, Map.class::isInstance, Map.class::cast);
    }

    /**
     * Get all duplicate values from map 1 that are also in map 2.<br>
     * This method recursively checks all values that are maps too.
     *
     * @param map1          The map to check
     * @param map2          The map to check against
     * @param valueMapCheck A function to check if a value is a map
     * @param valueToMap    A function to cast a value to a map
     * @param <V>           The type of the casted values
     * @return A map with all duplicate values
     */
    public static <V> Map<?, ?> getDuplicates(final Map<?, ?> map1, final Map<?, ?> map2, final Function<Object, Boolean> valueMapCheck, final Function<V, Map<?, ?>> valueToMap) {
        Map<Object, Object> duplicates = new HashMap<>();
        for (Map.Entry<?, ?> entry : map1.entrySet()) {
            if (!map2.containsKey(entry.getKey())) continue;
            if (map2.get(entry.getKey()).equals(entry.getValue())) {
                duplicates.put(entry.getKey(), entry.getValue());
            } else {
                Object rawValue1 = entry.getValue();
                Object rawValue2 = map2.get(entry.getKey());
                if (valueMapCheck.apply(rawValue1) && valueMapCheck.apply(rawValue2)) {
                    Map<?, ?> subDuplicates = getDuplicates(valueToMap.apply((V) entry.getValue()), valueToMap.apply((V) map2.get(entry.getKey())), valueMapCheck, valueToMap);
                    if (!subDuplicates.isEmpty()) duplicates.put(entry.getKey(), subDuplicates);
                }
            }
        }
        return duplicates;
    }

    /**
     * Get all unique values from map 1 that are not in map 2.<br>
     * This method recursively checks all values that are maps too.
     *
     * @param map   The map to check
     * @param other The map to check against
     * @return A map with all unique values
     */
    public static Map<?, ?> getUniques(final Map<?, ?> map, final Map<?, ?> other) {
        return getUniques(map, other, Map.class::isInstance, Map.class::cast);
    }

    /**
     * Get all unique values from map 1 that are not in map 2.<br>
     * This method recursively checks all values that are maps too.
     *
     * @param map           The map to check
     * @param other         The map to check against
     * @param valueMapCheck A function to check if a value is a map
     * @param valueToMap    A function to cast a value to a map
     * @param <V>           The type of the casted values
     * @return A map with all unique values
     */
    public static <V> Map<?, ?> getUniques(final Map<?, ?> map, final Map<?, ?> other, final Function<Object, Boolean> valueMapCheck, final Function<V, Map<?, ?>> valueToMap) {
        Map<Object, Object> uniques = new HashMap<>();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (!other.containsKey(entry.getKey())) {
                uniques.put(entry.getKey(), entry.getValue());
            } else {
                Object rawValue1 = entry.getValue();
                Object rawValue2 = other.get(entry.getKey());
                if (valueMapCheck.apply(rawValue1) && valueMapCheck.apply(rawValue2)) {
                    Map<?, ?> subUniques = getUniques(valueToMap.apply((V) entry.getValue()), valueToMap.apply((V) other.get(entry.getKey())), valueMapCheck, valueToMap);
                    if (!subUniques.isEmpty()) uniques.put(entry.getKey(), subUniques);
                } else {
                    if (!rawValue1.equals(rawValue2)) uniques.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return uniques;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void uncheckedPut(final Map map, final Object key, final Object value) {
        map.put(key, value);
    }

}
