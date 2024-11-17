package net.lenni0451.commons.asm.provider.instrumentation;

import net.lenni0451.commons.asm.provider.ClassProvider;
import net.lenni0451.commons.asm.provider.DelegatingClassProvider;
import net.lenni0451.commons.asm.provider.LoaderClassProvider;

import javax.annotation.Nonnull;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A class provider that uses a {@link Instrumentation} instance to collect all class loaders to provide classes.<br>
 * This provider delegates to {@link LoaderClassProvider} instances for each found class loader.<br>
 * If a class loader has not been used before yet, this provider will not be able to provide classes from it until the class loader is used at least once.
 *
 * @see LoaderClassProvider
 */
public class InstrumentationLoaderClassProvider implements ClassProvider, ClassFileTransformer {

    private final Instrumentation instrumentation;
    private final Map<ClassLoader, LoaderClassProvider> classLoaders;
    private DelegatingClassProvider classProvider;

    public InstrumentationLoaderClassProvider(final Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
        this.classLoaders = new ConcurrentHashMap<>();

        this.classLoaders.put(ClassLoader.getSystemClassLoader(), new LoaderClassProvider(ClassLoader.getSystemClassLoader()));
        this.setupInstrumentation();
    }

    @Nonnull
    @Override
    public byte[] getClass(String name) throws ClassNotFoundException {
        return this.classProvider.getClass(name);
    }

    @Nonnull
    @Override
    public Map<String, ClassSupplier> getAllClasses() throws UnsupportedOperationException {
        return this.classProvider.getAllClasses();
    }

    @Override
    public void close() throws Exception {
        this.instrumentation.removeTransformer(this);
        this.classLoaders.clear();
        this.classProvider.close();
    }

    private void setupInstrumentation() {
        this.instrumentation.addTransformer(this, true);
        for (Class<?> clazz : this.instrumentation.getAllLoadedClasses()) {
            if (clazz == null) continue;
            if (clazz.getClassLoader() == null) continue;
            this.classLoaders.computeIfAbsent(clazz.getClassLoader(), LoaderClassProvider::new);
        }
        this.updateClassProvider();
    }

    private synchronized void updateClassProvider() {
        this.classProvider = new DelegatingClassProvider(this.classLoaders.values().toArray(new ClassProvider[0]));
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        if (loader != null && !this.classLoaders.containsKey(loader)) {
            this.classLoaders.computeIfAbsent(loader, LoaderClassProvider::new);
            this.updateClassProvider();
        }
        return null;
    }

}
