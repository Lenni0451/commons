package net.lenni0451.commons.asm.provider;

import net.lenni0451.commons.asm.ASMUtils;
import net.lenni0451.commons.asm.io.ClassIO;
import org.objectweb.asm.tree.ClassNode;

import javax.annotation.Nonnull;

public class DummyClassProvider implements ClassProvider {

    @Nonnull
    @Override
    public byte[] getClass(String name) {
        ClassNode dummyClass = ASMUtils.createEmptyClass(name);
        return ClassIO.toStacklessBytes(dummyClass);
    }

}
