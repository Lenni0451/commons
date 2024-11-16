package net.lenni0451.commons.asm.provider;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.function.Supplier;

/**
 * A class provider that delegates to another class provider which is only initialized when it is needed.
 */
public class LazyClassProvider implements ClassProvider {

    private final Supplier<ClassProvider> supplier;
    private ClassProvider delegate;

    public LazyClassProvider(final Supplier<ClassProvider> supplier) {
        this.supplier = supplier;
    }

    @Nonnull
    @Override
    public byte[] getClass(String name) throws ClassNotFoundException {
        if (this.delegate == null) this.delegate = this.supplier.get();
        return this.delegate.getClass(name);
    }

    @Nonnull
    @Override
    public Map<String, ClassSupplier> getAllClasses() throws UnsupportedOperationException {
        if (this.delegate == null) this.delegate = this.supplier.get();
        return this.delegate.getAllClasses();
    }

    @Override
    public void close() throws Exception {
        if (this.delegate != null) this.delegate.close();
    }

}
