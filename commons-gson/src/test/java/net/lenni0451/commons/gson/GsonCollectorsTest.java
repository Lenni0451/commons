package net.lenni0451.commons.gson;

import net.lenni0451.commons.gson.elements.GsonArray;
import net.lenni0451.commons.gson.elements.GsonObject;
import net.lenni0451.commons.gson.elements.GsonPrimitive;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GsonCollectorsTest {

    @Test
    void toArray() {
        List<String> data = new ArrayList<>();
        data.add("one");
        data.add("two");
        data.add("three");

        GsonArray array = data.stream().map(GsonPrimitive::new).collect(GsonCollectors.toArray());
        assertEquals(3, array.size());
        assertEquals("one", array.get(0).asString());
        assertEquals("two", array.get(1).asString());
        assertEquals("three", array.get(2).asString());
    }

    @Test
    void toObject() {
        Map<String, String> data = new HashMap<>();
        data.put("key1", "value1");
        data.put("key2", "value2");
        data.put("key3", "value3");

        GsonObject object = data.entrySet().stream().collect(GsonCollectors.toObject(Map.Entry::getKey, e -> new GsonPrimitive(e.getValue())));
        assertEquals(3, object.size());
        assertEquals("value1", object.get("key1").asString());
        assertEquals("value2", object.get("key2").asString());
        assertEquals("value3", object.get("key3").asString());
    }

}
