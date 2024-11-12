package net.lenni0451.commons.asm;

import org.junit.jupiter.api.Test;
import org.objectweb.asm.Opcodes;

import static org.junit.jupiter.api.Assertions.*;

class ModifiersTest {

    @Test
    void add() {
        assertEquals(0b11, Modifiers.add(0b10, 0b01));
    }

    @Test
    void remove() {
        assertEquals(0b01, Modifiers.remove(0b11, 0b10));
    }

    @Test
    void set() {
        assertEquals(0b11, Modifiers.set(0b10, 0b01, true));
        assertEquals(0b10, Modifiers.set(0b11, 0b01, false));
    }

    @Test
    void has() {
        assertTrue(Modifiers.has(0b11, 0b01));
        assertFalse(Modifiers.has(0b11, 0b100));
    }

    @Test
    void hasAny() {
        assertTrue(Modifiers.hasAny(0b11, 0b01, 0b100));
        assertFalse(Modifiers.hasAny(0b11, 0b100, 0b1000));
    }

    @Test
    void hasAll() {
        assertTrue(Modifiers.hasAll(0b11, 0b01, 0b10));
        assertFalse(Modifiers.hasAll(0b11, 0b10, 0b100));
    }

    @Test
    void setAccess() {
        assertEquals(Opcodes.ACC_STATIC | Opcodes.ACC_PUBLIC, Modifiers.setAccess(Opcodes.ACC_STATIC | Opcodes.ACC_PRIVATE, Opcodes.ACC_PUBLIC));
        assertEquals(Opcodes.ACC_STATIC | Opcodes.ACC_PUBLIC, Modifiers.setAccess(Opcodes.ACC_STATIC, Opcodes.ACC_PUBLIC));
        assertEquals(Opcodes.ACC_STATIC | Opcodes.ACC_PUBLIC, Modifiers.setAccess(Opcodes.ACC_STATIC | Opcodes.ACC_PROTECTED, Opcodes.ACC_PUBLIC));
        assertEquals(Opcodes.ACC_STATIC | Opcodes.ACC_PUBLIC, Modifiers.setAccess(Opcodes.ACC_STATIC | Opcodes.ACC_PUBLIC, Opcodes.ACC_PUBLIC));
    }

}
