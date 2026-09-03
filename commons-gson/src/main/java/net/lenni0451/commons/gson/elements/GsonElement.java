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

    public GsonObject asObject(final GsonObject defaultValue) {
        if (this instanceof GsonObject) return (GsonObject) this;
        if (this.element.isJsonObject()) return new GsonObject(this.element.getAsJsonObject());
        return defaultValue;
    }

    public GsonArray asArray() {
        if (this instanceof GsonArray) return (GsonArray) this;
        return new GsonArray(this.element.getAsJsonArray());
    }

    public GsonArray asArray(final GsonArray defaultValue) {
        if (this instanceof GsonArray) return (GsonArray) this;
        if (this.element.isJsonArray()) return new GsonArray(this.element.getAsJsonArray());
        return defaultValue;
    }

    public GsonPrimitive asPrimitive() {
        if (this instanceof GsonPrimitive) return (GsonPrimitive) this;
        return new GsonPrimitive(this.element.getAsJsonPrimitive());
    }

    public GsonPrimitive asPrimitive(final GsonPrimitive defaultValue) {
        if (this instanceof GsonPrimitive) return (GsonPrimitive) this;
        if (this.element.isJsonPrimitive()) return new GsonPrimitive(this.element.getAsJsonPrimitive());
        return defaultValue;
    }

    public boolean asBoolean() {
        return this.element.getAsBoolean();
    }

    public boolean asBoolean(final boolean defaultValue) {
        if (this.isPrimitive()) {
            try {
                return this.asBoolean();
            } catch (Throwable ignored) {
            }
        }
        return defaultValue;
    }

    public byte asByte() {
        return this.element.getAsByte();
    }

    public byte asByte(final byte defaultValue) {
        if (this.isPrimitive()) {
            try {
                return this.asByte();
            } catch (Throwable ignored) {
            }
        }
        return defaultValue;
    }

    public short asShort() {
        return this.element.getAsShort();
    }

    public short asShort(final short defaultValue) {
        if (this.isPrimitive()) {
            try {
                return this.asShort();
            } catch (Throwable ignored) {
            }
        }
        return defaultValue;
    }

    public int asInt() {
        return this.element.getAsInt();
    }

    public int asInt(final int defaultValue) {
        if (this.isPrimitive()) {
            try {
                return this.asInt();
            } catch (Throwable ignored) {
            }
        }
        return defaultValue;
    }

    public long asLong() {
        return this.element.getAsLong();
    }

    public long asLong(final long defaultValue) {
        if (this.isPrimitive()) {
            try {
                return this.asLong();
            } catch (Throwable ignored) {
            }
        }
        return defaultValue;
    }

    public float asFloat() {
        return this.element.getAsFloat();
    }

    public float asFloat(final float defaultValue) {
        if (this.isPrimitive()) {
            try {
                return this.asFloat();
            } catch (Throwable ignored) {
            }
        }
        return defaultValue;
    }

    public double asDouble() {
        return this.element.getAsDouble();
    }

    public double asDouble(final double defaultValue) {
        if (this.isPrimitive()) {
            try {
                return this.asDouble();
            } catch (Throwable ignored) {
            }
        }
        return defaultValue;
    }

    public BigInteger asBigInteger() {
        return this.element.getAsBigInteger();
    }

    public BigInteger asBigInteger(final BigInteger defaultValue) {
        if (this.isPrimitive()) {
            try {
                return this.asBigInteger();
            } catch (Throwable ignored) {
            }
        }
        return defaultValue;
    }

    public BigDecimal asBigDecimal() {
        return this.element.getAsBigDecimal();
    }

    public BigDecimal asBigDecimal(final BigDecimal defaultValue) {
        if (this.isPrimitive()) {
            try {
                return this.asBigDecimal();
            } catch (Throwable ignored) {
            }
        }
        return defaultValue;
    }

    public Number asNumber() {
        return this.element.getAsNumber();
    }

    public Number asNumber(final Number defaultValue) {
        if (this.isPrimitive()) {
            try {
                return this.asNumber();
            } catch (Throwable ignored) {
            }
        }
        return defaultValue;
    }

    public String asString() {
        return this.element.getAsString();
    }

    public String asString(final String defaultValue) {
        if (this.isPrimitive()) {
            try {
                return this.asString();
            } catch (Throwable ignored) {
            }
        }
        return defaultValue;
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
