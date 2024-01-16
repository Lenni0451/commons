package net.lenni0451.commons.httpclient.content.impl;

import net.lenni0451.commons.httpclient.content.HttpContent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;

@ParametersAreNonnullByDefault
public class ByteArrayContent extends HttpContent {

    private final byte[] content;
    private final int start;
    private final int length;

    public ByteArrayContent(final byte[] content) {
        this(content, 0, content.length);
    }

    public ByteArrayContent(final byte[] content, final int start, final int length) {
        this.content = content;
        this.start = start;
        this.length = length;
    }

    @Override
    public String getDefaultContentType() {
        return "application/octet-stream";
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
