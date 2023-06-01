package net.lenni0451.commons.functional.multiples;

import net.lenni0451.commons.functional.multiples.immutable.ImmutableTuple;
import net.lenni0451.commons.functional.multiples.mutable.MutableTuple;

public interface Tuple<A, B> {

    static <A, B> MutableTuple<A, B> mutable(final A a, final B b) {
        return new MutableTuple<>(a, b);
    }

    static <A, B> ImmutableTuple<A, B> immutable(final A a, final B b) {
        return new ImmutableTuple<>(a, b);
    }


    /**
     * @return The first value of the tuple
     */
    A getA();

    /**
     * @return The second value of the tuple
     */
    B getB();

    /**
     * @return The first value of the tuple
     */
    default A getLeft() {
        return this.getA();
    }

    /**
     * @return The second value of the tuple
     */
    default B getRight() {
        return this.getB();
    }

}
