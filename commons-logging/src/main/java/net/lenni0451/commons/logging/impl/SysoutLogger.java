package net.lenni0451.commons.logging.impl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import net.lenni0451.commons.logging.Logger;
import net.lenni0451.commons.logging.LoggerFormat;

import javax.annotation.Nullable;
import java.io.PrintStream;
import java.util.function.Supplier;

@Builder
@AllArgsConstructor
public class SysoutLogger implements Logger {

    @Nullable
    @Builder.Default
    private final String name = null;
    @Builder.Default
    private final LoggerFormat format = LoggerFormat.CURLY_BRACKETS;
    @Builder.Default
    private final OutStream warnStream = OutStream.ERR;
    @Builder.Default
    private final boolean printLevel = true;

    @Override
    public void info(String message, Object... args) {
        this.log(OutStream.OUT, "INFO", message, args);
    }

    @Override
    public void warn(String message, Object... args) {
        this.log(this.warnStream, "WARN", message, args);
    }

    @Override
    public void error(String message, Object... args) {
        this.log(OutStream.ERR, "ERROR", message, args);
    }

    private void log(final OutStream outStream, final String level, final String message, final Object... args) {
        LoggerFormat.Result result = this.format.format(message, args);
        PrintStream stream = outStream.getStreamSupplier().get();
        stream.println((this.name != null ? ("[" + this.name + "] ") : "") + (this.printLevel ? ("(" + level + ") ") : "") + result.getMessage());
        if (result.getThrowable() != null) result.getThrowable().printStackTrace(stream);
    }


    @Getter
    @AllArgsConstructor
    public enum OutStream {
        OUT(() -> System.out), ERR(() -> System.err);

        private final Supplier<PrintStream> streamSupplier;
    }

}
