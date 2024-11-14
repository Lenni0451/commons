package net.lenni0451.commons.time;

/**
 * A simple class to ensure that a fixed amount of time has passed.<br>
 * See {@link Timing} to wait for a flexible amount of time.
 */
public class Timer {

    protected final long delay;
    protected long time;
    protected boolean forcePass = false;

    public Timer(final long delay) {
        this.delay = delay;
        this.reset();
    }

    /**
     * @return The delay of this timer
     */
    public long getDelay() {
        return this.delay;
    }

    /**
     * @return The time since the last reset
     */
    public long getTime() {
        return this.time;
    }

    /**
     * Reset the time.
     */
    public void reset() {
        this.time = System.currentTimeMillis();
        this.forcePass = false;
    }

    /**
     * Force the timer to tick.
     */
    public void forcePass() {
        this.forcePass = true;
    }

    /**
     * @return The time that has passed since the last reset
     */
    public long getPassedTime() {
        return System.currentTimeMillis() - this.time;
    }

    /**
     * Check if the delay has passed.<br>
     * The time will be reset if it has passed.
     *
     * @return If the delay has passed
     */
    public boolean hasPassed() {
        if (this.getPassedTime() >= this.delay || this.forcePass) {
            this.reset();
            return true;
        }
        return false;
    }

    /**
     * @return The time until the delay has passed
     */
    public long timeUntil() {
        return this.delay - this.getPassedTime();
    }

    /**
     * Wait until the delay has passed.<br>
     * The time will be reset after the delay has passed.
     *
     * @throws InterruptedException If the thread is interrupted
     */
    public void waitUntil() throws InterruptedException {
        if (!this.forcePass) Thread.sleep(this.timeUntil());
        this.reset();
    }

}
