package net.lenni0451.commons.asm.info.impl;

import net.lenni0451.commons.asm.info.ClassInfo;
import net.lenni0451.commons.asm.info.ClassInfoProvider;

import java.util.*;

public class ClassInfoResolver {

    /**
     * Resolve the super class of the given class info and return its class info representation.
     *
     * @param classInfoProvider The {@link ClassInfoProvider} to use for resolving the super class
     * @param classInfo         The class info to resolve the super class for
     * @return The resolved class info of the super class or null if the class has no super class
     */
    public static ClassInfo resolveSuperClass(final ClassInfoProvider classInfoProvider, final ClassInfo classInfo) {
        String superClass = classInfo.getSuperClass();
        if (superClass == null) return null;
        return classInfoProvider.of(superClass);
    }

    /**
     * Resolve the interfaces of the given class info and return their class info representations.
     *
     * @param classInfoProvider The {@link ClassInfoProvider} to use for resolving the interfaces
     * @param classInfo         The class info to resolve the interfaces for
     * @return The resolved class infos of the interfaces
     */
    public static ClassInfo[] resolveInterfaces(final ClassInfoProvider classInfoProvider, final ClassInfo classInfo) {
        String[] interfaces = classInfo.getInterfaces();
        ClassInfo[] infos = new ClassInfo[interfaces.length];
        for (int i = 0; i < interfaces.length; i++) {
            infos[i] = classInfoProvider.of(interfaces[i]);
        }
        return infos;
    }

    /**
     * Recursively resolve all super classes and interfaces of the given class info.
     *
     * @param classInfo   The class info to resolve the super classes and interfaces for
     * @param includeSelf If the given class info should be included in the result
     * @return The resolved class infos of all super classes and interfaces
     */
    public static Set<ClassInfo> recursiveResolveSuperClasses(final ClassInfo classInfo, final boolean includeSelf) {
        Set<ClassInfo> superClasses = new LinkedHashSet<>();
        Queue<ClassInfo> queue = new LinkedList<>();
        queue.add(classInfo);
        while (!queue.isEmpty()) {
            ClassInfo current = queue.poll();
            if (!superClasses.add(current)) continue;
            ClassInfo superClass = current.getSuperClassInfo();
            if (superClass != null) queue.add(superClass);
            Collections.addAll(queue, current.getInterfaceInfos());
        }
        if (!includeSelf) superClasses.remove(classInfo);
        return superClasses;
    }

}
