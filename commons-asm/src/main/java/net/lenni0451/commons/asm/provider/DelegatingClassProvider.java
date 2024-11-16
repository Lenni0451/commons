package net.lenni0451.commons.asm.provider;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

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

    @Nonnull
    @Override
    public Map<String, ClassSupplier> getAllClasses() {
        Map<String, ClassSupplier> classes = new HashMap<>();
        for (ClassProvider delegate : this.delegates) {
            try {
                classes.putAll(delegate.getAllClasses());
            } catch (UnsupportedOperationException ignored) {
            }
        }
        return classes;
    }

}
