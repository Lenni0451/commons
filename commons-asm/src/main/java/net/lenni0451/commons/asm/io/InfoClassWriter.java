package net.lenni0451.commons.asm.io;

import net.lenni0451.commons.asm.Modifiers;
import net.lenni0451.commons.asm.info.ClassInfo;
import net.lenni0451.commons.asm.info.ClassInfoProvider;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.util.Set;

import static net.lenni0451.commons.asm.Types.internalName;

public class InfoClassWriter extends ClassWriter {

    private static final String OBJECT = internalName(Object.class);

    private final ClassInfoProvider classInfoProvider;

    public InfoClassWriter(final int flags, final ClassInfoProvider classInfoProvider) {
        super(flags);
        this.classInfoProvider = classInfoProvider;
    }

    @Override
    protected String getCommonSuperClass(final String type1, final String type2) {
        if (type1.equals(OBJECT) || type2.equals(OBJECT)) return OBJECT;
        try {
            ClassInfo class1 = this.classInfoProvider.of(type1);
            Set<ClassInfo> superClasses1 = class1.recursiveResolveSuperClasses(false);
            ClassInfo class2 = this.classInfoProvider.of(type2);
            Set<ClassInfo> superClasses2 = class2.recursiveResolveSuperClasses(false);

            if (superClasses1.contains(class2)) return type2;
            if (superClasses2.contains(class1)) return type1;
            if (Modifiers.has(class1.getModifiers(), Opcodes.ACC_INTERFACE) || Modifiers.has(class2.getModifiers(), Opcodes.ACC_INTERFACE)) return OBJECT;
            do {
                class1 = class1.resolveSuperClass();
                if (class1 == null) return OBJECT;
            } while (!superClasses2.contains(class1));
            return class1.getName();
        } catch (ClassNotFoundException e) {
            throw new TypeNotPresentException(e.getMessage(), e);
        }
    }

}
