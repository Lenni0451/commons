package net.lenni0451.commons.time;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TimerTest {

    private Timer timer;

    @BeforeEach
    void setUp() {
        this.timer = new Timer(500);
    }

    @Test
    void forcePass() {
        Timer timer = new Timer(100_000_000L);
        assertFalse(timer.hasPassed());
        timer.forcePass();
        assertTrue(timer.hasPassed());
    }

    @Test
    void getPassedTime() throws InterruptedException {
        Thread.sleep(500);
        assertTrue(this.timer.getPassedTime() >= 500);
    }

    @Test
    void hasPassed() throws InterruptedException {
        assertFalse(this.timer.hasPassed());
        Thread.sleep(500);
        assertTrue(this.timer.hasPassed());
    }

    @Test
    void timeUntil() throws InterruptedException {
        assertTrue(this.timer.timeUntil() <= 500);
        Thread.sleep(500);
        assertTrue(this.timer.timeUntil() <= 0);
    }

    @Test
    void waitUntil() throws InterruptedException {
        long start = this.timer.getTime();
        this.timer.waitUntil();
        assertTrue(System.currentTimeMillis() - start >= 500);
    }

}
