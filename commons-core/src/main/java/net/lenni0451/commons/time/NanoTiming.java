package net.lenni0451.commons.time;

/**
 * A simple class to ensure that a given amount of time has passed.<br>
 * This class uses {@link System#nanoTime()} to ensure the best possible accuracy.<br>
 * See {@link Timer} to wait for a fixed amount of time.
 */
public class NanoTiming extends Timing {

    @Override
    public void reset() {
        this.time = System.nanoTime();
    }

    @Override
    public long getPassedTime() {
        return System.nanoTime() - this.time;
    }

    @Override
    public void waitUntil(final long time) throws InterruptedException {
        long timeUntil = this.timeUntil(time);
        Thread.sleep(timeUntil / 1_000_000, (int) (timeUntil % 1_000_000));
    }

}
