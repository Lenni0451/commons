package net.lenni0451.commons.gson.elements;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GsonPrimitiveTest {

    private static final GsonPrimitive BOOLEAN = new GsonPrimitive(true);
    private static final GsonPrimitive NUMBER = new GsonPrimitive(1);
    private static final GsonPrimitive STRING = new GsonPrimitive("test");

    @Test
    void isBoolean() {
        assertTrue(BOOLEAN.isBoolean());
        assertFalse(NUMBER.isBoolean());
        assertFalse(STRING.isBoolean());
    }

    @Test
    void isNumber() {
        assertFalse(BOOLEAN.isNumber());
        assertTrue(NUMBER.isNumber());
        assertFalse(STRING.isNumber());
    }

    @Test
    void isString() {
        assertFalse(BOOLEAN.isString());
        assertFalse(NUMBER.isString());
        assertTrue(STRING.isString());
    }

}
