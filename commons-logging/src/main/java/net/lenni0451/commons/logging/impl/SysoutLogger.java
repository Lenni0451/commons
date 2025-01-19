package net.lenni0451.commons.logging.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lenni0451.commons.logging.MessageFormat;

import javax.annotation.Nullable;
import java.io.PrintStream;
import java.util.function.Supplier;

public class SysoutLogger extends PrintStreamLogger {

    public static Builder builder() {
        return new Builder();
    }


    public SysoutLogger(@Nullable final String name, final MessageFormat messageFormat, final OutStream warnStream, final boolean printLevel) {
        super(
                level -> level == LogLevel.INFO ? System.out : warnStream.getStreamSupplier().get(),
                (name == null ? "" : ("[" + name + "] ")) + (printLevel ? "(%level%) " : ""),
                messageFormat
        );
    }


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

        public Builder name(final String name) {
            this.name = name;
            return this;
        }

        public Builder messageFormat(final MessageFormat messageFormat) {
            this.messageFormat = messageFormat;
            return this;
        }

        public Builder warnStream(final OutStream warnStream) {
            this.warnStream = warnStream;
            return this;
        }

        public Builder printLevel(final boolean printLevel) {
            this.printLevel = printLevel;
            return this;
        }

        public SysoutLogger build() {
            return new SysoutLogger(this.name, this.messageFormat, this.warnStream, this.printLevel);
        }
    }

}
