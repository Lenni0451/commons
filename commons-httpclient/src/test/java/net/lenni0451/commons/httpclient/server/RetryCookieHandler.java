package net.lenni0451.commons.httpclient.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.lenni0451.commons.httpclient.constants.StatusCodes;

import java.io.IOException;
import java.io.OutputStream;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
            if (retryCount % 2 == 0) {
                httpExchange.getResponseHeaders().add("Retry-After", "1");
            } else {
                ZonedDateTime now = ZonedDateTime.now().plusSeconds(1);
                String httpDate = DateTimeFormatter.RFC_1123_DATE_TIME.format(now);
                httpExchange.getResponseHeaders().add("Retry-After", httpDate);
            }
            httpExchange.sendResponseHeaders(StatusCodes.SERVICE_UNAVAILABLE, -1);
        }
    }

}
