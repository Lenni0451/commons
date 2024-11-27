package net.lenni0451.commons.gson;

import com.google.gson.stream.JsonReader;
import net.lenni0451.commons.gson.elements.GsonElement;
import net.lenni0451.commons.gson.elements.GsonObject;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GsonParserTest {

    private static final GsonElement ELEMENT = new GsonObject().add("key", "value").add("key2", new GsonObject().add("key3", "value3"));
    private static final String JSON = "{\"key\":\"value\",\"key2\":{\"key3\":\"value3\"}}";

    @Test
    void parse() {
        assertEquals(ELEMENT, GsonParser.parse(JSON));
        assertEquals(ELEMENT, GsonParser.parse(new StringReader(JSON)));
        assertEquals(ELEMENT, GsonParser.parse(new JsonReader(new StringReader(JSON))));
    }

}
