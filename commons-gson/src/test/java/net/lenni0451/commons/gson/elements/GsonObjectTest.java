package net.lenni0451.commons.gson.elements;

import org.junit.jupiter.api.*;

import java.util.*;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GsonObjectTest {

    private static final GsonObject OBJECT = new GsonObject();

    @Test
    @Order(0)
    void add() {
        OBJECT.add("key0", new GsonObject().add("test", 123));
        OBJECT.add("key1", new GsonArray().add("test").add(123).getJsonArray());
        OBJECT.add("key2", true);
        OBJECT.add("key3", 123);
        OBJECT.add("key4", "123");

        for (int i = 0; i < 5; i++) assertTrue(OBJECT.has("key" + i));
        assertEquals(new GsonObject().add("test", 123), OBJECT.get("key0"));
        assertEquals(new GsonArray().add("test").add(123), OBJECT.get("key1"));
        assertEquals(new GsonPrimitive(true), OBJECT.get("key2"));
        assertEquals(new GsonPrimitive(123), OBJECT.get("key3"));
        assertEquals(new GsonPrimitive("123"), OBJECT.get("key4"));
    }

    @Test
    @Order(1)
    void has() {
        Map<HasMethod, Predicate<String>> hasMethods = new HashMap<>();
        hasMethods.put(HasMethod.GENERIC, OBJECT::has);
        hasMethods.put(HasMethod.OBJECT, OBJECT::hasObject);
        hasMethods.put(HasMethod.ARRAY, OBJECT::hasArray);
        hasMethods.put(HasMethod.PRIMITIVE, OBJECT::hasPrimitive);
        hasMethods.put(HasMethod.BOOLEAN, OBJECT::hasBoolean);
        hasMethods.put(HasMethod.NUMBER, OBJECT::hasNumber);
        hasMethods.put(HasMethod.STRING, OBJECT::hasString);

        Map<String, List<HasMethod>> expected = new HashMap<>();
        expected.put("key0", Arrays.asList(HasMethod.GENERIC, HasMethod.OBJECT));
        expected.put("key1", Arrays.asList(HasMethod.GENERIC, HasMethod.ARRAY));
        expected.put("key2", Arrays.asList(HasMethod.GENERIC, HasMethod.PRIMITIVE, HasMethod.BOOLEAN));
        expected.put("key3", Arrays.asList(HasMethod.GENERIC, HasMethod.PRIMITIVE, HasMethod.NUMBER));
        expected.put("key4", Arrays.asList(HasMethod.GENERIC, HasMethod.PRIMITIVE, HasMethod.STRING));

        for (Map.Entry<String, List<HasMethod>> expectedEntry : expected.entrySet()) {
            for (Map.Entry<HasMethod, Predicate<String>> methodEntry : hasMethods.entrySet()) {
                boolean expectedValue = expectedEntry.getValue().contains(methodEntry.getKey());
                boolean actualValue = methodEntry.getValue().test(expectedEntry.getKey());
                assertEquals(expectedValue, actualValue, "Key: " + expectedEntry.getKey() + ", Method: " + methodEntry.getKey());
            }
        }
    }

    @Test
    @Order(1)
    void get() {
        for (String key : new String[]{"key0", "key1", "key2", "key3", "key4"}) {
            assertNotNull(OBJECT.get(key), "Key: " + key);
            assertNotNull(OBJECT.get(key, null), "Key: " + key);
        }
        assertNull(OBJECT.get("unknown"));
        assertNull(OBJECT.get("unknown", null));
        assertNotNull(OBJECT.get("unknown", new GsonObject()));
        assertFalse(OBJECT.opt("unknown").isPresent());
        assertTrue(OBJECT.opt("key0").isPresent());
        assertThrows(NoSuchElementException.class, () -> OBJECT.req("unknown"));
        assertDoesNotThrow(() -> OBJECT.req("key0"));

        assertNotNull(OBJECT.getObject("key0"));
        assertNotNull(OBJECT.getObject("key0", null));
        assertNull(OBJECT.getObject("unknown"));
        assertNull(OBJECT.getObject("unknown", null));
        assertNotNull(OBJECT.getObject("unknown", new GsonObject()));
        assertFalse(OBJECT.optObject("unknown").isPresent());
        assertTrue(OBJECT.optObject("key0").isPresent());
        assertThrows(NoSuchElementException.class, () -> OBJECT.reqObject("unknown"));
        assertDoesNotThrow(() -> OBJECT.reqObject("key0"));

        assertNotNull(OBJECT.getArray("key1"));
        assertNotNull(OBJECT.getArray("key1", null));
        assertNull(OBJECT.getArray("unknown"));
        assertNull(OBJECT.getArray("unknown", null));
        assertNotNull(OBJECT.getArray("unknown", new GsonArray()));
        assertFalse(OBJECT.optArray("unknown").isPresent());
        assertTrue(OBJECT.optArray("key1").isPresent());
        assertThrows(NoSuchElementException.class, () -> OBJECT.reqArray("unknown"));
        assertDoesNotThrow(() -> OBJECT.reqArray("key1"));

        assertNotNull(OBJECT.getPrimitive("key2"));
        assertNotNull(OBJECT.getPrimitive("key2", null));
        assertNull(OBJECT.getPrimitive("unknown"));
        assertNull(OBJECT.getPrimitive("unknown", null));
        assertNotNull(OBJECT.getPrimitive("unknown", new GsonPrimitive(true)));
        assertFalse(OBJECT.optPrimitive("unknown").isPresent());
        assertTrue(OBJECT.optPrimitive("key2").isPresent());
        assertThrows(NoSuchElementException.class, () -> OBJECT.reqPrimitive("unknown"));
        assertDoesNotThrow(() -> OBJECT.reqPrimitive("key2"));

        assertTrue(OBJECT.getBoolean("key2"));
        assertTrue(OBJECT.getBoolean("key2", false));
        assertFalse(OBJECT.getBoolean("unknown"));
        assertTrue(OBJECT.getBoolean("unknown", true));
        assertThrows(NoSuchElementException.class, () -> OBJECT.reqBoolean("unknown"));
        assertDoesNotThrow(() -> OBJECT.reqBoolean("key2"));

        assertEquals(123, OBJECT.getByte("key3"));
        assertEquals(123, OBJECT.getByte("key3", (byte) 0));
        assertEquals(0, OBJECT.getByte("unknown"));
        assertEquals(0, OBJECT.getByte("unknown", (byte) 0));
        assertThrows(NoSuchElementException.class, () -> OBJECT.reqByte("unknown"));
        assertDoesNotThrow(() -> OBJECT.reqByte("key3"));

        assertEquals(123, OBJECT.getShort("key3"));
        assertEquals(123, OBJECT.getShort("key3", (short) 0));
        assertEquals(0, OBJECT.getShort("unknown"));
        assertEquals(0, OBJECT.getShort("unknown", (short) 0));
        assertThrows(NoSuchElementException.class, () -> OBJECT.reqShort("unknown"));
        assertDoesNotThrow(() -> OBJECT.reqShort("key3"));

        assertEquals(123, OBJECT.getInt("key3"));
        assertEquals(123, OBJECT.getInt("key3", 0));
        assertEquals(0, OBJECT.getInt("unknown"));
        assertEquals(0, OBJECT.getInt("unknown", 0));
        assertFalse(OBJECT.optInt("unknown").isPresent());
        assertTrue(OBJECT.optInt("key3").isPresent());
        assertThrows(NoSuchElementException.class, () -> OBJECT.reqInt("unknown"));
        assertDoesNotThrow(() -> OBJECT.reqInt("key3"));

        assertEquals(123, OBJECT.getLong("key3"));
        assertEquals(123, OBJECT.getLong("key3", 0));
        assertEquals(0, OBJECT.getLong("unknown"));
        assertEquals(0, OBJECT.getLong("unknown", 0));
        assertFalse(OBJECT.optLong("unknown").isPresent());
        assertTrue(OBJECT.optLong("key3").isPresent());
        assertThrows(NoSuchElementException.class, () -> OBJECT.reqLong("unknown"));
        assertDoesNotThrow(() -> OBJECT.reqLong("key3"));

        assertEquals(123, OBJECT.getFloat("key3"));
        assertEquals(123, OBJECT.getFloat("key3", 0));
        assertEquals(0, OBJECT.getFloat("unknown"));
        assertEquals(0, OBJECT.getFloat("unknown", 0));
        assertThrows(NoSuchElementException.class, () -> OBJECT.reqFloat("unknown"));
        assertDoesNotThrow(() -> OBJECT.reqFloat("key3"));

        assertEquals(123, OBJECT.getDouble("key3"));
        assertEquals(123, OBJECT.getDouble("key3", 0));
        assertEquals(0, OBJECT.getDouble("unknown"));
        assertEquals(0, OBJECT.getDouble("unknown", 0));
        assertFalse(OBJECT.optDouble("unknown").isPresent());
        assertTrue(OBJECT.optDouble("key3").isPresent());
        assertThrows(NoSuchElementException.class, () -> OBJECT.reqDouble("unknown"));
        assertDoesNotThrow(() -> OBJECT.reqDouble("key3"));

        assertNotNull(OBJECT.getNumber("key3"));
        assertNotNull(OBJECT.getNumber("key3", 0));
        assertNull(OBJECT.getNumber("unknown"));
        assertNotNull(OBJECT.getNumber("unknown", 0));
        assertFalse(OBJECT.optNumber("unknown").isPresent());
        assertTrue(OBJECT.optNumber("key3").isPresent());
        assertThrows(NoSuchElementException.class, () -> OBJECT.reqNumber("unknown"));
        assertDoesNotThrow(() -> OBJECT.reqNumber("key3"));

        assertNotNull(OBJECT.getString("key4"));
        assertNotNull(OBJECT.getString("key4", ""));
        assertNull(OBJECT.getString("unknown"));
        assertNotNull(OBJECT.getString("unknown", ""));
        assertFalse(OBJECT.optString("unknown").isPresent());
        assertTrue(OBJECT.optString("key4").isPresent());
        assertThrows(NoSuchElementException.class, () -> OBJECT.reqString("unknown"));
        assertDoesNotThrow(() -> OBJECT.reqString("key4"));
    }

    @Test
    @Order(2)
    void size() {
        assertEquals(5, OBJECT.size());
    }

    @Test
    @Order(2)
    void sets() {
        assertIterableEquals(Arrays.asList("key0", "key1", "key2", "key3", "key4"), OBJECT.keySet());
        assertEquals(5, OBJECT.entrySet().size());
    }

    @Test
    @Order(2)
    void iterator() {
        for (Map.Entry<String, GsonElement> entry : OBJECT) {
            assertNotNull(entry);
        }
    }

    @Test
    @Order(2)
    void stream() {
        assertEquals(OBJECT.size(), OBJECT.stream().peek(Assertions::assertNotNull).count());
    }

    @Test
    @Order(2)
    void asMap() {
        Map<String, GsonElement> map = OBJECT.asMap();
        for (Map.Entry<String, GsonElement> entry : OBJECT) {
            assertEquals(entry.getValue(), map.get(entry.getKey()));
        }
    }

    @Test
    @Order(3)
    void remove() {
        OBJECT.remove("key0");
        assertFalse(OBJECT.has("key0"));

        while (!OBJECT.isEmpty()) {
            OBJECT.remove(OBJECT.keySet().iterator().next());
        }
    }


    private enum HasMethod {
        GENERIC,
        OBJECT,
        ARRAY,
        PRIMITIVE,
        BOOLEAN,
        NUMBER,
        STRING
    }

}
