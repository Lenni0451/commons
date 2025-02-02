package net.lenni0451.commons.asm.info.impl.asm;

import lombok.RequiredArgsConstructor;
import net.lenni0451.commons.asm.info.MethodInfo;
import org.objectweb.asm.tree.MethodNode;

@RequiredArgsConstructor
public class ASMMethodInfo implements MethodInfo {

    private final MethodNode methodNode;

    @Override
    public int getModifiers() {
        return this.methodNode.access;
    }

    @Override
    public String getName() {
        return this.methodNode.name;
    }

    @Override
    public String getDescriptor() {
        return this.methodNode.desc;
    }

}
