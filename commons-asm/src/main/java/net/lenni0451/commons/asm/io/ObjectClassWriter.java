package net.lenni0451.commons.asm.io;

import org.objectweb.asm.ClassWriter;

/**
 * A class writer implementation which always returns {@code java/lang/Object} as the common super class.<br>
 * This can cause issues with the stack frame calculation but works surprisingly well in most cases.
 */
public class ObjectClassWriter extends ClassWriter {

    public ObjectClassWriter(final int flags) {
        super(flags);
    }

    @Override
    protected String getCommonSuperClass(String type1, String type2) {
        return "java/lang/Object";
    }

}
