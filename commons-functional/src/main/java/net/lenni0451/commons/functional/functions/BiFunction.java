package net.lenni0451.commons.functional.functions;

@FunctionalInterface
public interface BiFunction<A, B, R> {

    R apply(final A a, final B b);

}
