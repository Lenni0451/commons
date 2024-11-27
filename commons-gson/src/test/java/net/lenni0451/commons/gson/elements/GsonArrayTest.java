package net.lenni0451.commons.gson.elements;

import com.google.gson.JsonPrimitive;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GsonArrayTest {

    private static final GsonArray ARRAY = new GsonArray();

    @Test
    @Order(0)
    void add() {
        ARRAY.add(new JsonPrimitive(0)).add(new GsonPrimitive(true)).add(false).add(1).add("");
        ARRAY.addAll(new GsonArray().add("10").add(11).getJsonArray());
        ARRAY.addAll(new GsonArray().add("20").add(21));

        this.check(0, true, false, 1, "", "10", 11, "20", 21);
    }

    @Test
    @Order(1)
    void set() {
        ARRAY.set(0, new JsonPrimitive(1));
        ARRAY.set(1, new GsonPrimitive(false));
        ARRAY.set(2, true);
        ARRAY.set(3, 0);
        ARRAY.set(4, "1");

        this.check(1, false, true, 0, "1", "10", 11, "20", 21);
    }

    @Test
    @Order(2)
    void remove() {
        ARRAY.remove(2);
        ARRAY.remove(new JsonPrimitive(1));
        ARRAY.remove(new GsonPrimitive(false));
        ARRAY.add(false).removeBoolean(false);
        ARRAY.add(123).removeNumber(123);
        ARRAY.add("123").removeString("123");
        ARRAY.removeAll(new GsonArray().add("10").add(11).getJsonArray());
        ARRAY.removeAll(new GsonArray().add("20").add(21));

        this.check(0, "1");
    }

    @Test
    @Order(3)
    void has() {
        assertTrue(ARRAY.has(0));
        assertFalse(ARRAY.has(-10));
        assertFalse(ARRAY.has(100));

        assertTrue(ARRAY.has(new JsonPrimitive(0)));
        assertTrue(ARRAY.has(new JsonPrimitive("1")));
        assertFalse(ARRAY.has(new JsonPrimitive("2")));

        assertTrue(ARRAY.has(new GsonPrimitive(0)));
        assertTrue(ARRAY.has(new GsonPrimitive("1")));
        assertFalse(ARRAY.has(new GsonPrimitive("2")));
    }

    @Test
    @Order(3)
    void iterator() {
        for (GsonElement element : ARRAY) {
            assertNotNull(element);
        }
    }

    @Test
    @Order(3)
    void stream() {
        assertEquals(ARRAY.size(), ARRAY.stream().peek(Assertions::assertNotNull).count());
    }

    @Test
    @Order(3)
    void asList() {
        this.check(ARRAY.asList().toArray());
    }

    @Test
    @Order(4)
    void clear() {
        ARRAY.clear();
        this.check();
    }

    private void check(final Object... objects) {
        List<GsonElement> expected = new ArrayList<>();
        for (Object object : objects) {
            if (object instanceof Number) {
                expected.add(new GsonPrimitive((Number) object));
            } else if (object instanceof Boolean) {
                expected.add(new GsonPrimitive((Boolean) object));
            } else if (object instanceof String) {
                expected.add(new GsonPrimitive((String) object));
            } else if (object instanceof GsonElement) {
                expected.add((GsonElement) object);
            } else {
                throw new IllegalArgumentException("Invalid object type: " + object.getClass().getName());
            }
        }

        assertEquals(expected.size(), ARRAY.size());
        assertEquals(expected.isEmpty(), ARRAY.isEmpty());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), ARRAY.get(i));
        }
    }

}
