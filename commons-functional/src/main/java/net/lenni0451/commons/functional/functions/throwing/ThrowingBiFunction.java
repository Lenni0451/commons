package net.lenni0451.commons.functional.functions.throwing;

@FunctionalInterface
public interface ThrowingBiFunction<A, B, R, T extends Throwable> {

    R apply(final A a, final B b) throws T;

}
