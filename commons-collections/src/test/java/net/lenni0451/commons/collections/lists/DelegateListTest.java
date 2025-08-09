package net.lenni0451.commons.collections.lists;

import net.lenni0451.commons.collections.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DelegateListTest {

    private List<String> list;
    private DelegateList<String> delegate;

    @BeforeEach
    void setUp() {
        this.list = Lists.arrayList("Test1", "Test2", "Test3");
        this.delegate = new DelegateList<>(this.list);
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
        assertEquals(this.list.contains("Test4"), this.delegate.contains("Test4"));
        assertEquals(this.list.contains("Test5"), this.delegate.contains("Test5"));
    }

    @Test
    void testAddAll() {
        this.delegate.addAll(1, Arrays.asList("Test6", "Test7"));
        assertEquals(this.list.size(), this.delegate.size());
        assertEquals(this.list.get(1), this.delegate.get(1));
        assertEquals(this.list.get(2), this.delegate.get(2));
    }

    @Test
    void removeAll() {
        this.delegate.removeAll(Arrays.asList("Test1", "Test2"));
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

    @Test
    void get() {
        assertEquals(this.list.get(0), this.delegate.get(0));
        assertEquals(this.list.get(1), this.delegate.get(1));
    }

    @Test
    void set() {
        this.delegate.set(1, "TestX");
        assertEquals(this.list.get(1), this.delegate.get(1));
    }

    @Test
    void testAdd() {
        this.delegate.add(1, "TestY");
        assertEquals(this.list.get(1), this.delegate.get(1));
        assertEquals(this.list.size(), this.delegate.size());
    }

    @Test
    void testRemove() {
        this.delegate.remove(1);
        assertEquals(this.list.size(), this.delegate.size());
        assertEquals(this.list.get(0), this.delegate.get(0));
    }

    @Test
    void indexOf() {
        assertEquals(this.list.indexOf("Test2"), this.delegate.indexOf("Test2"));
        assertEquals(this.list.indexOf("TestX"), this.delegate.indexOf("TestX"));
    }

    @Test
    void lastIndexOf() {
        this.delegate.add("Test2");
        assertEquals(this.list.lastIndexOf("Test2"), this.delegate.lastIndexOf("Test2"));
    }

    @Test
    void listIterator() {
        ListIterator<String> it1 = this.list.listIterator();
        ListIterator<String> it2 = this.delegate.listIterator();
        assertEquals(it1.next(), it2.next());
    }

    @Test
    void testListIterator() {
        ListIterator<String> it1 = this.list.listIterator(1);
        ListIterator<String> it2 = this.delegate.listIterator(1);
        assertEquals(it1.next(), it2.next());
    }

    @Test
    void subList() {
        List<String> sub1 = this.list.subList(0, 2);
        List<String> sub2 = this.delegate.subList(0, 2);
        assertEquals(sub1, sub2);
    }

}
