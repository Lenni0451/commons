package net.lenni0451.commons.logging.impl;

import net.lenni0451.commons.logging.Logger;
import net.lenni0451.commons.logging.LoggerFormat;

import java.util.logging.Level;

public class JavaLogger implements Logger {

    private final java.util.logging.Logger logger;
    private final LoggerFormat format;

    public JavaLogger(final String name) {
        this(name, LoggerFormat.CURLY_BRACKETS);
    }

    public JavaLogger(final String name, final LoggerFormat format) {
        this(java.util.logging.Logger.getLogger(name), format);
    }

    public JavaLogger(final java.util.logging.Logger logger) {
        this(logger, LoggerFormat.CURLY_BRACKETS);
    }

    public JavaLogger(final java.util.logging.Logger logger, final LoggerFormat format) {
        this.logger = logger;
        this.format = format;
    }


    @Override
    public void info(String message, Object... args) {
        this.log(Level.INFO, message, args);
    }

    @Override
    public void warn(String message, Object... args) {
        this.log(Level.WARNING, message, args);
    }

    @Override
    public void error(String message, Object... args) {
        this.log(Level.SEVERE, message, args);
    }

    private void log(final Level level, final String message, final Object[] args) {
        LoggerFormat.Result result = this.format.format(message, args);
        this.logger.log(level, result.getMessage(), result.getThrowable());
    }

}
