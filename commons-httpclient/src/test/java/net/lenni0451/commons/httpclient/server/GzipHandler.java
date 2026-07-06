package net.lenni0451.commons.httpclient.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPOutputStream;

public class GzipHandler implements HttpHandler {

    public static final String CONTENT = "Hello Gzip";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (GZIPOutputStream gzip = new GZIPOutputStream(baos)) {
            gzip.write(CONTENT.getBytes(StandardCharsets.UTF_8));
        }
        byte[] compressed = baos.toByteArray();
        exchange.getResponseHeaders().add("Content-Encoding", "gzip");
        exchange.sendResponseHeaders(200, compressed.length);
        exchange.getResponseBody().write(compressed);
        exchange.close();
    }

}
