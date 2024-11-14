package net.lenni0451.commons.time;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NanoTimingTest {

    private static final long DELAY = 500_000_000L;
    private static final long MS_OFFSET = 1_000_000L;
    private static final long MAX_DIFF = 50_000_000L;
    private static final long MIN_DELAY = DELAY - MAX_DIFF;
    private static final long MAX_DELAY = DELAY + MAX_DIFF;

    private NanoTiming timing;

    @BeforeEach
    void setUp() {
        this.timing = new NanoTiming();
    }

    @Test
    void forcePass() {
        assertFalse(this.timing.hasPassed(DELAY * 2));
        this.timing.forcePass();
        assertTrue(this.timing.hasPassed(DELAY * 2));
    }

    @Test
    void getPassedTime() throws InterruptedException {
        Thread.sleep(DELAY / MS_OFFSET);
        assertTrue(this.timing.getPassedTime() >= MIN_DELAY);
    }

    @Test
    void hasPassed() throws InterruptedException {
        assertFalse(this.timing.hasPassed(MAX_DELAY));
        Thread.sleep(DELAY / MS_OFFSET);
        assertTrue(this.timing.hasPassed(MIN_DELAY));
    }

    @Test
    void timeUntil() throws InterruptedException {
        assertTrue(this.timing.timeUntil(DELAY) <= MAX_DELAY);
        Thread.sleep(DELAY / MS_OFFSET);
        assertTrue(this.timing.timeUntil(DELAY) <= MAX_DIFF);
    }

    @Test
    void waitUntil() throws InterruptedException {
        long start = this.timing.getTime();
        this.timing.waitUntil(DELAY);
        assertTrue(System.nanoTime() - start >= MIN_DELAY);
    }

}
