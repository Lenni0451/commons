package net.lenni0451.commons.httpclient.utils;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class URLWrapperTest {

    private static URL url;

    @BeforeAll
    static void setUp() throws MalformedURLException {
        url = new URL("https://admin:123456@www.example.com:443/test?query=1#ref");
    }

    @Test
    void testGet() {
        URLWrapper wrapper = new URLWrapper(url);
        assertEquals("https", wrapper.getProtocol());
        assertEquals("www.example.com", wrapper.getHost());
        assertEquals(443, wrapper.getPort());
        assertEquals("/test", wrapper.getPath());
        assertEquals("query=1", wrapper.getQuery());
        assertEquals("admin:123456", wrapper.getUserInfo());
        assertEquals("ref", wrapper.getReference());
    }

    @Test
    void testGetURI() {
        URLWrapper wrapper = new URLWrapper(assertDoesNotThrow(() -> url.toURI()));
        assertEquals("https", wrapper.getProtocol());
        assertEquals("www.example.com", wrapper.getHost());
        assertEquals(443, wrapper.getPort());
        assertEquals("/test", wrapper.getPath());
        assertEquals("query=1", wrapper.getQuery());
        assertEquals("admin:123456", wrapper.getUserInfo());
        assertEquals("ref", wrapper.getReference());
    }

    @Test
    void testSet() {
        URLWrapper wrapper = new URLWrapper()
                .setProtocol("https")
                .setHost("www.example.com")
                .setPort(443)
                .setPath("/test")
                .setQuery("query=1")
                .setUserInfo("admin:123456")
                .setReference("ref");

        assertEquals(url, assertDoesNotThrow(wrapper::toURL));
    }

    @Test
    void testMinimalSet() {
        URLWrapper wrapper = new URLWrapper()
                .setProtocol("https")
                .setHost("www.example.com");

        assertEquals(assertDoesNotThrow(() -> new URL("https://www.example.com")), assertDoesNotThrow(wrapper::toURL));
    }

    @Test
    void testQueryWrapperGet() {
        URLWrapper wrapper = new URLWrapper(url);
        assertEquals("1", wrapper.wrapQueryParameters().getFirstValue("query").orElse(null));
    }

    @Test
    void testQueryWrapperSet() {
        URLWrapper wrapper = new URLWrapper()
                .setProtocol("https")
                .setHost("www.example.com")
                .wrapQueryParameters()
                .setParameter("Test", "Hello World")
                .setParameter("Test2", "Hello World2")
                .apply();

        assertEquals(assertDoesNotThrow(() -> new URL("https://www.example.com?Test=Hello+World&Test2=Hello+World2")), assertDoesNotThrow(wrapper::toURL));
    }

}
