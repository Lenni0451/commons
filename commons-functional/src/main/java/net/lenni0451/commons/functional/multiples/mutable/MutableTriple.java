package net.lenni0451.commons.functional.multiples.mutable;

import net.lenni0451.commons.functional.multiples.Triple;

import java.util.Objects;

public class MutableTriple<A, B, C> implements Triple<A, B, C> {

    private A a;
    private B b;
    private C c;

    public MutableTriple(final A a, final B b, final C c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public A getA() {
        return this.a;
    }

    /**
     * Set the first value of the triple.
     *
     * @param a The new value
     */
    public void setA(final A a) {
        this.a = a;
    }

    /**
     * Set the first value of the triple.
     *
     * @param a The new value
     */
    public void setLeft(final A a) {
        this.a = a;
    }

    @Override
    public B getB() {
        return this.b;
    }

    /**
     * Set the second value of the triple.
     *
     * @param b The new value
     */
    public void setB(final B b) {
        this.b = b;
    }

    /**
     * Set the second value of the triple.
     *
     * @param b The new value
     */
    public void setMiddle(final B b) {
        this.b = b;
    }

    @Override
    public C getC() {
        return this.c;
    }

    /**
     * Set the third value of the triple.
     *
     * @param c The new value
     */
    public void setC(final C c) {
        this.c = c;
    }

    /**
     * Set the third value of the triple.
     *
     * @param c The new value
     */
    public void setRight(final C c) {
        this.c = c;
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
        MutableTriple<?, ?, ?> that = (MutableTriple<?, ?, ?>) o;
        return Objects.equals(this.a, that.a) && Objects.equals(this.b, that.b) && Objects.equals(this.c, that.c);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.a, this.b, this.c);
    }

}
