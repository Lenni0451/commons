package net.lenni0451.commons.httpclient.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.lenni0451.commons.httpclient.utils.URLWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HeaderEchoHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String name = new URLWrapper(exchange.getRequestURI()).wrapQueryParameters().getFirstValue("name").orElse("");
        List<String> values = exchange.getRequestHeaders().get(name);
        String response = values == null ? "<none>" : String.join(",", values);
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(200, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.close();
    }

}
