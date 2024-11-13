package net.lenni0451.commons.collections;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MapOpsTest {

    @Test
    void testRecursivePut() {
        Map<String, Object> map1 = Maps.hashMap("a", "b", "c", Maps.hashMap("d", "e"));
        Map<String, Object> map2 = Maps.hashMap("b", "c", "c", Maps.hashMap("e", "f"));
        Map<String, Object> out = Maps.hashMap("a", "b", "b", "c", "c", Maps.hashMap("d", "e", "e", "f"));
        MapOps.recursivePutAll(map1, map2);
        assertEquals(out, map1);
    }

    @Test
    void testGetDuplicates() {
        Map<String, Object> map1 = Maps.hashMap("a", "b", "b", "c", "c", Maps.hashMap("d", "e", "e", "f"));
        Map<String, Object> map2 = Maps.hashMap("a", "c", "b", "c", "c", Maps.hashMap("e", "f"));
        Map<String, Object> out = Maps.hashMap("b", "c", "c", Maps.hashMap("e", "f"));
        assertEquals(out, MapOps.getDuplicates(map1, map2));
    }

    @Test
    void testGetUniques() {
        Map<String, Object> map1 = Maps.hashMap("a", "b", "b", "c", "c", Maps.hashMap("d", "e", "e", "f"));
        Map<String, Object> map2 = Maps.hashMap("a", "c", "b", "c", "c", Maps.hashMap("e", "f"));
        Map<String, Object> out = Maps.hashMap("a", "b", "c", Maps.hashMap("d", "e"));
        assertEquals(out, MapOps.getUniques(map1, map2));
    }

}
