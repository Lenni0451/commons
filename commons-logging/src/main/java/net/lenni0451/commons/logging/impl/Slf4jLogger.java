package net.lenni0451.commons.logging.impl;

import net.lenni0451.commons.logging.Logger;
import net.lenni0451.commons.logging.MessageFormat;
import org.slf4j.LoggerFactory;

public class Slf4jLogger implements Logger {

    private final org.slf4j.Logger logger;
    private final MessageFormat messageFormat;

    public Slf4jLogger(final String name) {
        this(LoggerFactory.getLogger(name), MessageFormat.CURLY_BRACKETS);
    }

    public Slf4jLogger(final String name, final MessageFormat messageFormat) {
        this(LoggerFactory.getLogger(name), messageFormat);
    }

    public Slf4jLogger(final Class<?> clazz) {
        this(LoggerFactory.getLogger(clazz), MessageFormat.CURLY_BRACKETS);
    }

    public Slf4jLogger(final Class<?> clazz, final MessageFormat messageFormat) {
        this(LoggerFactory.getLogger(clazz), messageFormat);
    }

    public Slf4jLogger(final org.slf4j.Logger logger) {
        this(logger, MessageFormat.CURLY_BRACKETS);
    }

    public Slf4jLogger(final org.slf4j.Logger logger, final MessageFormat messageFormat) {
        this.logger = logger;
        this.messageFormat = messageFormat;
    }

    @Override
    public void info(String message, Object... args) {
        MessageFormat.Result result = this.messageFormat.format(message, args);
        this.logger.info(result.getMessage(), result.getThrowable());
    }

    @Override
    public void warn(String message, Object... args) {
        MessageFormat.Result result = this.messageFormat.format(message, args);
        this.logger.warn(result.getMessage(), result.getThrowable());
    }

    @Override
    public void error(String message, Object... args) {
        MessageFormat.Result result = this.messageFormat.format(message, args);
        this.logger.error(result.getMessage(), result.getThrowable());
    }

}
