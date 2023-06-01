package net.lenni0451.commons.functional.multiples.immutable;

import net.lenni0451.commons.functional.multiples.Quadruple;

import java.util.Objects;

public class ImmutableQuadruple<A, B, C, D> implements Quadruple<A, B, C, D> {

    private final A a;
    private final B b;
    private final C c;
    private final D d;

    public ImmutableQuadruple(final A a, final B b, final C c, final D d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
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
    public D getD() {
        return this.d;
    }

    @Override
    public String toString() {
        return "MutableQuadruple{" +
                "a=" + this.a +
                ", b=" + this.b +
                ", c=" + this.c +
                ", d=" + this.d +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableQuadruple<?, ?, ?, ?> that = (ImmutableQuadruple<?, ?, ?, ?>) o;
        return Objects.equals(this.a, that.a) && Objects.equals(this.b, that.b) && Objects.equals(this.c, that.c) && Objects.equals(this.d, that.d);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.a, this.b, this.c, this.d);
    }

}
