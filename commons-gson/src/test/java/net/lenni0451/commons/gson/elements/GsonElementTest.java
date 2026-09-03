package net.lenni0451.commons.gson.elements;

import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class GsonElementTest {

    private static final JsonObject JSON_OBJECT = new JsonObject();
    private static final JsonArray JSON_ARRAY = new JsonArray();
    private static final JsonPrimitive JSON_PRIMITIVE_STRING = new JsonPrimitive("test");
    private static final JsonNull JSON_NULL = JsonNull.INSTANCE;

    private static final GsonPrimitive GSON_PRIMITIVE_NUMBER = new GsonPrimitive(123);
    private static final GsonElement GSON_PRIMITIVE_STRING = new GsonPrimitive("abc");
    private static final GsonElement GSON_ELEMENT_NULL = GsonElement.wrap(JSON_NULL);

    @BeforeAll
    static void init() {
        JSON_OBJECT.addProperty("key", "value");
        JSON_ARRAY.add("item");
    }

    @Test
    void wrap() {
        assertInstanceOf(GsonObject.class, GsonElement.wrap(JSON_OBJECT));
        assertInstanceOf(GsonArray.class, GsonElement.wrap(JSON_ARRAY));
        assertInstanceOf(GsonPrimitive.class, GsonElement.wrap(JSON_PRIMITIVE_STRING));
        assertInstanceOf(GsonElement.class, GsonElement.wrap(JSON_NULL));
    }

    @Test
    void getJsonElement() {
        assertSame(JSON_OBJECT, GsonElement.wrap(JSON_OBJECT).getJsonElement());
        assertSame(JSON_ARRAY, GsonElement.wrap(JSON_ARRAY).getJsonElement());
        assertSame(JSON_PRIMITIVE_STRING, GsonElement.wrap(JSON_PRIMITIVE_STRING).getJsonElement());
        assertSame(JSON_NULL, GsonElement.wrap(JSON_NULL).getJsonElement());
    }

    @Test
    void deepCopy() {
        GsonElement original = GsonElement.wrap(JSON_OBJECT);
        GsonElement copy = original.deepCopy();

        assertInstanceOf(GsonObject.class, copy);
        assertEquals(original, copy);
        assertNotSame(original.getJsonElement(), copy.getJsonElement());
    }

    @Test
    void isType() {
        GsonElement object = GsonElement.wrap(JSON_OBJECT);
        assertTrue(object.isObject());
        assertFalse(object.isArray());
        assertFalse(object.isPrimitive());
        assertFalse(object.isNull());

        GsonElement array = GsonElement.wrap(JSON_ARRAY);
        assertFalse(array.isObject());
        assertTrue(array.isArray());
        assertFalse(array.isPrimitive());
        assertFalse(array.isNull());

        GsonElement primitive = GsonElement.wrap(JSON_PRIMITIVE_STRING);
        assertFalse(primitive.isObject());
        assertFalse(primitive.isArray());
        assertTrue(primitive.isPrimitive());
        assertFalse(primitive.isNull());

        GsonElement nullElement = GsonElement.wrap(JSON_NULL);
        assertFalse(nullElement.isObject());
        assertFalse(nullElement.isArray());
        assertFalse(nullElement.isPrimitive());
        assertTrue(nullElement.isNull());
    }

    @Test
    void asObject() {
        GsonObject defaultObject = new GsonObject();
        GsonObject gsonObject = new GsonObject().add("key", "value");
        GsonElement wrappedObject = new GsonElement(JSON_OBJECT);

        assertSame(gsonObject, gsonObject.asObject());
        assertSame(gsonObject, gsonObject.asObject(defaultObject));
        assertEquals(gsonObject, wrappedObject.asObject());
        assertEquals(gsonObject, wrappedObject.asObject(defaultObject));

        assertThrows(IllegalStateException.class, GSON_ELEMENT_NULL::asObject);
        assertSame(defaultObject, GSON_ELEMENT_NULL.asObject(defaultObject));
    }

    @Test
    void asArray() {
        GsonArray defaultArray = new GsonArray();
        GsonArray gsonArray = new GsonArray().add("item");
        GsonElement wrappedArray = new GsonElement(JSON_ARRAY);

        assertSame(gsonArray, gsonArray.asArray());
        assertSame(gsonArray, gsonArray.asArray(defaultArray));
        assertEquals(gsonArray, wrappedArray.asArray());
        assertEquals(gsonArray, wrappedArray.asArray(defaultArray));

        assertThrows(IllegalStateException.class, GSON_ELEMENT_NULL::asArray);
        assertSame(defaultArray, GSON_ELEMENT_NULL.asArray(defaultArray));
    }

    @Test
    void asPrimitive() {
        GsonPrimitive defaultPrimitive = new GsonPrimitive("default");
        GsonPrimitive gsonPrimitive = new GsonPrimitive("test");
        GsonElement wrappedPrimitive = new GsonElement(JSON_PRIMITIVE_STRING);

        assertSame(gsonPrimitive, gsonPrimitive.asPrimitive());
        assertSame(gsonPrimitive, gsonPrimitive.asPrimitive(defaultPrimitive));
        assertEquals(gsonPrimitive, wrappedPrimitive.asPrimitive());
        assertEquals(gsonPrimitive, wrappedPrimitive.asPrimitive(defaultPrimitive));

        assertThrows(IllegalStateException.class, GSON_ELEMENT_NULL::asPrimitive);
        assertSame(defaultPrimitive, GSON_ELEMENT_NULL.asPrimitive(defaultPrimitive));
    }

    @Test
    void asBoolean() {
        GsonElement trueElement = new GsonPrimitive(true);
        GsonElement falseElement = new GsonPrimitive(false);

        assertTrue(trueElement.asBoolean());
        assertTrue(trueElement.asBoolean(false));
        assertFalse(falseElement.asBoolean());
        assertFalse(falseElement.asBoolean(true));

        assertFalse(GSON_ELEMENT_NULL.asBoolean(false));
        assertTrue(GSON_ELEMENT_NULL.asBoolean(true));
        assertThrows(UnsupportedOperationException.class, GSON_ELEMENT_NULL::asBoolean);
    }

    @Test
    void asByte() {
        assertEquals((byte) 123, GSON_PRIMITIVE_NUMBER.asByte());
        assertEquals((byte) 123, GSON_PRIMITIVE_NUMBER.asByte((byte) 0));

        assertEquals((byte) 10, GSON_PRIMITIVE_STRING.asByte((byte) 10));
        assertThrows(NumberFormatException.class, GSON_PRIMITIVE_STRING::asByte);

        assertEquals((byte) 10, GSON_ELEMENT_NULL.asByte((byte) 10));
        assertThrows(UnsupportedOperationException.class, GSON_ELEMENT_NULL::asByte);
    }

    @Test
    void asShort() {
        assertEquals((short) 123, GSON_PRIMITIVE_NUMBER.asShort());
        assertEquals((short) 123, GSON_PRIMITIVE_NUMBER.asShort((short) 0));

        assertEquals((short) 10, GSON_PRIMITIVE_STRING.asShort((short) 10));
        assertThrows(NumberFormatException.class, GSON_PRIMITIVE_STRING::asShort);

        assertEquals((short) 10, GSON_ELEMENT_NULL.asShort((short) 10));
        assertThrows(UnsupportedOperationException.class, GSON_ELEMENT_NULL::asShort);
    }

    @Test
    void asInt() {
        assertEquals(123, GSON_PRIMITIVE_NUMBER.asInt());
        assertEquals(123, GSON_PRIMITIVE_NUMBER.asInt(0));

        assertEquals(10, GSON_PRIMITIVE_STRING.asInt(10));
        assertThrows(NumberFormatException.class, GSON_PRIMITIVE_STRING::asInt);

        assertEquals(10, GSON_ELEMENT_NULL.asInt(10));
        assertThrows(UnsupportedOperationException.class, GSON_ELEMENT_NULL::asInt);
    }

    @Test
    void asLong() {
        assertEquals(123L, GSON_PRIMITIVE_NUMBER.asLong());
        assertEquals(123L, GSON_PRIMITIVE_NUMBER.asLong(0L));

        assertEquals(10L, GSON_PRIMITIVE_STRING.asLong(10L));
        assertThrows(NumberFormatException.class, GSON_PRIMITIVE_STRING::asLong);

        assertEquals(10L, GSON_ELEMENT_NULL.asLong(10L));
        assertThrows(UnsupportedOperationException.class, GSON_ELEMENT_NULL::asLong);
    }

    @Test
    void asFloat() {
        assertEquals(123F, GSON_PRIMITIVE_NUMBER.asFloat());
        assertEquals(123F, GSON_PRIMITIVE_NUMBER.asFloat(0F));

        assertEquals(10F, GSON_PRIMITIVE_STRING.asFloat(10F));
        assertThrows(NumberFormatException.class, GSON_PRIMITIVE_STRING::asFloat);

        assertEquals(10F, GSON_ELEMENT_NULL.asFloat(10F));
        assertThrows(UnsupportedOperationException.class, GSON_ELEMENT_NULL::asFloat);
    }

    @Test
    void asDouble() {
        assertEquals(123D, GSON_PRIMITIVE_NUMBER.asDouble());
        assertEquals(123D, GSON_PRIMITIVE_NUMBER.asDouble(0D));

        assertEquals(10D, GSON_PRIMITIVE_STRING.asDouble(10D));
        assertThrows(NumberFormatException.class, GSON_PRIMITIVE_STRING::asDouble);

        assertEquals(10D, GSON_ELEMENT_NULL.asDouble(10D));
        assertThrows(UnsupportedOperationException.class, GSON_ELEMENT_NULL::asDouble);
    }

    @Test
    void asBigInteger() {
        BigInteger number = new BigInteger("12345678901234567890");
        GsonElement numberElement = new GsonPrimitive(number);

        assertEquals(number, numberElement.asBigInteger());
        assertEquals(number, numberElement.asBigInteger(BigInteger.ZERO));

        assertEquals(BigInteger.TEN, GSON_PRIMITIVE_STRING.asBigInteger(BigInteger.TEN));
        assertThrows(NumberFormatException.class, GSON_PRIMITIVE_STRING::asBigInteger);

        assertEquals(BigInteger.TEN, GSON_ELEMENT_NULL.asBigInteger(BigInteger.TEN));
        assertThrows(UnsupportedOperationException.class, GSON_ELEMENT_NULL::asBigInteger);
    }

    @Test
    void asBigDecimal() {
        BigDecimal number = new BigDecimal("1234567890.1234567890");
        GsonElement numberElement = new GsonPrimitive(number);

        assertEquals(number, numberElement.asBigDecimal());
        assertEquals(number, numberElement.asBigDecimal(BigDecimal.ZERO));

        assertEquals(BigDecimal.TEN, GSON_PRIMITIVE_STRING.asBigDecimal(BigDecimal.TEN));
        assertThrows(NumberFormatException.class, GSON_PRIMITIVE_STRING::asBigDecimal);

        assertEquals(BigDecimal.TEN, GSON_ELEMENT_NULL.asBigDecimal(BigDecimal.TEN));
        assertThrows(UnsupportedOperationException.class, GSON_ELEMENT_NULL::asBigDecimal);
    }

    @Test
    void asNumber() {
        assertEquals(123, GSON_PRIMITIVE_NUMBER.asNumber().intValue());
        assertEquals(123, GSON_PRIMITIVE_NUMBER.asNumber(0).intValue());

        assertEquals(10, GSON_ELEMENT_NULL.asNumber(10));
        assertThrows(UnsupportedOperationException.class, GSON_ELEMENT_NULL::asNumber);
    }

    @Test
    void asString() {
        assertEquals("abc", GSON_PRIMITIVE_STRING.asString());
        assertEquals("abc", GSON_PRIMITIVE_STRING.asString("default"));
        assertEquals("123", GSON_PRIMITIVE_NUMBER.asString());
        assertEquals("123", GSON_PRIMITIVE_NUMBER.asString("default"));

        assertEquals("default", GSON_ELEMENT_NULL.asString("default"));
        assertThrows(UnsupportedOperationException.class, GSON_ELEMENT_NULL::asString);
    }

}
