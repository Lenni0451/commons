package net.lenni0451.commons.logging.impl;

import net.lenni0451.commons.logging.MessageFormat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class FileLogger extends PrintStreamLogger implements AutoCloseable {

    public static Builder builder() {
        return new Builder();
    }


    private final FileOutputStream fileOutputStream;

    public FileLogger(final FileOutputStream fos, final String prefix, final MessageFormat messageFormat) {
        super(new PrintStream(fos), prefix, messageFormat);
        this.fileOutputStream = fos;
    }

    @Override
    protected void log(PrintStream stream, String level, String message, Object[] args) {
        synchronized (this.fileOutputStream) {
            super.log(stream, level, message, args);
        }
    }

    @Override
    public void close() throws Exception {
        this.fileOutputStream.close();
    }


    public static class Builder {
        private FileOutputStream fos;
        private String prefix = "";
        private MessageFormat messageFormat = MessageFormat.CURLY_BRACKETS;

        public Builder file(final File file) throws FileNotFoundException {
            return this.fileOutputStream(new FileOutputStream(file));
        }

        public Builder fileOutputStream(final FileOutputStream fos) {
            this.fos = fos;
            return this;
        }

        public Builder prefix(final String prefix) {
            this.prefix = prefix;
            return this;
        }

        public Builder messageFormat(final MessageFormat messageFormat) {
            this.messageFormat = messageFormat;
            return this;
        }

        public FileLogger build() {
            return new FileLogger(this.fos, this.prefix, this.messageFormat);
        }
    }

}
