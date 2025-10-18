package net.lenni0451.commons.httpclient.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.lenni0451.commons.httpclient.utils.URLWrapper;

import java.io.IOException;

public class ContentResponseHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        URLWrapper.QueryParametersWrapper wrapper = new URLWrapper(httpExchange.getRequestURI()).wrapQueryParameters();
        int responseCode = wrapper.getFirstValue("code").map(Integer::parseInt).orElse(200);
        String response = wrapper.getFirstValue("content").orElse("<none>");
        httpExchange.sendResponseHeaders(responseCode, response.length());
        httpExchange.getResponseBody().write(response.getBytes());
        httpExchange.close();
    }

}
