package net.lenni0451.commons.logging.impl;

import net.lenni0451.commons.logging.Logger;
import net.lenni0451.commons.logging.LoggerFormat;

import javax.annotation.WillNotClose;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class FileLogger implements Logger, AutoCloseable {

    private final FileOutputStream fileOutputStream;
    private final PrintWriter printWriter;
    private final LoggerFormat format;

    public FileLogger(@WillNotClose final File file) throws FileNotFoundException {
        this(new FileOutputStream(file), LoggerFormat.CURLY_BRACKETS);
    }

    public FileLogger(@WillNotClose final File file, final LoggerFormat format) throws FileNotFoundException {
        this(new FileOutputStream(file), format);
    }

    public FileLogger(@WillNotClose final FileOutputStream fileOutputStream) {
        this(fileOutputStream, LoggerFormat.CURLY_BRACKETS);
    }

    public FileLogger(@WillNotClose final FileOutputStream fileOutputStream, final LoggerFormat format) {
        this.fileOutputStream = fileOutputStream;
        this.printWriter = new PrintWriter(fileOutputStream);
        this.format = format;
    }

    @Override
    public void info(String message, Object... args) {
        this.log("INFO", message, args);
    }

    @Override
    public void warn(String message, Object... args) {
        this.log("WARN", message, args);
    }

    @Override
    public void error(String message, Object... args) {
        this.log("ERROR", message, args);
    }

    @Override
    public void close() throws Exception {
        this.fileOutputStream.close();
    }

    private void log(final String level, final String message, final Object[] args) {
        synchronized (this.printWriter) {
            LoggerFormat.Result result = this.format.format(message, args);
            this.printWriter.println("[" + level + "] " + result.getMessage());
            if (result.getThrowable() != null) result.getThrowable().printStackTrace(this.printWriter);
            this.printWriter.flush();
        }
    }

}
