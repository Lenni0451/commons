package net.lenni0451.commons.asm.info.impl.jvm;

import jdk.internal.org.objectweb.asm.Type;
import lombok.RequiredArgsConstructor;
import net.lenni0451.commons.asm.info.FieldInfo;

import java.lang.reflect.Field;

@RequiredArgsConstructor
public class JVMFieldInfo implements FieldInfo {

    private final Field field;

    @Override
    public int getModifiers() {
        return this.field.getModifiers();
    }

    @Override
    public String getName() {
        return this.field.getName();
    }

    @Override
    public String getDescriptor() {
        return Type.getDescriptor(this.field.getType());
    }

}
