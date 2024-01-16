package net.lenni0451.commons.httpclient.content.impl;

import net.lenni0451.commons.httpclient.constants.ContentTypes;
import net.lenni0451.commons.httpclient.model.ContentType;

import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@ParametersAreNonnullByDefault
public class StringContent extends ByteArrayContent {

    public StringContent(final String content) {
        this(content, StandardCharsets.UTF_8);
    }

    public StringContent(final String content, final Charset charset) {
        super(content.getBytes(charset));
    }

    public StringContent(final ContentType contentType, final String content) {
        this(contentType, content, StandardCharsets.UTF_8);
    }

    public StringContent(final ContentType contentType, final String content, final Charset charset) {
        super(contentType, content.getBytes(charset));
    }

    @Override
    public ContentType getContentType() {
        return ContentTypes.TEXT_PLAIN;
    }

}
