package net.lenni0451.commons.functional.supplier.throwing;

@FunctionalInterface
public interface ThrowingSupplier<O, T extends Throwable> {

    O get() throws T;

}
