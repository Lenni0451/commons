package net.lenni0451.commons.httpclient.content.impl;

import net.lenni0451.commons.httpclient.constants.ContentTypes;
import net.lenni0451.commons.httpclient.content.HttpContent;
import net.lenni0451.commons.httpclient.model.ContentType;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class FileContent extends HttpContent {

    private final File file;

    public FileContent(final File file) {
        this(ContentTypes.APPLICATION_OCTET_STREAM, file);
    }

    public FileContent(final ContentType contentType, final File file) {
        super(contentType);
        this.file = file;
    }

    @Override
    public boolean canBeStreamedMultipleTimes() {
        return true;
    }

    @Override
    public int getContentLength() {
        return (int) this.file.length();
    }

    @Nonnull
    @Override
    protected InputStream compute() throws IOException {
        return Files.newInputStream(this.file.toPath());
    }

}
