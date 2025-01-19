package net.lenni0451.commons.logging.impl;

import net.lenni0451.commons.logging.Logger;
import net.lenni0451.commons.logging.MessageFormat;

import java.util.logging.Level;

public class JavaLogger implements Logger {

    private final java.util.logging.Logger logger;
    private final MessageFormat messageFormat;

    public JavaLogger(final String name) {
        this(name, MessageFormat.CURLY_BRACKETS);
    }

    public JavaLogger(final String name, final MessageFormat messageFormat) {
        this(java.util.logging.Logger.getLogger(name), messageFormat);
    }

    public JavaLogger(final java.util.logging.Logger logger) {
        this(logger, MessageFormat.CURLY_BRACKETS);
    }

    public JavaLogger(final java.util.logging.Logger logger, final MessageFormat messageFormat) {
        this.logger = logger;
        this.messageFormat = messageFormat;
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
        MessageFormat.Result result = this.messageFormat.format(message, args);
        this.logger.log(level, result.getMessage(), result.getThrowable());
    }

}
