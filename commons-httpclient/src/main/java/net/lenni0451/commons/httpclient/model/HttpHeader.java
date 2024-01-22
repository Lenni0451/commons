package net.lenni0451.commons.httpclient.model;

import javax.annotation.Nonnull;
import java.util.Objects;

public class HttpHeader {

    @Nonnull
    private final String name;
    @Nonnull
    private final String value;

    public HttpHeader(@Nonnull final String name, @Nonnull final String value) {
        this.name = name;
        this.value = value;
    }

    @Nonnull
    public String getName() {
        return this.name;
    }

    @Nonnull
    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return "HttpHeader{" +
                "name='" + this.name + '\'' +
                ", value='" + this.value + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpHeader that = (HttpHeader) o;
        return Objects.equals(this.name, that.name) && Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.value);
    }

}
