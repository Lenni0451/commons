package net.lenni0451.commons.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NumberCheckerTest {

    @Test
    void isByte() {
        assertTrue(NumberChecker.isByte("1"));
        assertTrue(NumberChecker.isByte(String.valueOf(Byte.MAX_VALUE)));
        assertTrue(NumberChecker.isByte(String.valueOf(Byte.MIN_VALUE)));
        assertFalse(NumberChecker.isByte(String.valueOf(Byte.MAX_VALUE + 1)));
        assertFalse(NumberChecker.isByte(String.valueOf(Byte.MIN_VALUE - 1)));
    }

    @Test
    void isShort() {
        assertTrue(NumberChecker.isShort("1"));
        assertTrue(NumberChecker.isShort(String.valueOf(Short.MAX_VALUE)));
        assertTrue(NumberChecker.isShort(String.valueOf(Short.MIN_VALUE)));
        assertFalse(NumberChecker.isShort(String.valueOf(Short.MAX_VALUE + 1)));
        assertFalse(NumberChecker.isShort(String.valueOf(Short.MIN_VALUE - 1)));
    }

    @Test
    void isInteger() {
        assertTrue(NumberChecker.isInteger("1"));
        assertTrue(NumberChecker.isInteger(String.valueOf(Integer.MAX_VALUE)));
        assertTrue(NumberChecker.isInteger(String.valueOf(Integer.MIN_VALUE)));
        assertFalse(NumberChecker.isInteger(String.valueOf(Integer.MAX_VALUE + 1L)));
        assertFalse(NumberChecker.isInteger(String.valueOf(Integer.MIN_VALUE - 1L)));
    }

    @Test
    void isLong() {
        assertTrue(NumberChecker.isLong("1"));
        assertTrue(NumberChecker.isLong(String.valueOf(Long.MAX_VALUE)));
        assertTrue(NumberChecker.isLong(String.valueOf(Long.MIN_VALUE)));
        assertFalse(NumberChecker.isLong(Long.MAX_VALUE + "1"));
        assertFalse(NumberChecker.isLong(Long.MIN_VALUE + "1"));
    }

    @Test
    void isFloat() {
        assertTrue(NumberChecker.isFloat("1"));
        assertTrue(NumberChecker.isFloat(String.valueOf(Float.MAX_VALUE)));
        assertTrue(NumberChecker.isFloat(String.valueOf(Float.MIN_VALUE)));
    }

    @Test
    void isDouble() {
        assertTrue(NumberChecker.isDouble("1"));
        assertTrue(NumberChecker.isDouble(String.valueOf(Double.MAX_VALUE)));
        assertTrue(NumberChecker.isDouble(String.valueOf(Double.MIN_VALUE)));
    }

}
