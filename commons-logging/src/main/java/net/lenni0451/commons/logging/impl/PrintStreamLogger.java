package net.lenni0451.commons.logging.impl;

import net.lenni0451.commons.logging.Logger;
import net.lenni0451.commons.logging.MessageFormat;

import java.io.PrintStream;
import java.util.function.Function;

public class PrintStreamLogger implements Logger {

    private final Function<LogLevel, PrintStream> stream;
    /**
     * The prefix for the log message.<br>
     * Placeholders:<br>
     * {@code %level%} - The log level
     */
    private final String prefix;
    private final MessageFormat messageFormat;

    public PrintStreamLogger(final PrintStream stream, final String prefix, final MessageFormat messageFormat) {
        this(
                level -> stream,
                prefix,
                messageFormat
        );
    }

    public PrintStreamLogger(final Function<LogLevel, PrintStream> stream, final String prefix, final MessageFormat messageFormat) {
        this.stream = stream;
        this.prefix = prefix;
        this.messageFormat = messageFormat;
    }

    @Override
    public void info(String message, Object... args) {
        this.log(this.stream.apply(LogLevel.INFO), "INFO", message, args);
    }

    @Override
    public void warn(String message, Object... args) {
        this.log(this.stream.apply(LogLevel.WARN), "WARN", message, args);
    }

    @Override
    public void error(String message, Object... args) {
        this.log(this.stream.apply(LogLevel.ERROR), "ERROR", message, args);
    }

    protected void log(final PrintStream stream, final String level, final String message, final Object[] args) {
        MessageFormat.Result result = this.messageFormat.format(message, args);
        stream.println(this.prefix.replace("%level%", level) + result.getMessage());
        if (result.getThrowable() != null) result.getThrowable().printStackTrace(stream);
    }


    public enum LogLevel {
        INFO, WARN, ERROR
    }

}
