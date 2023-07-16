package net.lenni0451.commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ArrayUtilsTest {

    private static final byte[] ARRAY = new byte[]{1, 2, 3, 4, 5, 5};

    @Test
    void indexOf() {
        for (int i = 0; i < ARRAY.length - 2; i++) assertEquals(i, ArrayUtils.indexOf(ARRAY, ARRAY[i]));
        assertEquals(4, ArrayUtils.indexOf(ARRAY, (byte) 5));
        assertEquals(-1, ArrayUtils.indexOf(ARRAY, (byte) 6));
    }

    @Test
    void indexOfLast() {
        for (int i = 0; i < ARRAY.length - 2; i++) assertEquals(i, ArrayUtils.indexOfLast(ARRAY, ARRAY[i]));
        assertEquals(5, ArrayUtils.indexOfLast(ARRAY, (byte) 5));
        assertEquals(-1, ArrayUtils.indexOfLast(ARRAY, (byte) 6));
    }

    @Test
    void indexOfArray() {
        byte[] bytes2 = new byte[]{3, 4};
        byte[] bytes3 = new byte[]{5, 5};
        byte[] bytes4 = new byte[]{6, 6};
        assertEquals(2, ArrayUtils.indexOf(ARRAY, bytes2));
        assertEquals(4, ArrayUtils.indexOf(ARRAY, bytes3));
        assertEquals(-1, ArrayUtils.indexOf(ARRAY, bytes4));
    }

    @Test
    void addLast() {
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5, 5, 6}, ArrayUtils.add(ARRAY, (byte) 6));
    }

    @Test
    void prepend() {
        assertArrayEquals(new byte[]{6, 1, 2, 3, 4, 5, 5}, ArrayUtils.prepend(ARRAY, (byte) 6));
    }

    @Test
    void addIndex() {
        assertArrayEquals(new byte[]{1, 2, 3, 4, 6, 5, 5}, ArrayUtils.add(ARRAY, 4, (byte) 6));
    }

    @Test
    void addLastBytes() {
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5, 5, 6, 7, 8}, ArrayUtils.add(ARRAY, new byte[]{6, 7, 8}));
    }

    @Test
    void prependBytes() {
        assertArrayEquals(new byte[]{6, 7, 8, 1, 2, 3, 4, 5, 5}, ArrayUtils.prepend(ARRAY, new byte[]{6, 7, 8}));
    }

    @Test
    void addBytesIndex() {
        assertArrayEquals(new byte[]{1, 2, 3, 4, 6, 7, 8, 5, 5}, ArrayUtils.add(ARRAY, 4, new byte[]{6, 7, 8}));
    }

    @Test
    void addArrays() {
        byte[] bytes1 = new byte[]{3, 4};
        byte[] bytes2 = new byte[]{5, 6};
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5, 5, 3, 4, 5, 6}, ArrayUtils.add(ARRAY, bytes1, bytes2));
    }

    @Test
    void prependArrays() {
        byte[] bytes1 = new byte[]{3, 4};
        byte[] bytes2 = new byte[]{5, 6};
        assertArrayEquals(new byte[]{3, 4, 5, 6, 1, 2, 3, 4, 5, 5}, ArrayUtils.prepend(ARRAY, bytes1, bytes2));
    }

    @Test
    void addArraysIndex() {
        byte[] bytes1 = new byte[]{3, 4};
        byte[] bytes2 = new byte[]{5, 6};
        assertArrayEquals(new byte[]{1, 2, 3, 4, 3, 4, 5, 6, 5, 5}, ArrayUtils.add(ARRAY, 4, bytes1, bytes2));
    }

    @Test
    void remove() {
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, ArrayUtils.remove(ARRAY, (byte) 5));
    }

    @Test
    void removeFirst() {
        assertArrayEquals(new byte[]{2, 3, 4, 5, 5}, ArrayUtils.removeFirst(ARRAY));
    }

    @Test
    void removeLast() {
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, ArrayUtils.removeLast(ARRAY));
    }

    @Test
    void removeAt() {
        assertArrayEquals(new byte[]{1, 2, 3, 5, 5}, ArrayUtils.removeAt(ARRAY, 3));
    }

    @Test
    void removeAtLength() {
        assertArrayEquals(new byte[]{1, 4, 5, 5}, ArrayUtils.removeAt(ARRAY, 1, 2));
    }

}
