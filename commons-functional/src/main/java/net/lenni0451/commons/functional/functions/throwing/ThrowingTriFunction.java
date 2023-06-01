package net.lenni0451.commons.functional.functions.throwing;

@FunctionalInterface
public interface ThrowingTriFunction<A, B, C, R> {

    R apply(final A a, final B b, final C c);

}
