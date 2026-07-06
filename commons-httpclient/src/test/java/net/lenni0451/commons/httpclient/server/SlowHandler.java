package net.lenni0451.commons.httpclient.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.lenni0451.commons.httpclient.utils.URLWrapper;

import java.io.IOException;

public class SlowHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        long delay = new URLWrapper(exchange.getRequestURI()).wrapQueryParameters().getFirstValue("delay").map(Long::parseLong).orElse(1000L);
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        exchange.sendResponseHeaders(200, 4);
        exchange.getResponseBody().write("slow".getBytes());
        exchange.close();
    }

}
