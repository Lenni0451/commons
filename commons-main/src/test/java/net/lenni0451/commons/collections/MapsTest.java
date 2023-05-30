package net.lenni0451.commons.collections;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

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
            }
            i++;
        }
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
