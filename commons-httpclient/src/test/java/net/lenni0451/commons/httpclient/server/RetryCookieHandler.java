package net.lenni0451.commons.httpclient.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.lenni0451.commons.httpclient.constants.StatusCodes;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class RetryCookieHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Map<String, List<String>> requestHeaders = httpExchange.getRequestHeaders();
        List<String> cookies = requestHeaders.get("Cookie");

        int retryCount = 0;
        if (cookies != null) {
            for (String cookie : cookies) {
                if (cookie.startsWith("retryCount=")) {
                    retryCount = Integer.parseInt(cookie.substring("retryCount=".length()));
                }
            }
        }
        retryCount++;

        if (retryCount >= 5) {
            httpExchange.sendResponseHeaders(200, 2);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write("OK".getBytes());
            }
        } else {
            httpExchange.getResponseHeaders().add("Set-Cookie", "retryCount=" + retryCount);
            httpExchange.getResponseHeaders().add("Retry-After", "1");
            httpExchange.sendResponseHeaders(StatusCodes.SERVICE_UNAVAILABLE, -1);
        }
    }

}
