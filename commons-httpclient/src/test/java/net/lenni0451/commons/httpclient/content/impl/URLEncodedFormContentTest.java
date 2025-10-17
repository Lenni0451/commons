package net.lenni0451.commons.httpclient.content.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class URLEncodedFormContentTest {

    @Test
    void test() {
        URLEncodedFormContent content = new URLEncodedFormContent();
        content.put("key1", "value1");
        content.put("key 2", "value 2");
        content.put("key&3", "value&3");
        int calculatedLength = content.getContentLength();
        byte[] contentBytes = assertDoesNotThrow(content::getAsBytes);
        assertEquals(contentBytes.length, calculatedLength);
    }

}
