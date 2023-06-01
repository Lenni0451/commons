package net.lenni0451.commons.functional.multiples;

import net.lenni0451.commons.functional.multiples.immutable.ImmutableQuadruple;
import net.lenni0451.commons.functional.multiples.mutable.MutableQuadruple;

public interface Quadruple<A, B, C, D> {

    static <A, B, C, D> MutableQuadruple<A, B, C, D> mutable(final A a, final B b, final C c, final D d) {
        return new MutableQuadruple<>(a, b, c, d);
    }

    static <A, B, C, D> ImmutableQuadruple<A, B, C, D> immutable(final A a, final B b, final C c, final D d) {
        return new ImmutableQuadruple<>(a, b, c, d);
    }


    /**
     * @return The first value of the quadruple
     */
    A getA();

    /**
     * @return The second value of the quadruple
     */
    B getB();

    /**
     * @return The third value of the quadruple
     */
    C getC();

    /**
     * @return The fourth value of the quadruple
     */
    D getD();

}
