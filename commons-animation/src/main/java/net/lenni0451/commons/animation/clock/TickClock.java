package net.lenni0451.commons.animation.clock;

/**
 * A clock that provides the current time in milliseconds.
 */
public interface TickClock {

    /**
     * The system clock using {@link System#currentTimeMillis()}.
     */
    TickClock SYSTEM = System::currentTimeMillis;

    /**
     * @return The current time in milliseconds
     */
    long getTime();

}
