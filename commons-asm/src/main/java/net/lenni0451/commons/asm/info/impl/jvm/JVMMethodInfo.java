package net.lenni0451.commons.asm.info.impl.jvm;

import lombok.RequiredArgsConstructor;
import net.lenni0451.commons.asm.info.MethodInfo;
import org.objectweb.asm.Type;

import java.lang.reflect.Method;

@RequiredArgsConstructor
public class JVMMethodInfo implements MethodInfo {

    private final Method method;

    @Override
    public int getModifiers() {
        return this.method.getModifiers();
    }

    @Override
    public String getName() {
        return this.method.getName();
    }

    @Override
    public String getDescriptor() {
        return Type.getMethodDescriptor(this.method);
    }

}
