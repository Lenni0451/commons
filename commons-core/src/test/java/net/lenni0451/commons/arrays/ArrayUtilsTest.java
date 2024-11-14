package net.lenni0451.commons.arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArrayUtilsTest {

    private byte[] array;

    @BeforeEach
    void setUp() {
        this.array = new byte[]{1, 2, 3, 4, 5, 5};
    }

    @Test
    void contains() {
        assertTrue(ArrayUtils.contains(this.array, (byte) 5));
        assertFalse(ArrayUtils.contains(this.array, (byte) 6));
    }

    @Test
    void indexOf() {
        for (int i = 0; i < this.array.length - 2; i++) assertEquals(i, ArrayUtils.indexOf(this.array, this.array[i]));
        assertEquals(4, ArrayUtils.indexOf(this.array, (byte) 5));
        assertEquals(-1, ArrayUtils.indexOf(this.array, (byte) 6));
    }

    @Test
    void indexOfLast() {
        for (int i = 0; i < this.array.length - 2; i++) assertEquals(i, ArrayUtils.indexOfLast(this.array, this.array[i]));
        assertEquals(5, ArrayUtils.indexOfLast(this.array, (byte) 5));
        assertEquals(-1, ArrayUtils.indexOfLast(this.array, (byte) 6));
    }

    @Test
    void indexOfArray() {
        byte[] bytes2 = new byte[]{3, 4};
        byte[] bytes3 = new byte[]{5, 5};
        byte[] bytes4 = new byte[]{6, 6};
        assertEquals(2, ArrayUtils.indexOf(this.array, bytes2));
        assertEquals(4, ArrayUtils.indexOf(this.array, bytes3));
        assertEquals(-1, ArrayUtils.indexOf(this.array, bytes4));
    }

    @Test
    void addLast() {
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5, 5, 6}, ArrayUtils.add(this.array, (byte) 6));
    }

    @Test
    void prepend() {
        assertArrayEquals(new byte[]{6, 1, 2, 3, 4, 5, 5}, ArrayUtils.prepend(this.array, (byte) 6));
    }

    @Test
    void addIndex() {
        assertArrayEquals(new byte[]{1, 2, 3, 4, 6, 5, 5}, ArrayUtils.add(this.array, 4, (byte) 6));
    }

    @Test
    void addLastBytes() {
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5, 5, 6, 7, 8}, ArrayUtils.add(this.array, new byte[]{6, 7, 8}));
    }

    @Test
    void prependBytes() {
        assertArrayEquals(new byte[]{6, 7, 8, 1, 2, 3, 4, 5, 5}, ArrayUtils.prepend(this.array, new byte[]{6, 7, 8}));
    }

    @Test
    void addBytesIndex() {
        assertArrayEquals(new byte[]{1, 2, 3, 4, 6, 7, 8, 5, 5}, ArrayUtils.add(this.array, 4, new byte[]{6, 7, 8}));
    }

    @Test
    void addArrays() {
        byte[] bytes1 = new byte[]{3, 4};
        byte[] bytes2 = new byte[]{5, 6};
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5, 5, 3, 4, 5, 6}, ArrayUtils.add(this.array, bytes1, bytes2));
    }

    @Test
    void prependArrays() {
        byte[] bytes1 = new byte[]{3, 4};
        byte[] bytes2 = new byte[]{5, 6};
        assertArrayEquals(new byte[]{3, 4, 5, 6, 1, 2, 3, 4, 5, 5}, ArrayUtils.prepend(this.array, bytes1, bytes2));
    }

    @Test
    void addArraysIndex() {
        byte[] bytes1 = new byte[]{3, 4};
        byte[] bytes2 = new byte[]{5, 6};
        assertArrayEquals(new byte[]{1, 2, 3, 4, 3, 4, 5, 6, 5, 5}, ArrayUtils.add(this.array, 4, bytes1, bytes2));
    }

    @Test
    void remove() {
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, ArrayUtils.remove(this.array, (byte) 5));
    }

    @Test
    void removeFirst() {
        assertArrayEquals(new byte[]{2, 3, 4, 5, 5}, ArrayUtils.removeFirst(this.array));
    }

    @Test
    void removeLast() {
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, ArrayUtils.removeLast(this.array));
    }

    @Test
    void removeAt() {
        assertArrayEquals(new byte[]{1, 2, 3, 5, 5}, ArrayUtils.removeAt(this.array, 3));
    }

    @Test
    void removeAtLength() {
        assertArrayEquals(new byte[]{1, 4, 5, 5}, ArrayUtils.removeAt(this.array, 1, 2));
    }

    @Test
    void reverse() {
        ArrayUtils.reverse(this.array);
        assertArrayEquals(new byte[]{5, 5, 4, 3, 2, 1}, this.array);
    }

    @Test
    void testObjectArrays() {
        String[] strings = new String[]{"a", "b", "c", "d"};
        strings = ArrayUtils.add(strings, "e");
        strings = ArrayUtils.add(strings, "f", "g");
        strings = ArrayUtils.add(strings, new String[]{"h", "i"}, new String[]{"j", "k"});
        strings = ArrayUtils.removeAt(strings, 2, 3);
        assertArrayEquals(new String[]{"a", "b", "f", "g", "h", "i", "j", "k"}, strings);
    }

}
