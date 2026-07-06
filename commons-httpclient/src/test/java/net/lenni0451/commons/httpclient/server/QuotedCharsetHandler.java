package net.lenni0451.commons.httpclient.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class QuotedCharsetHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        //A quoted charset is legal per RFC 9110 and must not crash the content type parser
        exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=\"utf-8\"");
        byte[] bytes = "quoted".getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(200, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.close();
    }

}
