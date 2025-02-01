package net.lenni0451.commons.logging.special;

import net.lenni0451.commons.logging.Logger;

/**
 * A Logger implementation that does nothing.
 */
public class NoOpLogger implements Logger {

    @Override
    public void info(String message, Object... args) {
    }

    @Override
    public void warn(String message, Object... args) {
    }

    @Override
    public void error(String message, Object... args) {
    }

}
