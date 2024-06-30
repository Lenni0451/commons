package net.lenni0451.commons.collections.maps;

import net.lenni0451.commons.collections.Maps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SizeConstrainedMapTest {

    private Map<String, String> map;

    @BeforeEach
    void setUp() {
        this.map = new SizeConstrainedMap<>(new HashMap<>(), 4);
        this.map.putAll(Maps.hashMap("Test1", "Test2", "Test3", "Test4", "Test5", "Test6"));
    }

    @Test
    void put() {
        this.map.put("Test7", "Test8");
        assertEquals(4, this.map.size());
        assertEquals("Test8", this.map.get("Test7"));
    }

    @Test
    void remove() {
        this.map.remove("Test1");
        assertEquals(2, this.map.size());
        assertNull(this.map.get("Test1"));
        assertEquals("Test4", this.map.get("Test3"));
        assertEquals("Test6", this.map.get("Test5"));
    }

    @Test
    void overfill() {
        this.map.put("Test7", "Test8");
        this.map.put("Test9", "Test10");
        assertEquals(4, this.map.size());
        assertNull(this.map.get("Test1"));
        assertNull(this.map.get("Test2"));
        assertEquals("Test4", this.map.get("Test3"));
        assertEquals("Test6", this.map.get("Test5"));
        assertEquals("Test8", this.map.get("Test7"));
        assertEquals("Test10", this.map.get("Test9"));
    }

}
