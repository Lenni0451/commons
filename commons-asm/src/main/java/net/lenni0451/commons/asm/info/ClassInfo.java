package net.lenni0451.commons.asm.info;

import org.objectweb.asm.tree.ClassNode;

import javax.annotation.Nullable;
import java.util.*;

public class ClassInfo {

    @Nullable
    private final ClassInfoProvider classInfoProvider;
    @Nullable
    private ClassNode classNode;
    private final String name;
    private final int modifiers;
    private final String superClass;
    private final String[] interfaces;

    public ClassInfo(final String name, final int modifiers, final String superClass, final String[] interfaces) {
        this(null, null, name, modifiers, superClass, interfaces);
    }

    public ClassInfo(@Nullable final ClassInfoProvider classInfoProvider, @Nullable final ClassNode classNode, final String name, final int modifiers, final String superClass, final String[] interfaces) {
        this.classInfoProvider = classInfoProvider;
        this.classNode = classNode;
        this.name = name;
        this.modifiers = modifiers;
        this.superClass = superClass;
        this.interfaces = interfaces;
    }

    /**
     * @return The {@link ClassInfoProvider} if this class info was created by one, otherwise null
     */
    @Nullable
    public ClassInfoProvider getClassInfoProvider() {
        return this.classInfoProvider;
    }

    /**
     * Create a new class info with the given {@link ClassInfoProvider}.
     *
     * @param classInfoProvider The class info provider
     * @return The new class info
     */
    public ClassInfo withProvider(final ClassInfoProvider classInfoProvider) {
        return new ClassInfo(classInfoProvider, this.classNode, this.name, this.modifiers, this.superClass, this.interfaces);
    }

    /**
     * Get the class node of this class info.<br>
     * This requires the class info provider to be set.
     *
     * @return The class node or null if the class info provider is not set
     * @throws ClassNotFoundException If the class could not be found in the class info provider
     */
    @Nullable
    public ClassNode getClassNode() throws ClassNotFoundException {
        if (this.classNode == null) {
            if (this.classInfoProvider == null) return null;
            this.classNode = this.classInfoProvider.getClassProvider().getClassNode(this.name);
        }
        return this.classNode;
    }

    /**
     * @return The name of this class (internal name)
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return The modifiers of this class
     */
    public int getModifiers() {
        return this.modifiers;
    }

    /**
     * @return The super class name of this class (internal name)
     */
    public String getSuperClass() {
        return this.superClass;
    }

    /**
     * @return The interfaces of this class (internal names)
     */
    public String[] getInterfaces() {
        return this.interfaces;
    }

    /**
     * Resolve the super class of this class.<br>
     * This requires the class info provider to be set.
     *
     * @return The super class or null if the class info provider is not set
     * @throws ClassNotFoundException If the super class could not be found in the class info provider
     */
    public ClassInfo resolveSuperClass() throws ClassNotFoundException {
        if (this.classInfoProvider == null || this.superClass == null) return null;
        return this.classInfoProvider.of(this.superClass);
    }

    /**
     * Resolve the interfaces of this class.<br>
     * This requires the class info provider to be set.
     *
     * @return The interfaces of this class
     * @throws ClassNotFoundException If an interface could not be found in the class info provider
     */
    public ClassInfo[] resolveInterfaces() throws ClassNotFoundException {
        if (this.classInfoProvider == null) return new ClassInfo[0];
        ClassInfo[] infos = new ClassInfo[this.interfaces.length];
        for (int i = 0; i < this.interfaces.length; i++) {
            infos[i] = this.classInfoProvider.of(this.interfaces[i]);
        }
        return infos;
    }

    /**
     * Resolve the super classes of this class recursively.<br>
     * All interfaces will be included.<br>
     * This requires the class info provider to be set.
     *
     * @param includeSelf If the class itself should be included
     * @return The super classes of this class
     * @throws ClassNotFoundException If a super class could not be found in the class info provider
     */
    public Set<ClassInfo> recursiveResolveSuperClasses(final boolean includeSelf) throws ClassNotFoundException {
        Set<ClassInfo> superClasses = new LinkedHashSet<>();
        Queue<ClassInfo> queue = new LinkedList<>();
        queue.add(this);
        while (!queue.isEmpty()) {
            ClassInfo current = queue.poll();
            if (!superClasses.add(current)) continue;
            ClassInfo superClass = current.resolveSuperClass();
            if (superClass != null) queue.add(superClass);
            Collections.addAll(queue, current.resolveInterfaces());
        }
        if (!includeSelf) superClasses.remove(this);
        return superClasses;
    }

}
