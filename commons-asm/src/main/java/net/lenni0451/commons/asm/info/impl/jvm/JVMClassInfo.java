package net.lenni0451.commons.asm.info.impl.jvm;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.lenni0451.commons.asm.info.ClassInfo;
import net.lenni0451.commons.asm.info.ClassInfoProvider;
import net.lenni0451.commons.asm.info.FieldInfo;
import net.lenni0451.commons.asm.info.MethodInfo;
import net.lenni0451.commons.asm.info.impl.ClassInfoResolver;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

import static net.lenni0451.commons.asm.ASMUtils.slash;

@RequiredArgsConstructor
public class JVMClassInfo implements ClassInfo {

    private final ClassInfoProvider classInfoProvider;
    private final Class<?> clazz;

    @Getter(lazy = true)
    private final ClassInfo superClassInfo = ClassInfoResolver.resolveSuperClass(this.classInfoProvider, this);
    @Getter(lazy = true)
    private final ClassInfo[] interfaceInfos = ClassInfoResolver.resolveInterfaces(this.classInfoProvider, this);
    @Getter(lazy = true)
    private final Set<ClassInfo> recursiveSuperClasses = ClassInfoResolver.recursiveResolveSuperClasses(this, false);
    @Getter(lazy = true)
    private final FieldInfo[] fields = this.resolveFields();
    @Getter(lazy = true)
    private final MethodInfo[] methods = this.resolveMethods();

    @Override
    public int getModifiers() {
        return this.clazz.getModifiers();
    }

    @Override
    public String getName() {
        return slash(this.clazz.getName());
    }

    @Override
    public String getSuperClass() {
        if (this.clazz.getSuperclass() == null) return null;
        return slash(this.clazz.getSuperclass().getName());
    }

    @Override
    public String[] getInterfaces() {
        Class<?>[] interfaces = this.clazz.getInterfaces();
        String[] interfaceNames = new String[interfaces.length];
        for (int i = 0; i < interfaces.length; i++) {
            interfaceNames[i] = slash(interfaces[i].getName());
        }
        return interfaceNames;
    }


    private FieldInfo[] resolveFields() {
        Field[] fields = this.clazz.getDeclaredFields();
        FieldInfo[] fieldInfos = new FieldInfo[fields.length];
        for (int i = 0; i < fields.length; i++) {
            fieldInfos[i] = new JVMFieldInfo(fields[i]);
        }
        return fieldInfos;
    }

    private MethodInfo[] resolveMethods() {
        Method[] methods = this.clazz.getDeclaredMethods();
        MethodInfo[] methodInfos = new MethodInfo[methods.length];
        for (int i = 0; i < methods.length; i++) {
            methodInfos[i] = new JVMMethodInfo(methods[i]);
        }
        return methodInfos;
    }

}
