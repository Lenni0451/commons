package net.lenni0451.commons.asm.info.impl.asm;

import lombok.RequiredArgsConstructor;
import net.lenni0451.commons.asm.info.FieldInfo;
import org.objectweb.asm.tree.FieldNode;

@RequiredArgsConstructor
public class ASMFieldInfo implements FieldInfo {

    private final FieldNode fieldNode;

    @Override
    public int getModifiers() {
        return this.fieldNode.access;
    }

    @Override
    public String getName() {
        return this.fieldNode.name;
    }

    @Override
    public String getDescriptor() {
        return this.fieldNode.desc;
    }

}
