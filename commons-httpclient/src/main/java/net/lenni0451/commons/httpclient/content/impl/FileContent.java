package net.lenni0451.commons.httpclient.content.impl;

import net.lenni0451.commons.httpclient.content.HttpContent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@ParametersAreNonnullByDefault
public class FileContent implements HttpContent {

    private final File file;

    public FileContent(final File file) {
        this.file = file;
    }

    @Override
    public String getDefaultContentType() {
        return "application/octet-stream";
    }

    @Override
    public int getContentLength() {
        return (int) this.file.length();
    }

    @Override
    public void writeContent(OutputStream outputStream) throws IOException {
        try (FileInputStream is = new FileInputStream(this.file)) {
            byte[] buf = new byte[1024];
            int len;
            while ((len = is.read(buf)) != -1) outputStream.write(buf, 0, len);
        }
    }

}
