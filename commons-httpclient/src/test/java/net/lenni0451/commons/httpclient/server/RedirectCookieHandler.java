package net.lenni0451.commons.httpclient.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.lenni0451.commons.httpclient.constants.StatusCodes;

import java.io.IOException;

public class RedirectCookieHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        //Set a cookie on the redirect hop itself; it must be stored and sent to the redirect target
        exchange.getResponseHeaders().add("Set-Cookie", "rc=1");
        exchange.getResponseHeaders().add("Location", "/cookieEcho");
        exchange.sendResponseHeaders(StatusCodes.MOVED_PERMANENTLY, -1);
        exchange.close();
    }

}
