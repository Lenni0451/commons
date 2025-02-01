package net.lenni0451.commons.logging.impl;

import net.lenni0451.commons.logging.MessageFormat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * A Logger implementation that logs to a file.<br>
 * Needs to be closed after usage to release the file.
 */
public class FileLogger extends PrintStreamLogger implements AutoCloseable {

    public static Builder builder() {
        return new Builder();
    }


    private final FileOutputStream fileOutputStream;

    /**
     * Create a new FileLogger instance.
     *
     * @param fos           The {@link FileOutputStream} where the log messages should be written to
     * @param prefix        The prefix for the log message
     * @param messageFormat The argument format for the log message
     */
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

        /**
         * The {@link FileOutputStream} where the log messages should be written to.
         *
         * @param file The file where the log messages should be written to
         * @return This builder instance
         * @throws FileNotFoundException If the file could not be found
         */
        public Builder file(final File file) throws FileNotFoundException {
            return this.fileOutputStream(new FileOutputStream(file));
        }

        /**
         * The {@link FileOutputStream} where the log messages should be written to.
         *
         * @param fos The {@link FileOutputStream} where the log messages should be written to
         * @return This builder instance
         */
        public Builder fileOutputStream(final FileOutputStream fos) {
            this.fos = fos;
            return this;
        }

        /**
         * The prefix for the log message.
         *
         * @param prefix The prefix for the log message
         * @return This builder instance
         */
        public Builder prefix(final String prefix) {
            this.prefix = prefix;
            return this;
        }

        /**
         * The argument format for the log message.
         *
         * @param messageFormat The argument format for the log message
         * @return This builder instance
         */
        public Builder messageFormat(final MessageFormat messageFormat) {
            this.messageFormat = messageFormat;
            return this;
        }

        /**
         * @return The created FileLogger instance
         */
        public FileLogger build() {
            return new FileLogger(this.fos, this.prefix, this.messageFormat);
        }
    }

}
