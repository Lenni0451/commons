package net.lenni0451.commons.asm.info.impl.jvm;

import lombok.SneakyThrows;
import net.lenni0451.commons.asm.info.ClassInfoProvider;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

import static net.lenni0451.commons.asm.ASMUtils.dot;

public class JVMClassInfoProvider implements ClassInfoProvider {

    private final ClassLoader classLoader;
    private final Map<String, JVMClassInfo> classInfoCache;

    public JVMClassInfoProvider() {
        this(JVMClassInfoProvider.class.getClassLoader());
    }

    public JVMClassInfoProvider(final ClassLoader classLoader) {
        this.classLoader = classLoader;
        this.classInfoCache = new HashMap<>();
    }

    @Nonnull
    @Override
    @SneakyThrows
    public synchronized JVMClassInfo of(String className) {
        className = dot(className);
        if (this.classInfoCache.containsKey(className)) return this.classInfoCache.get(className);
        return this.of(this.classLoader.loadClass(className));
    }

    @Nonnull
    @Override
    public synchronized JVMClassInfo of(Class<?> clazz) {
        if (this.classInfoCache.containsKey(clazz.getName())) return this.classInfoCache.get(clazz.getName());
        JVMClassInfo classInfo = new JVMClassInfo(this, clazz);
        this.classInfoCache.put(clazz.getName(), classInfo);
        return classInfo;
    }

}
