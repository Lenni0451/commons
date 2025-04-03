package net.lenni0451.commons.collections;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import static org.junit.jupiter.api.Assertions.*;

class SetsTest {

    @Test
    void sort() {
        Set<String> set = new HashSet<>();
        set.add("A");
        set.add("B");
        set.add("C");
        set = Sets.sort(set, (o1, o2) -> o2.compareToIgnoreCase(o1));

        int i = 0;
        for (String s : set) {
            switch (i) {
                case 0:
                    assertEquals("C", s);
                    break;
                case 1:
                    assertEquals("B", s);
                    break;
                case 2:
                    assertEquals("A", s);
                    break;
                default:
                    fail("Unexpected value: " + s + " (" + i + ")");
            }
            i++;
        }
    }

    @Test
    void merge() {
        Set<String> set1 = new HashSet<>();
        set1.add("A");
        set1.add("B");
        Set<String> set2 = new HashSet<>();
        set2.add("C");
        set2.add("D");
        Set<String> set3 = new HashSet<>();
        set3.add("E");
        set3.add("F");

        Set<String> set = Sets.merge(set1, set2, set3);
        assertEquals(6, set.size());
        assertTrue(set.contains("A"));
        assertTrue(set.contains("B"));
        assertTrue(set.contains("C"));
        assertTrue(set.contains("D"));
        assertTrue(set.contains("E"));
        assertTrue(set.contains("F"));
    }

    @Test
    void newHashSet() {
        Set<String> set = Sets.hashSet("A", "B");
        assertInstanceOf(HashSet.class, set);
        assertTrue(set.contains("A"));
        assertTrue(set.contains("B"));
    }

    @Test
    void newLinkedHashSet() {
        Set<String> set = Sets.linkedHashSet("A", "B");
        assertInstanceOf(HashSet.class, set);
        assertTrue(set.contains("A"));
        assertTrue(set.contains("B"));
    }

    @Test
    void newConcurrentSkipListSet() {
        Set<String> set = Sets.concurrentSkipListSet("A", "B");
        assertInstanceOf(ConcurrentSkipListSet.class, set);
        assertTrue(set.contains("A"));
        assertTrue(set.contains("B"));
    }

}
