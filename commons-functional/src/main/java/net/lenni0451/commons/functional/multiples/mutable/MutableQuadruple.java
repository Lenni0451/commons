package net.lenni0451.commons.functional.multiples.mutable;

import net.lenni0451.commons.functional.multiples.Quadruple;

import java.util.Objects;

public class MutableQuadruple<A, B, C, D> implements Quadruple<A, B, C, D> {

    private A a;
    private B b;
    private C c;
    private D d;

    public MutableQuadruple(final A a, final B b, final C c, final D d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    @Override
    public A getA() {
        return this.a;
    }

    /**
     * Set the first value of the quadruple.
     *
     * @param a The new value
     */
    public void setA(final A a) {
        this.a = a;
    }

    @Override
    public B getB() {
        return this.b;
    }

    /**
     * Set the second value of the quadruple.
     *
     * @param b The new value
     */
    public void setB(final B b) {
        this.b = b;
    }

    @Override
    public C getC() {
        return this.c;
    }

    /**
     * Set the third value of the quadruple.
     *
     * @param c The new value
     */
    public void setC(final C c) {
        this.c = c;
    }

    @Override
    public D getD() {
        return this.d;
    }

    /**
     * Set the fourth value of the quadruple.
     *
     * @param d The new value
     */
    public void setD(final D d) {
        this.d = d;
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
        MutableQuadruple<?, ?, ?, ?> that = (MutableQuadruple<?, ?, ?, ?>) o;
        return Objects.equals(this.a, that.a) && Objects.equals(this.b, that.b) && Objects.equals(this.c, that.c) && Objects.equals(this.d, that.d);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.a, this.b, this.c, this.d);
    }

}
