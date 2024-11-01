package net.lenni0451.commons.asm.info;

import javax.annotation.Nullable;
import java.util.*;

public class ClassInfo {

    @Nullable
    private final ClassInfoProvider classInfoProvider;
    private final String name;
    private final int modifiers;
    private final String superClass;
    private final String[] interfaces;

    public ClassInfo(final String name, final int modifiers, final String superClass, final String[] interfaces) {
        this(null, name, modifiers, superClass, interfaces);
    }

    public ClassInfo(@Nullable final ClassInfoProvider classInfoProvider, final String name, final int modifiers, final String superClass, final String[] interfaces) {
        this.classInfoProvider = classInfoProvider;
        this.name = name;
        this.modifiers = modifiers;
        this.superClass = superClass;
        this.interfaces = interfaces;
    }

    @Nullable
    public ClassInfoProvider getClassInfoProvider() {
        return this.classInfoProvider;
    }

    public ClassInfo withProvider(final ClassInfoProvider classInfoProvider) {
        return new ClassInfo(classInfoProvider, this.name, this.modifiers, this.superClass, this.interfaces);
    }

    public String getName() {
        return this.name;
    }

    public int getModifiers() {
        return this.modifiers;
    }

    public String getSuperClass() {
        return this.superClass;
    }

    public String[] getInterfaces() {
        return this.interfaces;
    }

    public ClassInfo resolveSuperClass() throws ClassNotFoundException {
        if (this.classInfoProvider == null || this.superClass == null) return null;
        return this.classInfoProvider.of(this.superClass);
    }

    public ClassInfo[] resolveInterfaces() throws ClassNotFoundException {
        if (this.classInfoProvider == null) return new ClassInfo[0];
        ClassInfo[] infos = new ClassInfo[this.interfaces.length];
        for (int i = 0; i < this.interfaces.length; i++) {
            infos[i] = this.classInfoProvider.of(this.interfaces[i]);
        }
        return infos;
    }

    public Set<ClassInfo> recursiveResolveSuperClasses() throws ClassNotFoundException {
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
        return superClasses;
    }

}
