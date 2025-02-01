package net.lenni0451.commons.logging.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lenni0451.commons.logging.MessageFormat;

import javax.annotation.Nullable;
import java.io.PrintStream;
import java.util.function.Supplier;

/**
 * A Logger implementation that logs to the {@link System#out} or {@link System#err} stream.
 */
public class SysoutLogger extends PrintStreamLogger {

    public static Builder builder() {
        return new Builder();
    }


    /**
     * Create a new SysoutLogger instance.
     *
     * @param name          The name of the Logger, will be displayed in the log message if not null
     * @param messageFormat The argument format for the log message
     * @param warnStream    The stream where the WARN messages should be printed to
     * @param printLevel    If the log level should be printed in the log message
     */
    public SysoutLogger(@Nullable final String name, final MessageFormat messageFormat, final OutStream warnStream, final boolean printLevel) {
        super(
                level -> level == LogLevel.INFO ? System.out : warnStream.getStreamSupplier().get(),
                (name == null ? "" : ("[" + name + "] ")) + (printLevel ? "(%level%) " : ""),
                messageFormat
        );
    }


    /**
     * The stream where the WARN messages should be printed to.
     */
    @Getter
    @AllArgsConstructor
    public enum OutStream {
        OUT(() -> System.out), ERR(() -> System.err);

        private final Supplier<PrintStream> streamSupplier;
    }

    public static class Builder {
        @Nullable
        private String name;
        private MessageFormat messageFormat = MessageFormat.CURLY_BRACKETS;
        private OutStream warnStream = OutStream.ERR;
        private boolean printLevel = true;

        /**
         * The name of the Logger, will be displayed in the log message.<br>
         * If null, the name will not be displayed.
         *
         * @param name The name of the Logger
         * @return The builder instance
         */
        public Builder name(@Nullable final String name) {
            this.name = name;
            return this;
        }

        /**
         * The argument format for the log message.<br>
         * Default is {@link MessageFormat#CURLY_BRACKETS}
         *
         * @param messageFormat The argument format for the log message
         * @return The builder instance
         */
        public Builder messageFormat(final MessageFormat messageFormat) {
            this.messageFormat = messageFormat;
            return this;
        }

        /**
         * The stream where the WARN messages should be printed to.<br>
         * Default is {@link OutStream#ERR}
         *
         * @param warnStream The stream where the WARN messages should be printed to
         * @return The builder instance
         */
        public Builder warnStream(final OutStream warnStream) {
            this.warnStream = warnStream;
            return this;
        }

        /**
         * If the log level should be printed in the log message.<br>
         * Default is true
         *
         * @param printLevel If the log level should be printed in the log message
         * @return The builder instance
         */
        public Builder printLevel(final boolean printLevel) {
            this.printLevel = printLevel;
            return this;
        }

        /**
         * @return The created SysoutLogger instance
         */
        public SysoutLogger build() {
            return new SysoutLogger(this.name, this.messageFormat, this.warnStream, this.printLevel);
        }
    }

}
