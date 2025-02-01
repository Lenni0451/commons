package net.lenni0451.commons.logging.impl;

import net.lenni0451.commons.logging.Logger;
import net.lenni0451.commons.logging.MessageFormat;
import org.slf4j.LoggerFactory;

/**
 * A logger implementation that uses the SLF4J logger.
 */
public class Slf4jLogger implements Logger {

    private final org.slf4j.Logger logger;
    private final MessageFormat messageFormat;

    /**
     * Create a new Slf4jLogger instance.<br>
     * The message format is set to {@link MessageFormat#CURLY_BRACKETS}.
     *
     * @param name The name of the Logger
     */
    public Slf4jLogger(final String name) {
        this(LoggerFactory.getLogger(name), MessageFormat.CURLY_BRACKETS);
    }

    /**
     * Create a new Slf4jLogger instance.
     *
     * @param name          The name of the Logger
     * @param messageFormat The argument format for the log message
     */
    public Slf4jLogger(final String name, final MessageFormat messageFormat) {
        this(LoggerFactory.getLogger(name), messageFormat);
    }

    /**
     * Create a new Slf4jLogger instance.<br>
     * The message format is set to {@link MessageFormat#CURLY_BRACKETS}.
     *
     * @param clazz The class of the Logger
     */
    public Slf4jLogger(final Class<?> clazz) {
        this(LoggerFactory.getLogger(clazz), MessageFormat.CURLY_BRACKETS);
    }

    /**
     * Create a new Slf4jLogger instance.
     *
     * @param clazz         The class of the Logger
     * @param messageFormat The argument format for the log message
     */
    public Slf4jLogger(final Class<?> clazz, final MessageFormat messageFormat) {
        this(LoggerFactory.getLogger(clazz), messageFormat);
    }

    /**
     * Create a new Slf4jLogger instance.<br>
     * The message format is set to {@link MessageFormat#CURLY_BRACKETS}.
     *
     * @param logger The SLF4J logger
     */
    public Slf4jLogger(final org.slf4j.Logger logger) {
        this(logger, MessageFormat.CURLY_BRACKETS);
    }

    /**
     * Create a new Slf4jLogger instance.
     *
     * @param logger        The SLF4J logger
     * @param messageFormat The argument format for the log message
     */
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
