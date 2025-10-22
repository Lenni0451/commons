package net.lenni0451.commons.httpclient.content.impl;

import net.lenni0451.commons.httpclient.constants.ContentTypes;
import net.lenni0451.commons.httpclient.content.HttpContent;
import net.lenni0451.commons.httpclient.model.ContentType;

import javax.annotation.Nonnull;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ByteArrayContent extends HttpContent {

    private final byte[] content;
    private final int start;
    private final int length;

    public ByteArrayContent(final byte[] content) {
        this(content, 0, content.length);
    }

    public ByteArrayContent(final byte[] content, final int start, final int length) {
        super(ContentTypes.APPLICATION_OCTET_STREAM);
        this.content = content;
        this.start = start;
        this.length = length;
    }

    public ByteArrayContent(final ContentType contentType, final byte[] content) {
        this(contentType, content, 0, content.length);
    }

    public ByteArrayContent(final ContentType contentType, final byte[] content, final int start, final int length) {
        super(contentType);
        this.content = content;
        this.start = start;
        this.length = length;
    }

    @Override
    public boolean canBeStreamedMultipleTimes() {
        return true;
    }

    @Override
    public int getLength() {
        return this.content.length;
    }

    @Nonnull
    @Override
    protected InputStream compute() {
        return new ByteArrayInputStream(this.content, this.start, this.length);
    }

}
