package net.lenni0451.commons.functional.multiples.mutable;

import net.lenni0451.commons.functional.multiples.Tuple;

import java.util.Objects;

public class MutableTuple<A, B> implements Tuple<A, B> {

    private A a;
    private B b;

    public MutableTuple(final A a, final B b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public A getA() {
        return this.a;
    }

    /**
     * Set the first value of the tuple.
     *
     * @param a The new value
     */
    public void setA(final A a) {
        this.a = a;
    }

    /**
     * Set the first value of the tuple.
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
     * Set the second value of the tuple.
     *
     * @param b The new value
     */
    public void setB(final B b) {
        this.b = b;
    }

    /**
     * Set the second value of the tuple.
     *
     * @param b The new value
     */
    public void setRight(final B b) {
        this.b = b;
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
        MutableTuple<?, ?> that = (MutableTuple<?, ?>) o;
        return Objects.equals(this.a, that.a) && Objects.equals(this.b, that.b);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.a, this.b);
    }

}
