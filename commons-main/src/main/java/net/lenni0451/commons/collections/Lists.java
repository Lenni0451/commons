package net.lenni0451.commons.collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public class Lists {

    /**
     * Move the given item to the bottom of the list.
     *
     * @param list  The list to move the item in
     * @param input The item to move
     * @param <T>   The item type
     * @return The list
     */
    public static <T> List<T> moveToBottom(final List<T> list, final T input) {
        if (list.size() > 1 && list.indexOf(input) < list.size() - 1) {
            list.remove(input);
            list.add(list.size(), input);
        }
        return list;
    }

    /**
     * Create a new list with the given objects.
     *
     * @param listSupplier The supplier for the list
     * @param objects      The objects to add to the list
     * @param <T>          The list type
     * @param <O>          The object type
     * @return The created list
     */
    @SafeVarargs
    public static <T extends List<O>, O> T any(final Supplier<T> listSupplier, final O... objects) {
        T list = listSupplier.get();
        Collections.addAll(list, objects);
        return list;
    }

    /**
     * Create a new list which is passed to the given consumer.
     *
     * @param listSupplier The supplier to create the list
     * @param listConsumer The consumer to pass the list to
     * @param <T>          The list type
     * @param <O>          The object type
     * @return The created list
     */
    public static <T extends List<O>, O> T any(final Supplier<T> listSupplier, final Consumer<T> listConsumer) {
        T list = listSupplier.get();
        listConsumer.accept(list);
        return list;
    }

    /**
     * Create a new {@link ArrayList} with the given objects.
     *
     * @param objects The objects to add to the list
     * @param <O>     The object type
     * @return The created list
     */
    @SafeVarargs
    public static <O> ArrayList<O> arrayList(final O... objects) {
        return any(ArrayList::new, objects);
    }

    /**
     * Create a new {@link ArrayList} which is passed to the given consumer.
     *
     * @param listConsumer The consumer to pass the list to
     * @param <O>          The object type
     * @return The created list
     */
    public static <O> ArrayList<O> arrayList(final Consumer<ArrayList<O>> listConsumer) {
        return any(ArrayList::new, listConsumer);
    }

    /**
     * Create a new {@link CopyOnWriteArrayList} with the given objects.
     *
     * @param objects The objects to add to the list
     * @param <O>     The object type
     * @return The created list
     */
    @SafeVarargs
    public static <O> CopyOnWriteArrayList<O> copyOnWriteArrayList(final O... objects) {
        return any(CopyOnWriteArrayList::new, objects);
    }

    /**
     * Create a new {@link CopyOnWriteArrayList} which is passed to the given consumer.
     *
     * @param listConsumer The consumer to pass the list to
     * @param <O>          The object type
     * @return The created list
     */
    public static <O> CopyOnWriteArrayList<O> copyOnWriteArrayList(final Consumer<CopyOnWriteArrayList<O>> listConsumer) {
        return any(CopyOnWriteArrayList::new, listConsumer);
    }

}
