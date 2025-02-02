package net.lenni0451.commons.asm.mappings;

import net.lenni0451.commons.asm.info.impl.jvm.JVMClassInfoProvider;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MappingsFillerTest {

    @Test
    void test() {
        Mappings mappings = new Mappings();
        mappings.addClassMapping("java/lang/Integer", "java/lang/Integer"); //Required for MappingsFiller#getAllMentionedClasses to find the class
        mappings.addMethodMapping("java/lang/Number", "intValue", "()I", "noLongerIntValue");
        assertEquals("intValue", mappings.mapMethodName("java/lang/Integer", "intValue", "()I"));
        MappingsFiller.fillAllSuperMembers(mappings, new JVMClassInfoProvider());
        assertEquals("noLongerIntValue", mappings.mapMethodName("java/lang/Integer", "intValue", "()I"));
    }

}
