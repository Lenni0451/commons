package net.lenni0451.commons;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomUtilsTest {

    @RepeatedTest(10)
    void randomStringLength() {
        int length = RandomUtils.randomInt(1, 100);
        String randomString = RandomUtils.randomString(length);
        assertEquals(length, randomString.length());
    }

    @RepeatedTest(10)
    void randomStringLengthAndCharset() {
        int length = RandomUtils.randomInt(1, 100);
        String charset = "abc";
        String randomString = RandomUtils.randomString(length, charset);
        assertEquals(length, randomString.length());
        assertTrue(randomString.chars().allMatch(c -> charset.indexOf(c) != -1));
    }

    @RepeatedTest(10)
    void randomizeCaseLength() {
        String s = RandomUtils.randomString(10);
        String randomized = RandomUtils.randomizeCase(s);
        assertEquals(s.length(), randomized.length());
    }

    @RepeatedTest(10)
    void randomElementArray() {
        Integer[] array = {1, 2, 3, 4, 5};
        Integer randomElement = RandomUtils.randomElement(array);
        assertTrue(Arrays.asList(array).contains(randomElement));
    }

    @RepeatedTest(10)
    void randomElementList() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        Integer randomElement = RandomUtils.randomElement(list);
        assertTrue(list.contains(randomElement));
    }

    @RepeatedTest(10)
    void randomInt() {
        int i = RandomUtils.randomInt(10, 20);
        assertTrue(i >= 10);
        assertTrue(i <= 20);
    }

    @RepeatedTest(10)
    void randomLong() {
        long l = RandomUtils.randomLong(10, 20);
        assertTrue(l >= 10);
        assertTrue(l <= 20);
    }

    @RepeatedTest(10)
    void randomDouble() {
        double d = RandomUtils.randomDouble(10, 20);
        assertTrue(d >= 10);
        assertTrue(d <= 20);
    }

    @Test
    void randomBytes() {
        byte[] bytes = RandomUtils.randomBytes(10);
        assertEquals(10, bytes.length);
    }

}
