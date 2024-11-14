package net.lenni0451.commons.math;

import net.lenni0451.commons.collections.Lists;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SliceCalculatorTest {

    private static SliceCalculator<String> calculator;

    @BeforeAll
    static void setUp() {
        calculator = new SliceCalculator<>(Lists.arrayList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J"), 3);
    }

    @Test
    void getSliceCount() {
        assertEquals(4, calculator.getSliceCount());
    }

    @Test
    void getSlice() {
        assertEquals(Lists.arrayList("A", "B", "C"), calculator.getSlice(0));
        assertEquals(Lists.arrayList("D", "E", "F"), calculator.getSlice(1));
        assertEquals(Lists.arrayList("G", "H", "I"), calculator.getSlice(2));
        assertEquals(Lists.arrayList("J"), calculator.getSlice(3));
        assertThrows(IndexOutOfBoundsException.class, () -> calculator.getSlice(4));
    }

}
