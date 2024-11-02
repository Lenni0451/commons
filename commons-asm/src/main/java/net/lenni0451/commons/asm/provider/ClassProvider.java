package net.lenni0451.commons.asm.provider;

import net.lenni0451.commons.asm.io.ClassIO;
import org.objectweb.asm.tree.ClassNode;

import javax.annotation.Nonnull;

public interface ClassProvider {

    @Nonnull
    byte[] getClass(final String name) throws ClassNotFoundException;

    default ClassNode getClassNode(final String name) throws ClassNotFoundException {
        return ClassIO.fromBytes(this.getClass(name));
    }

    default ClassProvider then(final ClassProvider classProvider) {
        return new DelegatingClassProvider(this, classProvider);
    }

}
