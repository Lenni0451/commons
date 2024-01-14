package net.lenni0451.commons.httpclient.content.impl;

import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.charset.Charset;

@ParametersAreNonnullByDefault
public class StringContent extends ByteArrayContent {

    public StringContent(final String content) {
        this(content, Charset.defaultCharset());
    }

    public StringContent(final String content, final Charset charset) {
        super(content.getBytes(charset));
    }

    @Override
    public String getDefaultContentType() {
        return "text/plain";
    }

}
