package net.lenni0451.commons.httpclient.content.impl;

import net.lenni0451.commons.httpclient.constants.ContentTypes;
import net.lenni0451.commons.httpclient.content.HttpContent;
import net.lenni0451.commons.httpclient.model.ContentType;
import net.lenni0451.commons.httpclient.utils.HttpRequestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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
    public int getContentLength() {
        return (int) this.file.length();
    }

    @Override
    protected byte[] compute() throws IOException {
        try (FileInputStream fis = new FileInputStream(this.file)) {
            return HttpRequestUtils.readFromStream(fis);
        }
    }

}
