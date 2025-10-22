package net.lenni0451.commons.httpclient.content;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;

public class DelegatingHttpContent extends HttpContent {

    private final HttpContent content;

    public DelegatingHttpContent(final HttpContent content) {
        super(content.getType());
        this.content = content;
    }

    @Override
    public boolean canBeStreamedMultipleTimes() {
        return this.content.canBeStreamedMultipleTimes();
    }

    @Override
    public int getLength() {
        return this.content.getLength();
    }

    @Nonnull
    @Override
    protected InputStream compute() throws IOException {
        return this.content.compute();
    }

    @Override
    protected InputStream modify(InputStream inputStream) throws IOException {
        return this.content.modify(inputStream);
    }

}
