package net.lenni0451.commons.time;

/**
 * A simple class to ensure that a given amount of time has passed.<br>
 * See {@link Timer} to wait for a fixed amount of time.
 */
public class Timing {

    protected long time;
    protected boolean forcePass = false;

    public Timing() {
        this.reset();
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
    }

    /**
     * Force the time to have passed.
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
     * Check if a certain amount of time has passed.
     *
     * @param time The time to check for
     * @return If the time has passed
     */
    public boolean hasPassed(final long time) {
        if (this.forcePass) {
            this.forcePass = false;
            return true;
        }
        return this.getPassedTime() >= time;
    }

    /**
     * Get the time until a certain amount of time has passed.
     *
     * @param time The time to check for
     * @return The time until the time has passed
     */
    public long timeUntil(final long time) {
        return time - this.getPassedTime();
    }

    /**
     * Wait until a certain amount of time has passed.
     *
     * @param time The time to check for
     * @throws InterruptedException If the thread is interrupted
     */
    public void waitUntil(final long time) throws InterruptedException {
        if (this.forcePass) {
            this.forcePass = false;
            return;
        }
        Thread.sleep(this.timeUntil(time));
    }

}
