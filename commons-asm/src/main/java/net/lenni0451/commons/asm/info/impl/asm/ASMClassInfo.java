package net.lenni0451.commons.asm.info.impl.asm;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.lenni0451.commons.asm.info.ClassInfo;
import net.lenni0451.commons.asm.info.ClassInfoProvider;
import net.lenni0451.commons.asm.info.FieldInfo;
import net.lenni0451.commons.asm.info.MethodInfo;
import net.lenni0451.commons.asm.info.impl.ClassInfoResolver;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class ASMClassInfo implements ClassInfo {

    private final ClassInfoProvider classInfoProvider;
    private final ClassNode classNode;

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

    public ClassInfoProvider getClassInfoProvider() {
        return this.classInfoProvider;
    }

    public ClassNode getClassNode() {
        return this.classNode;
    }

    @Override
    public int getModifiers() {
        return this.classNode.access;
    }

    @Override
    public String getName() {
        return this.classNode.name;
    }

    @Override
    public String getSuperClass() {
        return this.classNode.superName;
    }

    @Override
    public String[] getInterfaces() {
        return this.classNode.interfaces == null ? new String[0] : this.classNode.interfaces.toArray(new String[0]);
    }


    private FieldInfo[] resolveFields() {
        List<FieldNode> fields = this.classNode.fields;
        FieldInfo[] fieldInfos = new FieldInfo[fields.size()];
        for (int i = 0; i < fields.size(); i++) {
            fieldInfos[i] = new ASMFieldInfo(fields.get(i));
        }
        return fieldInfos;
    }

    private MethodInfo[] resolveMethods() {
        List<MethodNode> methods = this.classNode.methods;
        MethodInfo[] methodInfos = new MethodInfo[methods.size()];
        for (int i = 0; i < methods.size(); i++) {
            methodInfos[i] = new ASMMethodInfo(methods.get(i));
        }
        return methodInfos;
    }

}
