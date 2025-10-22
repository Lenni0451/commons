package net.lenni0451.commons.httpclient.content.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MultiPartFormContentTest {

    @Test
    void test() {
        MultiPartFormContent content = new MultiPartFormContent();
        content.addPart("field1", new StringContent("value1"));
        content.addPart("field2", new StringContent("value2"), "file.txt");
        content.addPart(new MultiPartFormContent.FormPart("field3", new ByteArrayContent(new byte[100]), "data.bin").appendHeader("Test", "Lol"));
        int calculatedLength = content.getLength();
        byte[] contentBytes = assertDoesNotThrow(content::getAsBytes);
        assertEquals(contentBytes.length, calculatedLength);
    }

}
