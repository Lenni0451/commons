package net.lenni0451.commons.logging.impl;

import net.lenni0451.commons.logging.Logger;
import net.lenni0451.commons.logging.LoggerFormat;
import org.slf4j.LoggerFactory;

public class Slf4jLogger implements Logger {

    private final org.slf4j.Logger logger;
    private final LoggerFormat format;

    public Slf4jLogger(final String name) {
        this(LoggerFactory.getLogger(name), LoggerFormat.CURLY_BRACKETS);
    }

    public Slf4jLogger(final String name, final LoggerFormat format) {
        this(LoggerFactory.getLogger(name), format);
    }

    public Slf4jLogger(final Class<?> clazz) {
        this(LoggerFactory.getLogger(clazz), LoggerFormat.CURLY_BRACKETS);
    }

    public Slf4jLogger(final Class<?> clazz, final LoggerFormat format) {
        this(LoggerFactory.getLogger(clazz), format);
    }

    public Slf4jLogger(final org.slf4j.Logger logger) {
        this(logger, LoggerFormat.CURLY_BRACKETS);
    }

    public Slf4jLogger(final org.slf4j.Logger logger, final LoggerFormat format) {
        this.logger = logger;
        this.format = format;
    }

    @Override
    public void info(String message, Object... args) {
        LoggerFormat.Result result = this.format.format(message, args);
        this.logger.info(result.getMessage(), result.getThrowable());
    }

    @Override
    public void warn(String message, Object... args) {
        LoggerFormat.Result result = this.format.format(message, args);
        this.logger.warn(result.getMessage(), result.getThrowable());
    }

    @Override
    public void error(String message, Object... args) {
        LoggerFormat.Result result = this.format.format(message, args);
        this.logger.error(result.getMessage(), result.getThrowable());
    }

}
