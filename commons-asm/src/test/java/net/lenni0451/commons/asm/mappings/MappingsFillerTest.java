package net.lenni0451.commons.asm.mappings;

import net.lenni0451.commons.asm.info.impl.jvm.JVMClassInfoProvider;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MappingsFillerTest {

    @Test
    void test() {
        Mappings mappings = new Mappings();
        mappings.addMethodMapping("java/lang/Number", "intValue", "()I", "noLongerIntValue");
        assertEquals("intValue", mappings.mapMethodName("java/lang/Integer", "intValue", "()I"));
        MappingsFiller.fillAllSuperMembers(mappings, new JVMClassInfoProvider(), Collections.singleton("java/lang/Integer"));
        assertEquals("noLongerIntValue", mappings.mapMethodName("java/lang/Integer", "intValue", "()I"));
    }

}
