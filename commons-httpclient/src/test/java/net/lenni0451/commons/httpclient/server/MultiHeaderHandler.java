package net.lenni0451.commons.httpclient.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class MultiHeaderHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("X-Multi", "first");
        exchange.getResponseHeaders().add("X-Multi", "second");
        exchange.sendResponseHeaders(200, 2);
        exchange.getResponseBody().write("ok".getBytes());
        exchange.close();
    }

}
