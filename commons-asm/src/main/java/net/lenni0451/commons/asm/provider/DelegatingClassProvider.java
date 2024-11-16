package net.lenni0451.commons.asm.provider;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * A class provider that delegates to multiple other class providers.
 */
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

    /**
     * @throws UnsupportedOperationException If this operation is not supported by any delegate
     * @implNote The return values of all delegates are merged into one map
     */
    @Nonnull
    @Override
    public Map<String, ClassSupplier> getAllClasses() {
        Map<String, ClassSupplier> classes = new HashMap<>();
        boolean supported = false;
        for (ClassProvider delegate : this.delegates) {
            try {
                classes.putAll(delegate.getAllClasses());
                supported = true;
            } catch (UnsupportedOperationException ignored) {
            }
        }
        if (!supported) throw new UnsupportedOperationException("getAllClasses is not supported by any delegate");
        return classes;
    }

}
