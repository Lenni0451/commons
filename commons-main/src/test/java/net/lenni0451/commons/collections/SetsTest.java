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
    void newHashSet() {
        Set<String> set = Sets.newHashSet("A", "B");
        assertInstanceOf(HashSet.class, set);
        assertTrue(set.contains("A"));
        assertTrue(set.contains("B"));
    }

    @Test
    void newLinkedHashSet() {
        Set<String> set = Sets.newLinkedHashSet("A", "B");
        assertInstanceOf(HashSet.class, set);
        assertTrue(set.contains("A"));
        assertTrue(set.contains("B"));
    }

    @Test
    void newConcurrentSkipListSet() {
        Set<String> set = Sets.newConcurrentSkipListSet("A", "B");
        assertInstanceOf(ConcurrentSkipListSet.class, set);
        assertTrue(set.contains("A"));
        assertTrue(set.contains("B"));
    }

}
