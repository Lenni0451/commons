package net.lenni0451.commons.httpclient.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.lenni0451.commons.httpclient.utils.HttpRequestUtils;

import java.io.IOException;

public class ContentEchoHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        httpExchange.sendResponseHeaders(200, 0);
        httpExchange.getResponseBody().write(HttpRequestUtils.readFromStream(httpExchange.getRequestBody()));
        httpExchange.close();
    }

}
