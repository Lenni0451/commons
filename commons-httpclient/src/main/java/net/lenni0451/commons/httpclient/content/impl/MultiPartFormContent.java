package net.lenni0451.commons.httpclient.content.impl;

import net.lenni0451.commons.httpclient.HeaderStore;
import net.lenni0451.commons.httpclient.constants.HttpHeaders;
import net.lenni0451.commons.httpclient.content.HttpContent;
import net.lenni0451.commons.httpclient.model.ContentType;
import net.lenni0451.commons.httpclient.utils.stream.MultiInputStream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Consumer;

public class MultiPartFormContent extends HttpContent {

    private final String boundary;
    private final List<FormPart> parts = new ArrayList<>();

    public MultiPartFormContent() {
        this("---" + UUID.randomUUID() + "---");
    }

    public MultiPartFormContent(final String boundary) {
        super(new ContentType("multipart/form-data; boundary=" + boundary));
        this.boundary = boundary;
    }

    /**
     * Add a new part to the form data.
     *
     * @param name    The name of the part
     * @param content The content of the part
     * @return This instance for chaining
     * @see FormPart
     */
    public MultiPartFormContent addPart(final String name, final HttpContent content) {
        return this.addPart(new FormPart(name, content));
    }

    /**
     * Add a new part to the form data.
     *
     * @param name     The name of the part
     * @param content  The content of the part
     * @param fileName The file name of the part
     * @return This instance for chaining
     */
    public MultiPartFormContent addPart(final String name, final HttpContent content, @Nullable final String fileName) {
        return this.addPart(new FormPart(name, content, fileName));
    }

    /**
     * Add a new part to the form data.
     *
     * @param part The part to add
     * @return This instance for chaining
     */
    public MultiPartFormContent addPart(final FormPart part) {
        this.parts.add(part);
        return this;
    }

    @Override
    public boolean canBeStreamedMultipleTimes() {
        return false;
    }

    @Override
    public int getContentLength() {
        int boundaryLength = ("--" + this.boundary + "\r\n").getBytes().length;
        int[] length = {0};
        for (FormPart part : this.parts) {
            length[0] += boundaryLength;
            part.forEachHeader(header -> length[0] += header.getBytes().length);
            length[0] += 2; // \r\n between headers and content
            length[0] += part.getContent().getContentLength();
            length[0] += 2; // \r\n after content
        }
        return length[0] + boundaryLength; // final boundary, '--' has the same length as '\r\n'
    }

    @Nonnull
    @Override
    protected InputStream compute() throws IOException {
        Queue<InputStream> partialStreams = new ArrayDeque<>(this.parts.size() * 4 + 1); // estimate size, the headers will add some more streams
        byte[] lineBreak = "\r\n".getBytes();
        byte[] boundary = ("--" + this.boundary + "\r\n").getBytes();
        byte[] finalBoundary = ("--" + this.boundary + "--").getBytes();
        for (FormPart part : this.parts) {
            partialStreams.add(new ByteArrayInputStream(boundary));
            part.forEachHeader(header -> partialStreams.add(new ByteArrayInputStream(header.getBytes())));
            partialStreams.add(new ByteArrayInputStream(lineBreak));
            partialStreams.add(part.getContent().getAsStream());
            partialStreams.add(new ByteArrayInputStream(lineBreak));
        }
        partialStreams.add(new ByteArrayInputStream(finalBoundary));
        return new MultiInputStream(partialStreams);
    }


    public static class FormPart extends HeaderStore<FormPart> {
        private final HttpContent content;

        public FormPart(final String name, final HttpContent content) {
            this(name, content, null);
        }

        public FormPart(final String name, final HttpContent content, @Nullable final String fileName) {
            this.content = content;
            this.setHeader(HttpHeaders.CONTENT_DISPOSITION, "form-data; name=\"" + name + "\"" + (fileName == null ? "" : ("; filename=\"" + fileName + "\"")));
            this.setHeader(HttpHeaders.CONTENT_TYPE, content.getContentType().toString());
        }

        public HttpContent getContent() {
            return this.content;
        }

        private void forEachHeader(final Consumer<String> consumer) {
            this.forEachHeader((key, value) -> {
                consumer.accept(key + ": " + value + "\r\n");
            });
        }
    }

}
