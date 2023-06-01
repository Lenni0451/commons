package net.lenni0451.commons.functional.multiples.immutable;

import net.lenni0451.commons.functional.multiples.Triple;

import java.util.Objects;

public class ImmutableTriple<A, B, C> implements Triple<A, B, C> {

    private final A a;
    private final B b;
    private final C c;

    public ImmutableTriple(final A a, final B b, final C c) {
        this.a = a;
        this.b = b;
        this.c = c;
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
    public C getC() {
        return this.c;
    }

    @Override
    public String toString() {
        return "MutableTriple{" +
                "a=" + this.a +
                ", b=" + this.b +
                ", c=" + this.c +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableTriple<?, ?, ?> that = (ImmutableTriple<?, ?, ?>) o;
        return Objects.equals(this.a, that.a) && Objects.equals(this.b, that.b) && Objects.equals(this.c, that.c);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.a, this.b, this.c);
    }

}
