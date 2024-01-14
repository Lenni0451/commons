package net.lenni0451.commons.httpclient.content.impl;

import lombok.SneakyThrows;
import net.lenni0451.commons.httpclient.content.HttpContent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ParametersAreNonnullByDefault
public class FormContent implements HttpContent {

    private final List<FormEntry> entries;
    private final Charset charset;
    private byte[] cache;

    public FormContent() {
        this(StandardCharsets.UTF_8);
    }

    public FormContent(final Map<String, String> entries) {
        this(entries, StandardCharsets.UTF_8);
    }

    public FormContent(final Charset charset) {
        this.entries = new ArrayList<>();
        this.charset = charset;
    }

    public FormContent(final Map<String, String> entries, final Charset charset) {
        this.entries = entries.entrySet().stream().map(e -> new FormEntry(e.getKey(), e.getValue())).collect(Collectors.toList());
        this.charset = charset;
    }

    public FormContent put(final String key, final String value) {
        this.entries.add(new FormEntry(key, value));
        this.cache = null;
        return this;
    }

    @Override
    public String getDefaultContentType() {
        return "application/x-www-form-urlencoded";
    }

    @Override
    public int getContentLength() {
        this.buildCache();
        return this.cache.length;
    }

    @Override
    public void writeContent(OutputStream outputStream) throws IOException {
        this.buildCache();
        outputStream.write(this.cache);
    }

    @SneakyThrows
    private void buildCache() {
        if (this.cache != null) return;
        StringBuilder builder = new StringBuilder();
        for (FormEntry entry : this.entries) {
            if (builder.length() != 0) builder.append("&");
            builder
                    .append(entry.encodeKey(this.charset))
                    .append("=")
                    .append(entry.encodeValue(this.charset));
        }
        this.cache = builder.toString().getBytes(this.charset);
    }


    private static class FormEntry {
        private final String key;
        private final String value;

        public FormEntry(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return this.key;
        }

        public String encodeKey(Charset charset) throws UnsupportedEncodingException {
            return URLEncoder.encode(this.key, charset.name());
        }

        public String getValue() {
            return this.value;
        }

        public String encodeValue(Charset charset) throws UnsupportedEncodingException {
            return URLEncoder.encode(this.value, charset.name());
        }
    }

}
