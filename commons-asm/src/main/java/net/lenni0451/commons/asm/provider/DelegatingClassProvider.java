package net.lenni0451.commons.asm.provider;

import javax.annotation.Nonnull;

public class DelegatingClassProvider implements ClassProvider {

    private final ClassProvider[] delegates;

    public DelegatingClassProvider(final ClassProvider... delegates) {
        this.delegates = delegates;
    }

    @Nonnull
    @Override
    public byte[] getClass(String name) throws ClassNotFoundException {
        for (ClassProvider delegate : this.delegates) {
            try {
                return delegate.getClass(name);
            } catch (ClassNotFoundException ignored) {
            }
        }
        throw new ClassNotFoundException(name);
    }

}
