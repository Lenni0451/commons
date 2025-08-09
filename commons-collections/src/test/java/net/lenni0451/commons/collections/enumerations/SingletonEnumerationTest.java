package net.lenni0451.commons.collections.enumerations;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class SingletonEnumerationTest {

    @Test
    void test() {
        SingletonEnumeration<String> enumeration = new SingletonEnumeration<>("test");
        assertTrue(enumeration.hasMoreElements());
        assertEquals("test", enumeration.nextElement());
        assertFalse(enumeration.hasMoreElements());
        assertThrows(NoSuchElementException.class, enumeration::nextElement);
    }

}
