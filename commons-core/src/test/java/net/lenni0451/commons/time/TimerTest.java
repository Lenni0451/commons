package net.lenni0451.commons.time;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TimerTest {

    private static final long DELAY = 500L;
    private static final long MS_OFFSET = 1L;
    private static final long MAX_DIFF = 50L;
    private static final long MIN_DELAY = DELAY - MAX_DIFF;
    private static final long MAX_DELAY = DELAY + MAX_DIFF;

    private Timer timer;

    @BeforeEach
    void setUp() {
        this.timer = new Timer(DELAY);
    }

    @Test
    void forcePass() {
        Timer timer = new Timer(DELAY * 2);
        assertFalse(timer.hasPassed());
        timer.forcePass();
        assertTrue(timer.hasPassed());
    }

    @Test
    void getPassedTime() throws InterruptedException {
        Thread.sleep(DELAY / MS_OFFSET);
        assertTrue(this.timer.getPassedTime() >= MIN_DELAY);
    }

    @Test
    void hasPassed() throws InterruptedException {
        assertFalse(this.timer.hasPassed());
        Thread.sleep(DELAY / MS_OFFSET);
        assertTrue(this.timer.hasPassed());
    }

    @Test
    void timeUntil() throws InterruptedException {
        assertTrue(this.timer.timeUntil() <= MAX_DELAY);
        Thread.sleep(DELAY / MS_OFFSET);
        assertTrue(this.timer.timeUntil() <= MAX_DIFF);
    }

    @Test
    void waitUntil() throws InterruptedException {
        long start = this.timer.getTime();
        assertTrue(this.timer.waitUntil() > 0);
        assertTrue(System.currentTimeMillis() - start >= MIN_DELAY);
    }

}
