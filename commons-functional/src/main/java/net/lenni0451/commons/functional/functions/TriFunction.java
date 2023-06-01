package net.lenni0451.commons.functional.functions;

@FunctionalInterface
public interface TriFunction<A, B, C, R> {

    R apply(final A a, final B b, final C c);

}
