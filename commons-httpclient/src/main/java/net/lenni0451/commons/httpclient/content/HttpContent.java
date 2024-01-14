package net.lenni0451.commons.httpclient.content;

import net.lenni0451.commons.httpclient.content.impl.ByteArrayContent;
import net.lenni0451.commons.httpclient.content.impl.FileContent;
import net.lenni0451.commons.httpclient.content.impl.FormContent;
import net.lenni0451.commons.httpclient.content.impl.StringContent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Map;

@ParametersAreNonnullByDefault
public interface HttpContent {

    static HttpContent bytes(final byte[] content) {
        return new ByteArrayContent(content);
    }

    static HttpContent bytes(final byte[] content, final int offset, final int length) {
        return new ByteArrayContent(content, offset, length);
    }

    static HttpContent string(final String content) {
        return new StringContent(content);
    }

    static HttpContent string(final String content, final Charset charset) {
        return new StringContent(content, charset);
    }

    static HttpContent file(final File file) {
        return new FileContent(file);
    }

    static HttpContent form(final String key, final String value) {
        return new FormContent().put(key, value);
    }

    static HttpContent form(final Map<String, String> form) {
        return new FormContent(form);
    }


    String getDefaultContentType();

    int getContentLength();

    void writeContent(final OutputStream outputStream) throws IOException;

}
