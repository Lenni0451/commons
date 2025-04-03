package net.lenni0451.commons.collections;

import net.lenni0451.commons.collections.collections.DelegateCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DelegateCollectionTest {

    private List<String> list;
    private DelegateCollection<String> delegate;

    @BeforeEach
    void setUp() {
        this.list = new ArrayList<>(Arrays.asList("Test1", "Test2", "Test3"));
        this.delegate = new DelegateCollection<>(this.list);
    }

    @Test
    void size() {
        assertEquals(this.list.size(), this.delegate.size());
    }

    @Test
    void isEmpty() {
        assertEquals(this.list.isEmpty(), this.delegate.isEmpty());
    }

    @Test
    void contains() {
        assertEquals(this.list.contains("Test1"), this.delegate.contains("Test1"));
        assertEquals(this.list.contains("Test10"), this.delegate.contains("Test10"));
    }

    @Test
    void iterator() {
        assertEquals(this.list.iterator().hasNext(), this.delegate.iterator().hasNext());
    }

    @Test
    void toArray() {
        assertArrayEquals(this.list.toArray(), this.delegate.toArray());
    }

    @Test
    void testToArray() {
        assertArrayEquals(this.list.toArray(new String[0]), this.delegate.toArray(new String[0]));
    }

    @Test
    void add() {
        this.delegate.add("Test4");
        assertEquals(this.list.size(), this.delegate.size());
        assertEquals(this.list.contains("Test4"), this.delegate.contains("Test4"));
    }

    @Test
    void remove() {
        this.delegate.remove("Test1");
        assertEquals(this.list.size(), this.delegate.size());
        assertEquals(this.list.contains("Test1"), this.delegate.contains("Test1"));
        assertEquals(this.list.contains("Test2"), this.delegate.contains("Test2"));
        assertEquals(this.list.contains("Test3"), this.delegate.contains("Test3"));
    }

    @Test
    void containsAll() {
        assertEquals(this.list.containsAll(Arrays.asList("Test1", "Test2")), this.delegate.containsAll(Arrays.asList("Test1", "Test2")));
        assertEquals(this.list.containsAll(Arrays.asList("Test1", "Test10")), this.delegate.containsAll(Arrays.asList("Test1", "Test10")));
    }

    @Test
    void addAll() {
        this.delegate.addAll(Arrays.asList("Test4", "Test5"));
        assertEquals(this.list.size(), this.delegate.size());
        assertEquals(this.list.size(), this.delegate.size());
        assertEquals(this.list.contains("Test1"), this.delegate.contains("Test1"));
        assertEquals(this.list.contains("Test2"), this.delegate.contains("Test2"));
        assertEquals(this.list.contains("Test3"), this.delegate.contains("Test3"));
    }

    @Test
    void removeAll() {
        this.delegate.removeAll(Arrays.asList("Test1", "Test2"));
        assertEquals(this.list.size(), this.delegate.size());
        assertEquals(this.list.size(), this.delegate.size());
        assertEquals(this.list.contains("Test1"), this.delegate.contains("Test1"));
        assertEquals(this.list.contains("Test2"), this.delegate.contains("Test2"));
        assertEquals(this.list.contains("Test3"), this.delegate.contains("Test3"));
    }

    @Test
    void retainAll() {
        this.delegate.retainAll(Arrays.asList("Test1", "Test2"));
        assertEquals(this.list.size(), this.delegate.size());
        assertEquals(this.list.contains("Test1"), this.delegate.contains("Test1"));
        assertEquals(this.list.contains("Test2"), this.delegate.contains("Test2"));
        assertEquals(this.list.contains("Test3"), this.delegate.contains("Test3"));
    }

    @Test
    void clear() {
        this.delegate.clear();
        assertEquals(this.list.size(), this.delegate.size());
        assertEquals(this.list.isEmpty(), this.delegate.isEmpty());
    }

}
