package net.lenni0451.commons.logging;

public interface Logger {

    void info(final String message, final Object... args);

    void warn(final String message, final Object... args);

    void error(final String message, final Object... args);

}
