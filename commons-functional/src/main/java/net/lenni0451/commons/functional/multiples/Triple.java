package net.lenni0451.commons.functional.multiples;

import net.lenni0451.commons.functional.multiples.immutable.ImmutableTriple;
import net.lenni0451.commons.functional.multiples.mutable.MutableTriple;

public interface Triple<A, B, C> {

    static <A, B, C> MutableTriple<A, B, C> mutable(final A a, final B b, final C c) {
        return new MutableTriple<>(a, b, c);
    }

    static <A, B, C> ImmutableTriple<A, B, C> immutable(final A a, final B b, final C c) {
        return new ImmutableTriple<>(a, b, c);
    }


    /**
     * @return The first value of the triple
     */
    A getA();

    /**
     * @return The second value of the triple
     */
    B getB();

    /**
     * @return The third value of the triple
     */
    C getC();

    /**
     * @return The first value of the triple
     */
    default A getLeft() {
        return this.getA();
    }

    /**
     * @return The second value of the triple
     */
    default B getMiddle() {
        return this.getB();
    }

    /**
     * @return The third value of the triple
     */
    default C getRight() {
        return this.getC();
    }

}
