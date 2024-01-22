package net.lenni0451.commons.httpclient.content.impl;

import net.lenni0451.commons.httpclient.constants.ContentTypes;
import net.lenni0451.commons.httpclient.content.HttpContent;
import net.lenni0451.commons.httpclient.model.ContentType;

import java.util.Arrays;

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
    public int getContentLength() {
        return this.content.length;
    }

    @Override
    protected byte[] compute() {
        return Arrays.copyOfRange(this.content, this.start, this.start + this.length);
    }

}
