package net.lenni0451.commons.asm.mappings;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MappingsTest {

    @Test
    void testUpdateReverse() {
        Mappings mappings = new Mappings();
        mappings.addClassMapping("a/b/C", "x/y/Z");
        mappings.addFieldMapping("a/b/C", "field", "I", "renamedField");
        mappings.addMethodMapping("a/b/C", "method", "()I", "renamedMethod");
        Mappings reverse = mappings.reverse(Mappings.ReverseCacheMode.UPDATE);

        //Adding member mappings must not deadlock in mutual recursion between the linked instances
        mappings.addFieldMapping("a/b/C", "field2", "I", "renamedField2");
        mappings.addFieldMapping("a/b/C", "field3", null, "renamedField3");
        mappings.addMethodMapping("a/b/C", "method2", "()I", "renamedMethod2");
        assertEquals("field2", reverse.mapFieldName("x/y/Z", "renamedField2", "I"));
        assertEquals("field3", reverse.mapFieldName("x/y/Z", "renamedField3", "I"));
        assertEquals("method2", reverse.mapMethodName("x/y/Z", "renamedMethod2", "()I"));

        reverse.addFieldMapping("x/y/Z", "rField", "I", "rRenamedField");
        reverse.addFieldMapping("x/y/Z", "rField2", null, "rRenamedField2");
        reverse.addMethodMapping("x/y/Z", "rMethod", "()I", "rRenamedMethod");
        assertEquals("rField", mappings.mapFieldName("a/b/C", "rRenamedField", "I"));
        assertEquals("rField2", mappings.mapFieldName("a/b/C", "rRenamedField2", "I"));
        assertEquals("rMethod", mappings.mapMethodName("a/b/C", "rRenamedMethod", "()I"));
    }

    @Test
    void testRecreateReverse() {
        Mappings mappings = new Mappings();
        mappings.addClassMapping("a/b/C", "x/y/Z");
        mappings.reverse(Mappings.ReverseCacheMode.RECREATE);

        //Adding a field mapping must invalidate the linked reverse instance
        mappings.addFieldMapping("a/b/C", "field", "I", "renamedField");
        Mappings recreated = mappings.reverse(Mappings.ReverseCacheMode.RECREATE);
        assertEquals("field", recreated.mapFieldName("x/y/Z", "renamedField", "I"));
        assertEquals("a/b/C", recreated.map("x/y/Z"));
    }

    @Test
    void testStandaloneReverse() {
        Mappings mappings = new Mappings();
        mappings.addClassMapping("a/b/C", "x/y/Z");
        Mappings reverse = mappings.reverse(Mappings.ReverseCacheMode.STANDALONE);

        reverse.addFieldMapping("x/y/Z", "field", "I", "renamedField");
        assertEquals("renamedField", reverse.mapFieldName("x/y/Z", "field", "I"));
        assertEquals(0, mappings.getFieldMappings().size());
    }

}
