package net.lenni0451.commons.gson.elements;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.math.BigInteger;

public class GsonElement {

    public static GsonElement wrap(final JsonElement element) {
        if (element instanceof JsonObject) {
            return new GsonObject(element.getAsJsonObject());
        } else if (element instanceof JsonArray) {
            return new GsonArray(element.getAsJsonArray());
        } else if (element instanceof JsonPrimitive) {
            return new GsonPrimitive(element.getAsJsonPrimitive());
        } else {
            return new GsonElement(element);
        }
    }


    private final JsonElement element;

    protected GsonElement(@Nonnull final JsonElement element) {
        this.element = element;
    }

    @Nonnull
    public JsonElement getJsonElement() {
        return this.element;
    }

    public GsonElement deepCopy() {
        return wrap(this.element.deepCopy());
    }

    public boolean isObject() {
        return this.element.isJsonObject();
    }

    public boolean isArray() {
        return this.element.isJsonArray();
    }

    public boolean isPrimitive() {
        return this.element.isJsonPrimitive();
    }

    public boolean isNull() {
        return this.element.isJsonNull();
    }

    public GsonObject asObject() {
        if (this instanceof GsonObject) return (GsonObject) this;
        return new GsonObject(this.element.getAsJsonObject());
    }

    public GsonArray asArray() {
        if (this instanceof GsonArray) return (GsonArray) this;
        return new GsonArray(this.element.getAsJsonArray());
    }

    public GsonPrimitive asPrimitive() {
        if (this instanceof GsonPrimitive) return (GsonPrimitive) this;
        return new GsonPrimitive(this.element.getAsJsonPrimitive());
    }

    public boolean asBoolean() {
        return this.element.getAsBoolean();
    }

    public byte asByte() {
        return this.element.getAsByte();
    }

    public short asShort() {
        return this.element.getAsShort();
    }

    public int asInt() {
        return this.element.getAsInt();
    }

    public long asLong() {
        return this.element.getAsLong();
    }

    public float asFloat() {
        return this.element.getAsFloat();
    }

    public double asDouble() {
        return this.element.getAsDouble();
    }

    public BigInteger asBigInteger() {
        return this.element.getAsBigInteger();
    }

    public BigDecimal asBigDecimal() {
        return this.element.getAsBigDecimal();
    }

    public Number asNumber() {
        return this.element.getAsNumber();
    }

    public String asString() {
        return this.element.getAsString();
    }

    @Override
    public String toString() {
        return this.element.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GsonElement) {
            return this.element.equals(((GsonElement) obj).element);
        } else if (obj instanceof JsonElement) {
            return this.element.equals(obj);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.element.hashCode();
    }

}
