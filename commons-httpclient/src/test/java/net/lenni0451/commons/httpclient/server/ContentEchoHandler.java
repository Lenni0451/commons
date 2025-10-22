package net.lenni0451.commons.httpclient.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ContentEchoHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        httpExchange.sendResponseHeaders(200, 0);
        this.transfer(httpExchange.getRequestBody(), httpExchange.getResponseBody());
        httpExchange.close();
    }

    private void transfer(final InputStream in, final OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) >= 0) {
            out.write(buffer, 0, len);
        }
    }

}
