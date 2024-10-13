package net.lenni0451.commons.httpclient.content.impl;

import lombok.SneakyThrows;
import net.lenni0451.commons.httpclient.HeaderStore;
import net.lenni0451.commons.httpclient.constants.Headers;
import net.lenni0451.commons.httpclient.content.HttpContent;
import net.lenni0451.commons.httpclient.model.ContentType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    @SneakyThrows
    public int getContentLength() {
        return this.getAsBytes().length;
    }

    @Nonnull
    @Override
    protected byte[] compute() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (FormPart part : this.parts) {
            baos.write(("--" + this.boundary + "\r\n").getBytes());
            for (Map.Entry<String, List<String>> entry : part.getHeaders().entrySet()) {
                for (String value : entry.getValue()) {
                    baos.write((entry.getKey() + ": " + value + "\r\n").getBytes());
                }
            }
            baos.write("\r\n".getBytes());
            baos.write(part.getContent().getAsBytes());
            baos.write("\r\n".getBytes());
        }
        baos.write(("--" + this.boundary + "--").getBytes());
        return baos.toByteArray();
    }


    public static class FormPart extends HeaderStore<FormPart> {
        private final HttpContent content;

        public FormPart(final String name, final HttpContent content) {
            this(name, content, null);
        }

        public FormPart(final String name, final HttpContent content, @Nullable final String fileName) {
            this.content = content;
            this.setHeader(Headers.CONTENT_DISPOSITION, "form-data; name=\"" + name + "\"" + (fileName == null ? "" : ("; fileName=\"" + fileName + "\"")));
            this.setHeader(Headers.CONTENT_TYPE, content.getContentType().toString());
        }

        public HttpContent getContent() {
            return this.content;
        }
    }

}
