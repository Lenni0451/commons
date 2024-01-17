package net.lenni0451.commons.httpclient.server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class TestWebServer {

    private final HttpServer server;

    public TestWebServer() throws IOException {
        this.server = HttpServer.create();

        this.server.createContext("/echo", new ContentEchoHandler());
        this.server.createContext("/response", new ContentResponseHandler());
        this.server.createContext("/empty", new EmptyContentHandler());
    }

    public int bind() throws IOException {
        int port;
        try (ServerSocket socket = new ServerSocket(0)) {
            port = socket.getLocalPort();
        }
        this.server.bind(new InetSocketAddress("127.0.0.1", port), 0);
        this.server.start();
        return port;
    }

    public void stop() {
        this.server.stop(0);
    }

}
