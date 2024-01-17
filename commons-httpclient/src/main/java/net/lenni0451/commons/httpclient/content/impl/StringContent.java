package net.lenni0451.commons.httpclient.content.impl;

import net.lenni0451.commons.httpclient.model.ContentType;

import javax.annotation.Nonnull;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class StringContent extends ByteArrayContent {

    public StringContent(@Nonnull final String content) {
        this(content, StandardCharsets.UTF_8);
    }

    public StringContent(@Nonnull final String content, @Nonnull final Charset charset) {
        super(content.getBytes(charset));
    }

    public StringContent(@Nonnull final ContentType contentType, @Nonnull final String content) {
        this(contentType, content, StandardCharsets.UTF_8);
    }

    public StringContent(@Nonnull final ContentType contentType, @Nonnull final String content, @Nonnull final Charset charset) {
        super(contentType, content.getBytes(charset));
    }

}
