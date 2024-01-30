package net.lenni0451.commons.httpclient.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class ContentTypeEchoHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String contentType = exchange.getRequestHeaders().getFirst("Content-Type");
        if (contentType == null) {
            exchange.sendResponseHeaders(400, 0);
            exchange.close();
        } else {
            exchange.sendResponseHeaders(200, contentType.length());
            exchange.getResponseBody().write(contentType.getBytes());
            exchange.close();
        }
    }

}
