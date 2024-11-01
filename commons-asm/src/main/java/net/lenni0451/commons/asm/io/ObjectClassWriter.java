package net.lenni0451.commons.asm.io;

import org.objectweb.asm.ClassWriter;

import static net.lenni0451.commons.asm.Types.internalName;

/**
 * A class writer implementation which always returns {@code java/lang/Object} as the common super class.<br>
 * This can cause issues with the stack frame calculation but works surprisingly well in most cases.
 */
public class ObjectClassWriter extends ClassWriter {

    private static final String OBJECT = internalName(Object.class);

    public ObjectClassWriter(final int flags) {
        super(flags);
    }

    @Override
    protected String getCommonSuperClass(String type1, String type2) {
        return OBJECT;
    }

}
