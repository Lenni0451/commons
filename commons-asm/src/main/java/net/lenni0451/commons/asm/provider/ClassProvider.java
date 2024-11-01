package net.lenni0451.commons.asm.provider;

import javax.annotation.Nonnull;

public interface ClassProvider {

    @Nonnull
    byte[] getClass(final String name) throws ClassNotFoundException;

}
