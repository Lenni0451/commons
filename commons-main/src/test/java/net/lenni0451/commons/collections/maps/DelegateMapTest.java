package net.lenni0451.commons.collections.maps;

import net.lenni0451.commons.collections.Maps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DelegateMapTest {

    private Map<String, String> map;
    private DelegateMap<String, String> delegate;

    @BeforeEach
    void setUp() {
        this.map = Maps.hashMap("Test1", "Test2", "Test3", "Test4", "Test5", "Test6");
        this.delegate = new DelegateMap<>(this.map);
    }

    @Test
    void size() {
        assertEquals(this.map.size(), this.delegate.size());
    }

    @Test
    void isEmpty() {
        assertEquals(this.map.isEmpty(), this.delegate.isEmpty());
    }

    @Test
    void containsKey() {
        assertEquals(this.map.containsKey("Test1"), this.delegate.containsKey("Test1"));
        assertEquals(this.map.containsKey("Test10"), this.delegate.containsKey("Test10"));
    }

    @Test
    void containsValue() {
        assertEquals(this.map.containsValue("Test2"), this.delegate.containsValue("Test2"));
        assertEquals(this.map.containsValue("Test20"), this.delegate.containsValue("Test20"));
    }

    @Test
    void get() {
        assertEquals(this.map.get("Test1"), this.delegate.get("Test1"));
        assertEquals(this.map.get("Test10"), this.delegate.get("Test10"));
    }

    @Test
    void put() {
        this.delegate.put("Test7", "Test8");
        assertEquals(this.map.size(), this.delegate.size());
        assertEquals(this.map.get("Test7"), this.delegate.get("Test7"));
    }

    @Test
    void remove() {
        this.delegate.remove("Test1");
        assertEquals(this.map.size(), this.delegate.size());
        assertEquals(this.map.containsKey("Test1"), this.delegate.containsKey("Test1"));
    }

    @Test
    void putAll() {
        this.delegate.putAll(Maps.hashMap("Test7", "Test8", "Test9", "Test10"));
        assertEquals(this.map.size(), this.delegate.size());
        assertEquals(this.map.get("Test7"), this.delegate.get("Test7"));
        assertEquals(this.map.get("Test9"), this.delegate.get("Test9"));
    }

    @Test
    void clear() {
        this.delegate.clear();
        assertEquals(this.map.size(), this.delegate.size());
        assertEquals(this.map.isEmpty(), this.delegate.isEmpty());
    }

    @Test
    void keySet() {
        assertEquals(this.map.keySet(), this.delegate.keySet());
    }

    @Test
    void values() {
        assertEquals(this.map.values(), this.delegate.values());
    }

    @Test
    void entrySet() {
        assertEquals(this.map.entrySet(), this.delegate.entrySet());
    }

}
