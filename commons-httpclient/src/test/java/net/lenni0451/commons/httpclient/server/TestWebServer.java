package net.lenni0451.commons.httpclient.server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestWebServer {

    private final HttpServer server;
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public TestWebServer() throws IOException {
        this.server = HttpServer.create();
        this.server.setExecutor(this.executor);

        this.server.createContext("/echo", new ContentEchoHandler());
        this.server.createContext("/response", new ContentResponseHandler());
        this.server.createContext("/empty", new EmptyContentHandler());
        this.server.createContext("/retryCookie", new RetryCookieHandler());
        this.server.createContext("/contentType", new ContentTypeEchoHandler());
        this.server.createContext("/redirect", new RedirectHandler());
        this.server.createContext("/constant", new ConstantContentHandler());
        this.server.createContext("/headerEcho", new HeaderEchoHandler());
        this.server.createContext("/multiHeader", new MultiHeaderHandler());
        this.server.createContext("/gzip", new GzipHandler());
        this.server.createContext("/slow", new SlowHandler());
        this.server.createContext("/cookieEcho", new CookieEchoHandler());
        this.server.createContext("/methodEcho", new MethodEchoHandler());
        this.server.createContext("/queryEcho", new QueryEchoHandler());
        this.server.createContext("/noContent", new NoContentHandler());
        this.server.createContext("/quotedCharset", new QuotedCharsetHandler());
        this.server.createContext("/redirectCookie", new RedirectCookieHandler());
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
        this.executor.shutdownNow();
    }

}
