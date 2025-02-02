package net.lenni0451.commons.asm.info.impl.asm;

import lombok.SneakyThrows;
import net.lenni0451.commons.asm.info.ClassInfoProvider;
import net.lenni0451.commons.asm.provider.ClassProvider;
import org.objectweb.asm.tree.ClassNode;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class ASMClassInfoProvider implements ClassInfoProvider {

    private final ClassProvider classProvider;
    private final Map<String, ASMClassInfo> classInfoCache;

    public ASMClassInfoProvider(final ClassProvider classProvider) {
        this.classProvider = classProvider;
        this.classInfoCache = new HashMap<>();
    }

    public ClassProvider getClassProvider() {
        return this.classProvider;
    }

    @Nonnull
    @Override
    @SneakyThrows
    public synchronized ASMClassInfo of(String className) {
        if (this.classInfoCache.containsKey(className)) return this.classInfoCache.get(className);
        return this.of(this.classProvider.getClassNode(className));
    }

    @Nonnull
    @Override
    public synchronized ASMClassInfo of(ClassNode classNode) {
        if (this.classInfoCache.containsKey(classNode.name)) return this.classInfoCache.get(classNode.name);
        ASMClassInfo classInfo = new ASMClassInfo(this, classNode);
        this.classInfoCache.put(classNode.name, classInfo);
        return classInfo;
    }

}
