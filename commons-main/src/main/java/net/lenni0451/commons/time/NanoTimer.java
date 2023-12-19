package net.lenni0451.commons.time;

/**
 * A simple class to ensure that a fixed amount of time has passed.<br>
 * This class uses {@link System#nanoTime()} to ensure the best possible accuracy.<br>
 * See {@link Timing} to wait for a flexible amount of time.
 */
public class NanoTimer extends Timer {

    public NanoTimer(final long delay) {
        super(delay);
    }

    @Override
    public void reset() {
        this.time = System.nanoTime();
        this.forcePass = false;
    }

    @Override
    public long getPassedTime() {
        return System.nanoTime() - this.time;
    }

    @Override
    public void waitUntil() throws InterruptedException {
        if (!this.forcePass) {
            long time = this.timeUntil();
            Thread.sleep(time / 1_000_000, (int) (time % 1_000_000));
        }
        this.reset();
    }

}
