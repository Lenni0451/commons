package net.lenni0451.commons.arrays.wrapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArrayWrapperTest {

    private static final String[] ARRAY = {"a", "b", "c", "d", "e"};
    private static final String[] SUB_ARRAY = {"c", "d", "e"};

    private StringArrayWrapper arrayWrapper;

    @BeforeEach
    void init() {
        this.arrayWrapper = new StringArrayWrapper(ARRAY.clone());
    }

    @Test
    void getArray() {
        assertArrayEquals(ARRAY, this.arrayWrapper.getArray());
    }

    @Test
    void length() {
        assertEquals(ARRAY.length, this.arrayWrapper.length());
    }

    @Test
    void isEmpty() {
        assertFalse(this.arrayWrapper.isEmpty());
        assertTrue(new StringArrayWrapper(new String[0]).isEmpty());
    }

    @Test
    void isNotEmpty() {
        assertTrue(this.arrayWrapper.isNotEmpty());
        assertFalse(new StringArrayWrapper(new String[0]).isNotEmpty());
    }

    @Test
    void containsIndex() {
        for (int i = 0; i < ARRAY.length * 2; i++) {
            assertEquals(i < ARRAY.length, this.arrayWrapper.contains(i));
        }
    }

    @Test
    void contains() {
        for (String s : ARRAY) assertTrue(this.arrayWrapper.contains(s));
        assertFalse(this.arrayWrapper.contains("f"));
    }

    @Test
    void getIndex() {
        for (int i = 0; i < ARRAY.length; i++) assertEquals(ARRAY[i], this.arrayWrapper.get(i));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> this.arrayWrapper.get(ARRAY.length));
    }

    @Test
    void getDefault() {
        for (int i = 0; i < ARRAY.length; i++) assertEquals(ARRAY[i], this.arrayWrapper.get(i, "f"));
        assertEquals("f", this.arrayWrapper.get(ARRAY.length, "f"));
    }

    @Test
    void set() {
        for (int i = 0; i < ARRAY.length; i++) {
            this.arrayWrapper.set(i, "f");
            assertEquals("f", this.arrayWrapper.get(i));
        }
    }

    @Test
    void add() {
        this.arrayWrapper.add("f");
        assertEquals("f", this.arrayWrapper.get(ARRAY.length));
    }

    @Test
    void addAll() {
        String[] added = {"f", "g", "h"};
        this.arrayWrapper.addAll(added);
        for (int i = 0; i < ARRAY.length; i++) assertEquals(ARRAY[i], this.arrayWrapper.get(i));
        for (int i = 0; i < added.length; i++) assertEquals(added[i], this.arrayWrapper.get(ARRAY.length + i));
    }

    @Test
    void addAllWrapper() {
        StringArrayWrapper added = new StringArrayWrapper(new String[]{"f", "g", "h"});
        this.arrayWrapper.addAll(added);
        for (int i = 0; i < ARRAY.length; i++) assertEquals(ARRAY[i], this.arrayWrapper.get(i));
        for (int i = 0; i < added.length(); i++) assertEquals(added.get(i), this.arrayWrapper.get(ARRAY.length + i));
    }

    @Test
    void removeIndex() {
        for (String s : ARRAY) {
            assertEquals(s, this.arrayWrapper.get(0));
            this.arrayWrapper.remove(0);
        }
    }

    @Test
    void removeValue() {
        for (String s : ARRAY) {
            this.arrayWrapper.remove(s);
            assertFalse(this.arrayWrapper.contains(s));
        }
    }

    @Test
    void sliceStart() {
        assertArrayEquals(SUB_ARRAY, this.arrayWrapper.slice(2));
    }

    @Test
    void sliceStartEnd() {
        assertArrayEquals(SUB_ARRAY, this.arrayWrapper.slice(2, 5));
    }

}
