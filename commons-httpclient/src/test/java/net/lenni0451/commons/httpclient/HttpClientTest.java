package net.lenni0451.commons.httpclient;

import net.lenni0451.commons.httpclient.content.impl.FormContent;
import net.lenni0451.commons.httpclient.content.impl.StringContent;
import net.lenni0451.commons.httpclient.requests.HttpRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpClientTest {

    private HttpClient client;

    @BeforeEach
    void setUp() {
        this.client = new HttpClient();
    }

    @Test
    void testGet() {
        HttpRequest request = assertDoesNotThrow(() -> this.client.get("https://lenni0451.net/debug/echo.php"));
        HttpResponse response = assertDoesNotThrow(() -> this.client.execute(request));
        assertEquals(200, response.getStatusCode());
    }

    @Test
    void testErrorGet() {
        HttpRequest request = assertDoesNotThrow(() -> this.client.get("https://lenni0451.net/debug/msg.php?msg=123&code=404"));
        HttpResponse response = assertDoesNotThrow(() -> this.client.execute(request));
        assertEquals(404, response.getStatusCode());
        assertEquals("123", response.getContentAsString());
    }

    @Test
    void testPostString() {
        HttpRequest request = assertDoesNotThrow(() -> this.client.post("https://lenni0451.net/debug/post.php").setContent(new StringContent("Hello World")));
        HttpResponse response = assertDoesNotThrow(() -> this.client.execute(request));
        assertEquals(200, response.getStatusCode());
        assertEquals("Hello World", response.getContentAsString());
    }

    @Test
    void testPostForm() {
        HttpRequest request = assertDoesNotThrow(() -> this.client.post("https://lenni0451.net/debug/post.php").setContent(new FormContent().put("content", "Hello World")));
        HttpResponse response = assertDoesNotThrow(() -> this.client.execute(request));
        assertEquals(200, response.getStatusCode());
        assertEquals("content=Hello+World", response.getContentAsString());
    }

}
