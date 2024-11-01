package net.lenni0451.commons.asm.info;

import net.lenni0451.commons.asm.io.ClassIO;
import net.lenni0451.commons.asm.provider.ClassProvider;
import org.objectweb.asm.tree.ClassNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.lenni0451.commons.asm.ASMUtils.slash;

public class ClassInfoProvider {

    private final ClassProvider classProvider;
    private final Map<String, ClassInfo> classInfoCache;

    public ClassInfoProvider(final ClassProvider classProvider) {
        this.classProvider = classProvider;
        this.classInfoCache = new HashMap<>();
    }

    public ClassProvider getClassProvider() {
        return this.classProvider;
    }

    public synchronized ClassInfo of(String className) throws ClassNotFoundException {
        className = slash(className);
        if (this.classInfoCache.containsKey(className)) return this.classInfoCache.get(className);
        byte[] bytecode = this.classProvider.getClass(className);
        return this.of(ClassIO.fromBytes(bytecode));
    }

    public synchronized ClassInfo of(final Class<?> clazz) {
        String slashName = slash(clazz.getName());
        if (this.classInfoCache.containsKey(slashName)) return this.classInfoCache.get(slashName);
        String superName;
        if (clazz.getSuperclass() == null) superName = null;
        else superName = slash(clazz.getSuperclass().getName());
        List<String> interfaces = new ArrayList<>();
        for (Class<?> itf : clazz.getInterfaces()) {
            interfaces.add(slash(itf.getName()));
        }
        ClassInfo classInfo = new ClassInfo(this, slashName, clazz.getModifiers(), superName, interfaces.toArray(new String[0]));
        this.classInfoCache.put(slashName, classInfo);
        return classInfo;
    }

    public synchronized ClassInfo of(final ClassNode classNode) {
        if (this.classInfoCache.containsKey(classNode.name)) return this.classInfoCache.get(classNode.name);
        String[] interfaces;
        if (classNode.interfaces == null) interfaces = new String[0];
        else interfaces = classNode.interfaces.toArray(new String[0]);
        ClassInfo classInfo = new ClassInfo(this, classNode.name, classNode.access, classNode.superName, interfaces);
        this.classInfoCache.put(classNode.name, classInfo);
        return classInfo;
    }

}
