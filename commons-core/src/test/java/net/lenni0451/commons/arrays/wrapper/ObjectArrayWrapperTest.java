package net.lenni0451.commons.arrays.wrapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.*;

class ObjectArrayWrapperTest {

    private static final Object[] ARRAY = {Boolean.TRUE, (byte) 1, (short) 2, '\3', 4, 5L, 6F, 7D};
    private static final Object[] SUB_ARRAY = {5L, 6F, 7D};

    private ObjectArrayWrapper objectArrayWrapper;

    @BeforeEach
    void init() {
        this.objectArrayWrapper = new ObjectArrayWrapper(ARRAY);
    }

    @Test
    void wrappedSliceStart() {
        assertArrayEquals(SUB_ARRAY, this.objectArrayWrapper.wrappedSlice(5).getArray());
    }

    @Test
    void wrappedSliceStartEnd() {
        assertArrayEquals(SUB_ARRAY, this.objectArrayWrapper.wrappedSlice(5, 8).getArray());
    }

    @Test
    void is() {
        this.checkIs(0, ObjectArrayWrapper::isBoolean);
        this.checkIs(1, ObjectArrayWrapper::isByte);
        this.checkIs(2, ObjectArrayWrapper::isShort);
        this.checkIs(3, ObjectArrayWrapper::isChar);
        this.checkIs(4, ObjectArrayWrapper::isInt);
        this.checkIs(5, ObjectArrayWrapper::isLong);
        this.checkIs(6, ObjectArrayWrapper::isFloat);
        this.checkIs(7, ObjectArrayWrapper::isDouble);
    }

    @Test
    void get() {
        assertEquals(true, this.objectArrayWrapper.getBoolean(0));
        assertEquals((byte) 1, this.objectArrayWrapper.getByte(1));
        assertEquals((short) 2, this.objectArrayWrapper.getShort(2));
        assertEquals('\3', this.objectArrayWrapper.getChar(3));
        assertEquals(4, this.objectArrayWrapper.getInt(4));
        assertEquals(5L, this.objectArrayWrapper.getLong(5));
        assertEquals(6F, this.objectArrayWrapper.getFloat(6));
        assertEquals(7D, this.objectArrayWrapper.getDouble(7));
    }

    @Test
    void getDefault() {
        assertEquals(false, this.objectArrayWrapper.getBoolean(-1, false));
        assertEquals((byte) 0, this.objectArrayWrapper.getByte(-1, (byte) 0));
        assertEquals((short) 0, this.objectArrayWrapper.getShort(-1, (short) 0));
        assertEquals('\0', this.objectArrayWrapper.getChar(-1, '\0'));
        assertEquals(0, this.objectArrayWrapper.getInt(-1, 0));
        assertEquals(0L, this.objectArrayWrapper.getLong(-1, 0L));
        assertEquals(0F, this.objectArrayWrapper.getFloat(-1, 0F));
        assertEquals(0D, this.objectArrayWrapper.getDouble(-1, 0D));
    }


    private void checkIs(final int index, final BiFunction<ObjectArrayWrapper, Integer, Boolean> function) {
        for (int i = 0; i < ARRAY.length; i++) {
            if (i == index) assertTrue(function.apply(this.objectArrayWrapper, i));
            else assertFalse(function.apply(this.objectArrayWrapper, i));
        }
    }

}
