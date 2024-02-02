package net.lenni0451.commons.httpclient.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.lenni0451.commons.httpclient.constants.StatusCodes;

import java.io.IOException;

public class RedirectHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Location", "/constant");
        exchange.sendResponseHeaders(StatusCodes.MOVED_PERMANENTLY, 8);
        exchange.getResponseBody().write("redirect".getBytes());
        exchange.close();
    }

}
