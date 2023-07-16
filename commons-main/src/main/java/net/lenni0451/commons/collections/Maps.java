package net.lenni0451.commons.collections;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Maps {

    /**
     * Sort a map using the given comparator.<br>
     * This will return a {@link LinkedHashMap} to keep the order of the map.
     *
     * @param map        The map to sort
     * @param comparator The comparator to use
     * @param <K>        The key type
     * @param <V>        The value type
     * @return The sorted map
     */
    public static <K, V> LinkedHashMap<K, V> sort(final Map<K, V> map, final Comparator<Map.Entry<K, V>> comparator) {
        return map.entrySet().stream()
                .sorted(comparator)
                .collect(Collectors.<Map.Entry<K, V>, K, V, LinkedHashMap<K, V>>toMap(Map.Entry::getKey, Map.Entry::getValue, (k, v) -> {
                    throw new IllegalStateException("Duplicate key '" + k + "' (" + v + ")");
                }, LinkedHashMap::new));
    }

    /**
     * Create a new map with the given objects.<br>
     * The objects must be in the format: {@code key, value, key, value, ...}<br>
     * The types of the keys and values are not checked.
     *
     * @param mapSupplier The supplier to create the map
     * @param objects     The objects to add to the map
     * @param <T>         The map type
     * @param <K>         The key type
     * @param <V>         The value type
     * @return The created map
     */
    @SuppressWarnings("unchecked")
    public static <T extends Map<K, V>, K, V> T any(final Supplier<T> mapSupplier, final Object... objects) {
        if (objects.length % 2 != 0) throw new IllegalArgumentException("Uneven object count");

        T map = mapSupplier.get();
        for (int i = 0; i < objects.length; i += 2) map.put((K) objects[i], (V) objects[i + 1]);
        return map;
    }

    /**
     * Create a new map which is passed to the given consumer.
     *
     * @param mapSupplier The supplier to create the map
     * @param mapConsumer The consumer to pass the map to
     * @param <T>         The map type
     * @param <K>         The key type
     * @param <V>         The value type
     * @return The created map
     */
    public static <T extends Map<K, V>, K, V> T any(final Supplier<T> mapSupplier, final Consumer<T> mapConsumer) {
        T map = mapSupplier.get();
        mapConsumer.accept(map);
        return map;
    }

    /**
     * Create a new {@link HashMap} with the given objects.<br>
     * The objects must be in the format: {@code key, value, key, value, ...}<br>
     * The types of the keys and values are not checked.
     *
     * @param objects The objects to add to the map
     * @param <K>     The key type
     * @param <V>     The value type
     * @return The created map
     */
    public static <K, V> HashMap<K, V> hashMap(final Object... objects) {
        return any(HashMap::new, objects);
    }

    /**
     * Create a new {@link HashMap} which is passed to the given consumer.
     *
     * @param mapConsumer The consumer to pass the map to
     * @param <K>         The key type
     * @param <V>         The value type
     * @return The created map
     */
    public static <K, V> HashMap<K, V> hashMap(final Consumer<HashMap<K, V>> mapConsumer) {
        return any(HashMap::new, mapConsumer);
    }

    /**
     * Create a new {@link LinkedHashMap} with the given objects.<br>
     * The objects must be in the format: {@code key, value, key, value, ...}<br>
     * The types of the keys and values are not checked.
     *
     * @param objects The objects to add to the map
     * @param <K>     The key type
     * @param <V>     The value type
     * @return The created map
     */
    public static <K, V> LinkedHashMap<K, V> linkedHashMap(final Object... objects) {
        return any(LinkedHashMap::new, objects);
    }

    /**
     * Create a new {@link LinkedHashMap} which is passed to the given consumer.
     *
     * @param mapConsumer The consumer to pass the map to
     * @param <K>         The key type
     * @param <V>         The value type
     * @return The created map
     */
    public static <K, V> LinkedHashMap<K, V> linkedHashMap(final Consumer<LinkedHashMap<K, V>> mapConsumer) {
        return any(LinkedHashMap::new, mapConsumer);
    }

    /**
     * Create a new {@link ConcurrentHashMap} with the given objects.<br>
     * The objects must be in the format: {@code key, value, key, value, ...}<br>
     * The types of the keys and values are not checked.
     *
     * @param objects The objects to add to the map
     * @param <K>     The key type
     * @param <V>     The value type
     * @return The created map
     */
    public static <K, V> ConcurrentHashMap<K, V> concurrentHashMap(final Object... objects) {
        return any(ConcurrentHashMap::new, objects);
    }

    /**
     * Create a new {@link ConcurrentHashMap} which is passed to the given consumer.
     *
     * @param mapConsumer The consumer to pass the map to
     * @param <K>         The key type
     * @param <V>         The value type
     * @return The created map
     */
    public static <K, V> ConcurrentHashMap<K, V> concurrentHashMap(final Consumer<ConcurrentHashMap<K, V>> mapConsumer) {
        return any(ConcurrentHashMap::new, mapConsumer);
    }

}
