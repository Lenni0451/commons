package net.lenni0451.commons.collections;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class MapsTest {

    @Test
    void sort() {
        Map<String, String> map = new HashMap<>();
        map.put("A", "C");
        map.put("C", "B");
        map.put("B", "A");
        map = Maps.sort(map, (o1, o2) -> o1.getValue().compareToIgnoreCase(o2.getValue()));

        int i = 0;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            switch (i) {
                case 0:
                    assertEquals("B", entry.getKey());
                    assertEquals("A", entry.getValue());
                    break;
                case 1:
                    assertEquals("C", entry.getKey());
                    assertEquals("B", entry.getValue());
                    break;
                case 2:
                    assertEquals("A", entry.getKey());
                    assertEquals("C", entry.getValue());
                    break;
                default:
                    fail("Unexpected value: " + entry.getKey() + " (" + i + ")");
            }
            i++;
        }
    }

    @Test
    void merge() {
        Map<String, String> map1 = new HashMap<>();
        map1.put("A", "B");
        map1.put("B", "C");
        Map<String, String> map2 = new HashMap<>();
        map2.put("C", "D");
        map2.put("D", "E");
        Map<String, String> map3 = new HashMap<>();
        map3.put("E", "F");
        map3.put("F", "G");

        Map<String, String> map = Maps.merge(map1, map2, map3);
        assertEquals(6, map.size());
        assertEquals("B", map.get("A"));
        assertEquals("C", map.get("B"));
        assertEquals("D", map.get("C"));
        assertEquals("E", map.get("D"));
        assertEquals("F", map.get("E"));
    }

    @Test
    void hashMap() {
        Map<String, String> map = Maps.hashMap("A", "B", "C", "D");
        assertInstanceOf(HashMap.class, map);
        assertEquals("B", map.get("A"));
        assertEquals("D", map.get("C"));
    }

    @Test
    void linkedHashMap() {
        Map<String, String> map = Maps.linkedHashMap("A", "B", "C", "D");
        assertInstanceOf(HashMap.class, map);
        assertEquals("B", map.get("A"));
        assertEquals("D", map.get("C"));
    }

    @Test
    void concurrentHashMap() {
        Map<String, String> map = Maps.concurrentHashMap("A", "B", "C", "D");
        assertInstanceOf(ConcurrentHashMap.class, map);
        assertEquals("B", map.get("A"));
        assertEquals("D", map.get("C"));
    }

}
