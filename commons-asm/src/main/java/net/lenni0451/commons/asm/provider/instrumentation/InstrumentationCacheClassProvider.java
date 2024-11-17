package net.lenni0451.commons.asm.provider.instrumentation;

import net.lenni0451.commons.asm.io.ClassIO;
import net.lenni0451.commons.asm.provider.ClassProvider;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static net.lenni0451.commons.asm.ASMUtils.slash;

/**
 * A class provider that uses an {@link Instrumentation} instance to cache all loaded classes.<br>
 * The caching operation is slow and memory consuming, since the bytecode of all loaded classes is stored in memory.<br>
 * This provider can't provide classes that have not been loaded yet. It is recommended to chain another class provider after this one.
 */
public class InstrumentationCacheClassProvider implements ClassProvider, ClassFileTransformer {

    private final Instrumentation instrumentation;
    private final Map<String, ClassSupplier> classes;

    public InstrumentationCacheClassProvider(final Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
        this.classes = new ConcurrentHashMap<>();

        this.setupInstrumentation();
    }

    @Nonnull
    @Override
    public byte[] getClass(String name) throws ClassNotFoundException {
        try {
            ClassSupplier supplier = this.classes.get(slash(name));
            if (supplier == null) throw new ClassNotFoundException(name);
            return supplier.get();
        } catch (IOException e) {
            throw new ClassNotFoundException(name, e);
        }
    }

    @Nonnull
    @Override
    public Map<String, ClassSupplier> getAllClasses() throws UnsupportedOperationException {
        return Collections.unmodifiableMap(this.classes);
    }

    @Override
    public void close() {
        this.instrumentation.removeTransformer(this);
        this.classes.clear();
    }

    private void setupInstrumentation() {
        this.instrumentation.addTransformer(this, true);
        for (Class<?> clazz : this.instrumentation.getAllLoadedClasses()) {
            if (clazz == null) continue;
            if (clazz.isArray()) continue;
            ClassLoader classLoader = clazz.getClassLoader();
            if (classLoader == null) classLoader = ClassLoader.getSystemClassLoader();
            try {
                //Try reading the class file from the class loader
                //This is faster and safer than redefining the class
                InputStream is = classLoader.getResourceAsStream(slash(clazz.getName()) + ".class");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = is.read(buffer)) >= 0) baos.write(buffer, 0, len);
                this.classes.put(slash(clazz.getName()), baos::toByteArray);
            } catch (Throwable t) {
                //Try to redefine the class
                //This is slow and can fail if the class is not modifiable
                //If this also fails, the class will not be available
                try {
                    this.instrumentation.retransformClasses(clazz);
                } catch (Throwable ignored) {
                }
            }
        }
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        if (className == null) {
            //If the class name is null, try reading the class using ASM and get the name from there
            try {
                //Skip everything possible to make it faster
                ClassNode node = ClassIO.fromBytes(classfileBuffer, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
                this.classes.put(node.name, () -> classfileBuffer);
            } catch (Throwable ignored) {
            }
        } else {
            this.classes.put(slash(className), () -> classfileBuffer);
        }
        return null;
    }

}
