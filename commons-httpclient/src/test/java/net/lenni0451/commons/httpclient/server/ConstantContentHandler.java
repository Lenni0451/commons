package net.lenni0451.commons.httpclient.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class ConstantContentHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(200, 4);
        exchange.getResponseBody().write("test".getBytes());
        exchange.close();
    }

}
