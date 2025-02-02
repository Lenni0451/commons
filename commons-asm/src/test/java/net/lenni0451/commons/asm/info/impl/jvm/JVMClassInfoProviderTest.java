package net.lenni0451.commons.asm.info.impl.jvm;

import net.lenni0451.commons.asm.info.ClassInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static net.lenni0451.commons.asm.ASMUtils.slash;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JVMClassInfoProviderTest {

    @Test
    void test() {
        JVMClassInfoProvider provider = new JVMClassInfoProvider();
        ClassInfo classInfo = Assertions.assertDoesNotThrow(() -> provider.of(slash(JVMClassInfoProviderTest.class.getName())));
        assertNotNull(classInfo);
        assertEquals(JVMClassInfoProviderTest.class.getModifiers(), classInfo.getModifiers());
        assertEquals(slash(JVMClassInfoProviderTest.class.getName()), classInfo.getName());
        assertEquals(slash(JVMClassInfoProviderTest.class.getSuperclass().getName()), classInfo.getSuperClass());
        assertNotNull(classInfo.getSuperClassInfo());
        assertEquals(JVMClassInfoProviderTest.class.getInterfaces().length, classInfo.getInterfaces().length);
        assertEquals(JVMClassInfoProviderTest.class.getInterfaces().length, classInfo.getInterfaceInfos().length);
        assertEquals(JVMClassInfoProviderTest.class.getDeclaredFields().length, classInfo.getFields().length);
        assertEquals(JVMClassInfoProviderTest.class.getDeclaredMethods().length, classInfo.getMethods().length);
    }

}
