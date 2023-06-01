package net.lenni0451.commons.functional.multiples.immutable;

import net.lenni0451.commons.functional.multiples.Tuple;

import java.util.Objects;

public class ImmutableTuple<A, B> implements Tuple<A, B> {

    private final A a;
    private final B b;

    public ImmutableTuple(final A a, final B b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public A getA() {
        return this.a;
    }

    @Override
    public B getB() {
        return this.b;
    }

    @Override
    public String toString() {
        return "MutableTuple{" +
                "a=" + this.a +
                ", b=" + this.b +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableTuple<?, ?> that = (ImmutableTuple<?, ?>) o;
        return Objects.equals(this.a, that.a) && Objects.equals(this.b, that.b);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.a, this.b);
    }

}
