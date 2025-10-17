package net.lenni0451.commons.httpclient.content.impl;

import net.lenni0451.commons.httpclient.content.HttpContent;
import net.lenni0451.commons.httpclient.model.ContentType;

import javax.annotation.Nonnull;
import java.io.InputStream;

public class InputStreamContent extends HttpContent {

    private final InputStream inputStream;
    private final int contentLength;

    public InputStreamContent(final ContentType contentType, final InputStream inputStream, final int contentLength) {
        super(contentType);
        this.inputStream = inputStream;
        this.contentLength = contentLength;
    }

    @Override
    public boolean canBeStreamedMultipleTimes() {
        return false;
    }

    @Override
    public int getContentLength() {
        return this.contentLength;
    }

    @Nonnull
    @Override
    protected InputStream compute() {
        return this.inputStream;
    }

}
