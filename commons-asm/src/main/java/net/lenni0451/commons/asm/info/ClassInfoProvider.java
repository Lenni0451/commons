package net.lenni0451.commons.asm.info;

import net.lenni0451.commons.asm.info.impl.asm.ASMClassInfoProvider;
import net.lenni0451.commons.asm.info.impl.jvm.JVMClassInfoProvider;
import net.lenni0451.commons.asm.provider.ClassProvider;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;

import javax.annotation.Nonnull;

import static net.lenni0451.commons.asm.ASMUtils.slash;

public interface ClassInfoProvider {

    static JVMClassInfoProvider create(final ClassLoader classLoader) {
        return new JVMClassInfoProvider(classLoader);
    }

    static ASMClassInfoProvider create(final ClassProvider classProvider) {
        return new ASMClassInfoProvider(classProvider);
    }


    /**
     * Get the {@link ClassInfo} of a class by its name.<br>
     * The name is in internal format (e.g. "java/lang/Object").<br>
     * Throws an exception if the class does not exist.
     *
     * @param className The name of the class
     * @return The class info
     */
    @Nonnull
    ClassInfo of(final String className);

    /**
     * Get the {@link ClassInfo} of a class by its type.<br>
     * Throws an exception if the class does not exist.
     *
     * @param type The type of the class
     * @return The class info
     */
    @Nonnull
    default ClassInfo of(final Type type) {
        if (type.getSort() != Type.OBJECT) throw new IllegalArgumentException("Type must be an object type");
        return this.of(type.getInternalName());
    }

    /**
     * Get the {@link ClassInfo} of a class.<br>
     * Throws an exception if the class does not exist.
     *
     * @param clazz The class
     * @return The class info
     */
    @Nonnull
    default ClassInfo of(final Class<?> clazz) {
        return this.of(slash(clazz.getName()));
    }

    /**
     * Get the {@link ClassInfo} of a class node.<br>
     * Throws an exception if the class does not exist.
     *
     * @param classNode The class node
     * @return The class info
     */
    @Nonnull
    default ClassInfo of(final ClassNode classNode) {
        return this.of(classNode.name);
    }

}
