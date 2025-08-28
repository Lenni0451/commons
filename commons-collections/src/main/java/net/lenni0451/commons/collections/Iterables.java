package net.lenni0451.commons.collections;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

@UtilityClass
public class Iterables {

    /**
     * Convert an iterable to a collection.<br>
     * If the iterable is already a collection, it will be returned as is.
     *
     * @param iterable The iterable to convert to a collection
     * @param <T>      The type of the elements in the iterable
     * @return A collection containing all elements of the iterable
     */
    public static <T> Collection<T> toCollection(final Iterable<T> iterable) {
        if (iterable instanceof Collection) {
            return (Collection<T>) iterable;
        } else {
            Collection<T> collection = new ArrayList<>();
            iterable.forEach(collection::add);
            return collection;
        }
    }

    /**
     * Map an iterable to a list using the provided mapper function.
     *
     * @param iterable The iterable to map
     * @param mapper   The function to map each element of the iterable
     * @param <F>      The type of the elements in the iterable
     * @param <T>      The type of the elements in the resulting list
     * @return A list containing the mapped elements
     */
    public static <F, T> List<T> map(final Iterable<F> iterable, final Function<F, T> mapper) {
        List<T> list = new ArrayList<>();
        iterable.forEach(f -> list.add(mapper.apply(f)));
        return list;
    }

}
