package net.lenni0451.commons.collections.sets;

import net.lenni0451.commons.collections.Sets;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DelegateSetTest {

    private Set<String> set;
    private DelegateSet<String> delegate;

    @BeforeEach
    void setUp() {
        this.set = Sets.hashSet("Test1", "Test2", "Test3");
        this.delegate = new DelegateSet<>(this.set);
    }

    @Test
    void size() {
        assertEquals(this.set.size(), this.delegate.size());
    }

    @Test
    void isEmpty() {
        assertEquals(this.set.isEmpty(), this.delegate.isEmpty());
    }

    @Test
    void contains() {
        Assertions.assertEquals(this.set.contains("Test1"), this.delegate.contains("Test1"));
        Assertions.assertEquals(this.set.contains("Test10"), this.delegate.contains("Test10"));
    }

    @Test
    void iterator() {
        Assertions.assertEquals(this.set.iterator().hasNext(), this.delegate.iterator().hasNext());
    }

    @Test
    void toArray() {
        Assertions.assertArrayEquals(this.set.toArray(), this.delegate.toArray());
    }

    @Test
    void testToArray() {
        Assertions.assertArrayEquals(this.set.toArray(new String[0]), this.delegate.toArray(new String[0]));
    }

    @Test
    void add() {
        this.delegate.add("Test4");
        assertEquals(this.set.size(), this.delegate.size());
        assertEquals(this.set.contains("Test4"), this.delegate.contains("Test4"));
    }

    @Test
    void remove() {
        this.delegate.remove("Test1");
        assertEquals(this.set.size(), this.delegate.size());
        assertEquals(this.set.contains("Test1"), this.delegate.contains("Test1"));
        assertEquals(this.set.contains("Test2"), this.delegate.contains("Test2"));
        assertEquals(this.set.contains("Test3"), this.delegate.contains("Test3"));
    }

    @Test
    void containsAll() {
        assertEquals(this.set.containsAll(Sets.hashSet("Test1", "Test2")), this.delegate.containsAll(Sets.hashSet("Test1", "Test2")));
        assertEquals(this.set.containsAll(Sets.hashSet("Test1", "Test10")), this.delegate.containsAll(Sets.hashSet("Test1", "Test10")));
    }

    @Test
    void addAll() {
        this.delegate.addAll(Sets.hashSet("Test4", "Test5"));
        assertEquals(this.set.size(), this.delegate.size());
        assertEquals(this.set.contains("Test4"), this.delegate.contains("Test4"));
        assertEquals(this.set.contains("Test5"), this.delegate.contains("Test5"));
    }

    @Test
    void retainAll() {
        this.delegate.retainAll(Sets.hashSet("Test1", "Test2"));
        assertEquals(this.set.size(), this.delegate.size());
        assertEquals(this.set.contains("Test1"), this.delegate.contains("Test1"));
        assertEquals(this.set.contains("Test2"), this.delegate.contains("Test2"));
        assertEquals(this.set.contains("Test3"), this.delegate.contains("Test3"));
    }

    @Test
    void removeAll() {
        this.delegate.removeAll(Sets.hashSet("Test1", "Test2"));
        assertEquals(this.set.size(), this.delegate.size());
        assertEquals(this.set.contains("Test1"), this.delegate.contains("Test1"));
        assertEquals(this.set.contains("Test2"), this.delegate.contains("Test2"));
        assertEquals(this.set.contains("Test3"), this.delegate.contains("Test3"));
    }

    @Test
    void clear() {
        this.delegate.clear();
        assertEquals(this.set.size(), this.delegate.size());
        assertEquals(this.set.contains("Test1"), this.delegate.contains("Test1"));
        assertEquals(this.set.contains("Test2"), this.delegate.contains("Test2"));
        assertEquals(this.set.contains("Test3"), this.delegate.contains("Test3"));
    }

}
