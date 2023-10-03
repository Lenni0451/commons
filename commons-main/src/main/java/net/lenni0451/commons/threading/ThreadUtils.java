package net.lenni0451.commons.threading;

import java.util.concurrent.TimeUnit;

public class ThreadUtils {

    private static final long SLEEP_PRECISION = TimeUnit.MILLISECONDS.toNanos(2);
    private static final long SPIN_YIELD_PRECISION = TimeUnit.MICROSECONDS.toNanos(2);

    /**
     * Start a thread that will sleep forever.<br>
     * This increases the precision of {@link Thread#sleep(long)}.
     */
    public static void startTimerHackThread() {
        Thread thread = new Thread(() -> {
            try {
                while (true) Thread.sleep(Long.MAX_VALUE);
            } catch (InterruptedException ignored) {
            }
        }, "TimerHackThread");
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Sleeps the current thread for the given amount of time.
     *
     * @param millis The amount of milliseconds to sleep
     * @return If the sleep was not interrupted
     */
    public static boolean sleep(final long millis) {
        try {
            Thread.sleep(millis);
            return true;
        } catch (InterruptedException e) {
            return false;
        }
    }

    /**
     * Sleeps the current thread for the given amount of time.<br>
     * This method is super precise but more expensive than {@link Thread#sleep(long)}.
     *
     * @param millis The amount of milliseconds to sleep
     * @param nanos  The amount of nanoseconds to sleep
     */
    public static void hybridSleep(long millis, final long nanos) {
        long timeLeft = TimeUnit.MILLISECONDS.toNanos(millis) + nanos;
        final long end = System.nanoTime() + timeLeft;
        do {
            if (timeLeft > SLEEP_PRECISION) sleep(1);
            else if (timeLeft > SPIN_YIELD_PRECISION) Thread.yield();

            timeLeft = end - System.nanoTime();
        } while (timeLeft > 0);
    }

}
