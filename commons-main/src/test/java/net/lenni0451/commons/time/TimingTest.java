package net.lenni0451.commons.time;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TimingTest {

    private Timing timing;

    @BeforeEach
    void setUp() {
        this.timing = new Timing();
    }

    @Test
    void forcePass() {
        assertFalse(this.timing.hasPassed(100_000_000L));
        this.timing.forcePass();
        assertTrue(this.timing.hasPassed(100_000_000L));
    }

    @Test
    void getPassedTime() throws InterruptedException {
        Thread.sleep(500);
        assertTrue(this.timing.getPassedTime() >= 500);
    }

    @Test
    void hasPassed() throws InterruptedException {
        assertFalse(this.timing.hasPassed(500));
        Thread.sleep(500);
        assertTrue(this.timing.hasPassed(500));
    }

    @Test
    void timeUntil() throws InterruptedException {
        assertTrue(this.timing.timeUntil(500) <= 500);
        Thread.sleep(500);
        assertTrue(this.timing.timeUntil(500) <= 0);
    }

    @Test
    void waitUntil() throws InterruptedException {
        long start = this.timing.getTime();
        this.timing.waitUntil(500);
        assertTrue(System.currentTimeMillis() - start >= 500);
    }

}
