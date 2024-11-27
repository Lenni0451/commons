package net.lenni0451.commons.gson;

import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import net.lenni0451.commons.gson.elements.GsonElement;

import java.io.Reader;

public class GsonParser {

    public static GsonElement parse(final String s) {
        return GsonElement.wrap(JsonParser.parseString(s));
    }

    public static GsonElement parse(final Reader reader) {
        return GsonElement.wrap(JsonParser.parseReader(reader));
    }

    public static GsonElement parse(final JsonReader reader) {
        return GsonElement.wrap(JsonParser.parseReader(reader));
    }

}
