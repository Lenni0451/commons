package net.lenni0451.commons.httpclient.content.impl;

import lombok.Getter;
import net.lenni0451.commons.httpclient.constants.ContentTypes;
import net.lenni0451.commons.httpclient.content.HttpContent;
import net.lenni0451.commons.httpclient.utils.URLCoder;

import javax.annotation.Nonnull;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class URLEncodedFormContent extends HttpContent {

    private final List<FormEntry> entries;
    private final Charset charset;

    public URLEncodedFormContent() {
        this(StandardCharsets.UTF_8);
    }

    public URLEncodedFormContent(final Map<String, String> entries) {
        this(entries, StandardCharsets.UTF_8);
    }

    public URLEncodedFormContent(final Charset charset) {
        super(ContentTypes.APPLICATION_FORM_URLENCODED);
        this.entries = new ArrayList<>();
        this.charset = charset;
    }

    public URLEncodedFormContent(final Map<String, String> entries, final Charset charset) {
        super(ContentTypes.APPLICATION_FORM_URLENCODED);
        this.entries = entries.entrySet().stream()
                .map(e -> new FormEntry(e.getKey(), e.getValue(), charset))
                .collect(Collectors.toList());
        this.charset = charset;
    }

    /**
     * Add a new entry to the form.
     *
     * @param key   The key
     * @param value The value
     * @return This instance for chaining
     */
    public URLEncodedFormContent put(final String key, final String value) {
        this.entries.add(new FormEntry(key, value, this.charset));
        this.clearCache();
        return this;
    }

    @Override
    public boolean canBeStreamedMultipleTimes() {
        return true;
    }

    @Override
    public int getLength() {
        int length = this.entries.size() - 1; // & characters
        for (FormEntry entry : this.entries) {
            length += entry.getLength();
        }
        return length;
    }

    @Nonnull
    @Override
    protected InputStream compute() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(this.getLength());
        for (int i = 0; i < this.entries.size(); i++) {
            FormEntry entry = this.entries.get(i);
            baos.write(entry.getKey());
            baos.write('=');
            baos.write(entry.getValue());
            if (i < this.entries.size() - 1) {
                baos.write('&');
            }
        }
        return new ByteArrayInputStream(baos.toByteArray());
    }


    @Getter
    private static class FormEntry {
        private final byte[] key;
        private final byte[] value;

        private FormEntry(final String key, final String value, final Charset charset) {
            this.key = URLCoder.encode(key).getBytes(charset);
            this.value = URLCoder.encode(value).getBytes(charset);
        }

        public int getLength() {
            return this.key.length + 1 + this.value.length; // key=value
        }
    }

}
