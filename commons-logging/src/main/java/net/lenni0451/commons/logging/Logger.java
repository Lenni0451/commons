package net.lenni0451.commons.logging;

public interface Logger {

    /**
     * Log a message with the level INFO.<br>
     * Argument placeholders are defined by the logging implementation.<br>
     * If the last argument is an instance of {@link Throwable} it will be logged as an error message.
     *
     * @param message The message to log
     * @param args    The arguments for the message
     */
    void info(final String message, final Object... args);

    /**
     * Log a message with the level WARN.<br>
     * Argument placeholders are defined by the logging implementation.<br>
     * If the last argument is an instance of {@link Throwable} it will be logged as an error message.
     *
     * @param message The message to log
     * @param args    The arguments for the message
     */
    void warn(final String message, final Object... args);

    /**
     * Log a message with the level ERROR.<br>
     * Argument placeholders are defined by the logging implementation.<br>
     * If the last argument is an instance of {@link Throwable} it will be logged as an error message.
     *
     * @param message The message to log
     * @param args    The arguments for the message
     */
    void error(final String message, final Object... args);

}
