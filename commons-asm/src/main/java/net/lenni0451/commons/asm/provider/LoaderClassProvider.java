package net.lenni0451.commons.asm.provider;

import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import static net.lenni0451.commons.asm.ASMUtils.slash;

public class LoaderClassProvider implements ClassProvider {

    private ClassLoader classLoader;

    public LoaderClassProvider() {
        this(LoaderClassProvider.class.getClassLoader());
    }

    public LoaderClassProvider(final ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public ClassLoader getClassLoader() {
        return this.classLoader;
    }

    public void setClassLoader(final ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Nonnull
    @Override
    public byte[] getClass(String name) throws ClassNotFoundException {
        try (InputStream is = this.classLoader.getResourceAsStream(slash(name) + ".class")) {
            if (is == null) throw new NullPointerException("Class input stream is null");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int len;
            while ((len = is.read(buf)) > 0) baos.write(buf, 0, len);
            return baos.toByteArray();
        } catch (Throwable t) {
            throw new ClassNotFoundException(name, t);
        }
    }

}
