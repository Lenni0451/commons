package net.lenni0451.commons.unchecked;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FieldInitializerTest {

    @Test
    void of() {
        String result = assertDoesNotThrow(() -> FieldInitializer.of("test").get());
        assertEquals("test", result);
    }

    @Test
    void attempt() {
        String result = assertDoesNotThrow(() -> FieldInitializer.attempt(() -> "test").get());
        assertEquals("test", result);
    }

    @Test
    void firstOf() {
        String result = assertDoesNotThrow(() -> FieldInitializer.firstOf(
                () -> {
                    throw new Exception("fail1");
                },
                () -> null,
                () -> "success",
                () -> "fail2"
        ).get());
        assertEquals("success", result);

        assertThrows(IllegalStateException.class, () -> FieldInitializer.firstOf(
                () -> {
                    throw new Exception("fail1");
                },
                () -> null,
                () -> {
                    throw new Exception("fail2");
                }
        ).get());
    }

    @Test
    void map() {
        String result = assertDoesNotThrow(() -> FieldInitializer.of(10).map(String::valueOf).get());
        assertEquals("10", result);

        result = assertDoesNotThrow(() -> FieldInitializer.of(null).map(String::valueOf).get());
        assertNull(result);
    }

    @Test
    void or() {
        String result = assertDoesNotThrow(() -> FieldInitializer.<String>of(null).or(() -> "test").get());
        assertEquals("test", result);
    }

    @Test
    void onlyIf() {
        String result = assertDoesNotThrow(() -> FieldInitializer.of("test").onlyIf(false).get());
        assertNull(result);
        result = assertDoesNotThrow(() -> FieldInitializer.of("test").onlyIf(true).get());
        assertEquals("test", result);

        result = assertDoesNotThrow(() -> FieldInitializer.of("test").onlyIf(() -> false).get());
        assertNull(result);
        result = assertDoesNotThrow(() -> FieldInitializer.of("test").onlyIf(() -> true).get());
        assertEquals("test", result);
    }

    @Test
    void ensure() {
        assertThrows(Exception.class, () -> FieldInitializer.of(null).ensure(() -> new Exception("Value is null")).get());

        String result = assertDoesNotThrow(() -> FieldInitializer.of("test").ensure(() -> new Exception("Value is null")).get());
        assertEquals("test", result);
    }

    @Test
    void handleException() {
        assertThrows(Exception.class, () -> FieldInitializer.attempt(() -> {
            throw new IllegalArgumentException();
        }).handleException(t -> new Exception()).get());

        String result = assertDoesNotThrow(() -> FieldInitializer.of("test").handleException(t -> new Exception()).get());
        assertEquals("test", result);
    }

    @Test
    void silent() {
        String result = assertDoesNotThrow(() -> FieldInitializer.<String>attempt(() -> {
            throw new Exception();
        }).silent().get());
        assertNull(result);

        result = assertDoesNotThrow(() -> FieldInitializer.of("test").silent().get());
        assertEquals("test", result);
    }

    @Test
    void uncheckedCast() {
        String result = assertDoesNotThrow(() -> FieldInitializer.<Object>of("test").<String>uncheckedCast().get());
        assertEquals("test", result);
    }

    @Test
    void get() {
        String result = assertDoesNotThrow(() -> FieldInitializer.of("test").get());
        assertEquals("test", result);
    }

    @Test
    void require() {
        String result = assertDoesNotThrow(() -> FieldInitializer.of("test").require(() -> new Exception("Value is null")));
        assertEquals("test", result);

        assertThrows(Exception.class, () -> FieldInitializer.of(null).require(() -> new Exception("Value is null")));
    }

    @Test
    void orElse() {
        String result = assertDoesNotThrow(() -> FieldInitializer.of("test").orElse("other"));
        assertEquals("test", result);

        result = assertDoesNotThrow(() -> FieldInitializer.<String>of(null).orElse("other"));
        assertEquals("other", result);
    }

}
