package net.lenni0451.commons.logging.special;

import lombok.Getter;
import net.lenni0451.commons.logging.Logger;

import java.util.function.Supplier;

public class LazyInitLogger implements Logger {

    private final Supplier<Logger> loggerSupplier;
    @Getter(lazy = true)
    private final Logger logger = this.loggerSupplier.get();

    public LazyInitLogger(final Supplier<Logger> loggerSupplier) {
        this.loggerSupplier = loggerSupplier;
    }

    @Override
    public void info(String message, Object... args) {
        this.getLogger().info(message, args);
    }

    @Override
    public void warn(String message, Object... args) {
        this.getLogger().warn(message, args);
    }

    @Override
    public void error(String message, Object... args) {
        this.getLogger().error(message, args);
    }

}
