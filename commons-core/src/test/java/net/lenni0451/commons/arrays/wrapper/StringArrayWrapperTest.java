package net.lenni0451.commons.arrays.wrapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringArrayWrapperTest {

    private static final String[] ARRAY = {"true", "1", "2", "3", "4", "5", "6", "7"};
    private static final String[] SUB_ARRAY = {"5", "6", "7"};

    private StringArrayWrapper stringArrayWrapper;

    @BeforeEach
    void init() {
        this.stringArrayWrapper = new StringArrayWrapper(ARRAY);
    }

    @Test
    void wrappedSliceStart() {
        assertArrayEquals(SUB_ARRAY, this.stringArrayWrapper.wrappedSlice(5).getArray());
    }

    @Test
    void wrappedSliceStartEnd() {
        assertArrayEquals(SUB_ARRAY, this.stringArrayWrapper.wrappedSlice(5, 8).getArray());
    }

    @Test
    void is() {
        assertTrue(this.stringArrayWrapper.isBoolean(0));
        assertFalse(this.stringArrayWrapper.isByte(0));
        assertFalse(this.stringArrayWrapper.isShort(0));
        assertFalse(this.stringArrayWrapper.isChar(0));
        assertFalse(this.stringArrayWrapper.isInt(0));
        assertFalse(this.stringArrayWrapper.isLong(0));
        assertFalse(this.stringArrayWrapper.isFloat(0));
        assertFalse(this.stringArrayWrapper.isDouble(0));
        for (int i = 1; i < 7; i++) {
            assertTrue(this.stringArrayWrapper.isBoolean(i));
            assertTrue(this.stringArrayWrapper.isByte(i));
            assertTrue(this.stringArrayWrapper.isShort(i));
            assertTrue(this.stringArrayWrapper.isChar(i));
            assertTrue(this.stringArrayWrapper.isInt(i));
            assertTrue(this.stringArrayWrapper.isLong(i));
            assertTrue(this.stringArrayWrapper.isFloat(i));
            assertTrue(this.stringArrayWrapper.isDouble(i));
        }
    }

    @Test
    void get() {
        assertEquals(true, this.stringArrayWrapper.getBoolean(0));
        assertEquals((byte) 1, this.stringArrayWrapper.getByte(1));
        assertEquals((short) 2, this.stringArrayWrapper.getShort(2));
        assertEquals('3', this.stringArrayWrapper.getChar(3));
        assertEquals(4, this.stringArrayWrapper.getInt(4));
        assertEquals(5L, this.stringArrayWrapper.getLong(5));
        assertEquals(6F, this.stringArrayWrapper.getFloat(6));
        assertEquals(7D, this.stringArrayWrapper.getDouble(7));
    }

    @Test
    void getDefault() {
        assertEquals(false, this.stringArrayWrapper.getBoolean(-1, false));
        assertEquals((byte) 0, this.stringArrayWrapper.getByte(-1, (byte) 0));
        assertEquals((short) 0, this.stringArrayWrapper.getShort(-1, (short) 0));
        assertEquals('\0', this.stringArrayWrapper.getChar(-1, '\0'));
        assertEquals(0, this.stringArrayWrapper.getInt(-1, 0));
        assertEquals(0L, this.stringArrayWrapper.getLong(-1, 0L));
        assertEquals(0F, this.stringArrayWrapper.getFloat(-1, 0F));
        assertEquals(0D, this.stringArrayWrapper.getDouble(-1, 0D));
    }

}
