package net.lenni0451.commons.gson.elements;

import com.google.gson.JsonPrimitive;

public class GsonPrimitive extends GsonElement {

    private final JsonPrimitive primitive;

    public GsonPrimitive(final String s) {
        this(new JsonPrimitive(s));
    }

    public GsonPrimitive(final boolean b) {
        this(new JsonPrimitive(b));
    }

    public GsonPrimitive(final Number n) {
        this(new JsonPrimitive(n));
    }

    public GsonPrimitive(final JsonPrimitive primitive) {
        super(primitive);
        this.primitive = primitive;
    }

    public JsonPrimitive getJsonPrimitive() {
        return this.primitive;
    }

    public boolean isBoolean() {
        return this.primitive.isBoolean();
    }

    public boolean isNumber() {
        return this.primitive.isNumber();
    }

    public boolean isString() {
        return this.primitive.isString();
    }

}
