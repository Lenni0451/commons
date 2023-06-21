package net.lenni0451.commons.debugging;

/**
 * This class can be used to count method calls per second.
 */
public class MethodCallCounter {

    private static long lastReset = System.currentTimeMillis();
    private static long calls = 0;

    /**
     * Call this method to count the calls.<br>
     * This method will print the calls every second into {@link System#err}.
     */
    public static void call() {
        if (System.currentTimeMillis() - lastReset > 1000) {
            System.err.println("MethodCallCounter: " + calls + "/s");
            lastReset = System.currentTimeMillis();
            calls = 0;
        }
        calls++;
    }

}
