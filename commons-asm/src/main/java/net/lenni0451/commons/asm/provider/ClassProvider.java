package net.lenni0451.commons.asm.provider;

import net.lenni0451.commons.asm.io.ClassIO;
import org.objectweb.asm.tree.ClassNode;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Map;

public interface ClassProvider {

    @Nonnull
    byte[] getClass(final String name) throws ClassNotFoundException;

    @Nonnull
    default Map<String, ClassSupplier> getAllClasses() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    default ClassNode getClassNode(final String name) throws ClassNotFoundException {
        return ClassIO.fromBytes(this.getClass(name));
    }

    default ClassProvider then(final ClassProvider classProvider) {
        return new DelegatingClassProvider(this, classProvider);
    }


    @FunctionalInterface
    interface ClassSupplier {
        byte[] get() throws IOException;
    }

}
