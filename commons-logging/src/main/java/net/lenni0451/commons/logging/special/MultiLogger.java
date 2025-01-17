package net.lenni0451.commons.logging.special;

import net.lenni0451.commons.logging.Logger;

public class MultiLogger implements Logger {

    private final Logger[] loggers;

    public MultiLogger(final Logger... loggers) {
        this.loggers = loggers;
    }

    @Override
    public void info(String message, Object... args) {
        for (Logger logger : this.loggers) {
            logger.info(message, args);
        }
    }

    @Override
    public void warn(String message, Object... args) {
        for (Logger logger : this.loggers) {
            logger.warn(message, args);
        }
    }

    @Override
    public void error(String message, Object... args) {
        for (Logger logger : this.loggers) {
            logger.error(message, args);
        }
    }

}
