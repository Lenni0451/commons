package net.lenni0451.commons.asm.mappings;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;

class MappingsTest {

    @Test
    void testUpdateReverse() {
        Mappings mappings = new Mappings();
        mappings.addClassMapping("a/b/C", "x/y/Z");
        mappings.addFieldMapping("a/b/C", "field", "I", "renamedField");
        mappings.addMethodMapping("a/b/C", "method", "()I", "renamedMethod");
        Mappings reverse = mappings.reverse(Mappings.ReverseCacheMode.UPDATE);

        //Adding member mappings must not recurse infinitely between the linked instances
        mappings.addFieldMapping("a/b/C", "field2", "I", "renamedField2");
        mappings.addFieldMapping("a/b/C", "field3", null, "renamedField3");
        mappings.addFieldMapping("a/b/C", "refField", "La/b/C;", "renamedRefField");
        mappings.addMethodMapping("a/b/C", "method2", "()I", "renamedMethod2");
        mappings.addMethodMapping("a/b/C", "refMethod", "(La/b/C;)La/b/C;", "renamedRefMethod");
        assertEquals("field2", reverse.mapFieldName("x/y/Z", "renamedField2", "I"));
        assertEquals("field3", reverse.mapFieldName("x/y/Z", "renamedField3", "I"));
        assertEquals("refField", reverse.mapFieldName("x/y/Z", "renamedRefField", "Lx/y/Z;"));
        assertEquals("method2", reverse.mapMethodName("x/y/Z", "renamedMethod2", "()I"));
        assertEquals("refMethod", reverse.mapMethodName("x/y/Z", "renamedRefMethod", "(Lx/y/Z;)Lx/y/Z;"));

        reverse.addFieldMapping("x/y/Z", "rField", "I", "rRenamedField");
        reverse.addFieldMapping("x/y/Z", "rField2", null, "rRenamedField2");
        reverse.addFieldMapping("x/y/Z", "rRefField", "Lx/y/Z;", "rRenamedRefField");
        reverse.addMethodMapping("x/y/Z", "rMethod", "()I", "rRenamedMethod");
        reverse.addMethodMapping("x/y/Z", "rRefMethod", "(Lx/y/Z;)Lx/y/Z;", "rRenamedRefMethod");
        assertEquals("rField", mappings.mapFieldName("a/b/C", "rRenamedField", "I"));
        assertEquals("rField2", mappings.mapFieldName("a/b/C", "rRenamedField2", "I"));
        assertEquals("rRefField", mappings.mapFieldName("a/b/C", "rRenamedRefField", "La/b/C;"));
        assertEquals("rMethod", mappings.mapMethodName("a/b/C", "rRenamedMethod", "()I"));
        assertEquals("rRefMethod", mappings.mapMethodName("a/b/C", "rRenamedRefMethod", "(La/b/C;)La/b/C;"));
    }

    @Test
    void testUpdateReverseOverwrite() {
        Mappings mappings = new Mappings();
        mappings.addClassMapping("a/b/C", "x/y/Z");
        mappings.addFieldMapping("a/b/C", "field", "I", "renamedField");
        mappings.addMethodMapping("a/b/C", "method", "()I", "renamedMethod");
        mappings.addPackageMapping("a/b", "x/y");
        Mappings reverse = mappings.reverse(Mappings.ReverseCacheMode.UPDATE);

        //Overwriting a member mapping must remove the stale reverse key instead of leaving it behind
        mappings.addFieldMapping("a/b/C", "field", "I", "renamedField2");
        mappings.addMethodMapping("a/b/C", "method", "()I", "renamedMethod2");
        assertEquals("field", reverse.mapFieldName("x/y/Z", "renamedField2", "I"));
        assertEquals("method", reverse.mapMethodName("x/y/Z", "renamedMethod2", "()I"));
        assertEquals("renamedField", reverse.mapFieldName("x/y/Z", "renamedField", "I"));
        assertEquals("renamedMethod", reverse.mapMethodName("x/y/Z", "renamedMethod", "()I"));

        //Class and package overwrites must drop the stale reverse keys as well
        mappings.addClassMapping("a/b/C", "x/y/W");
        mappings.addPackageMapping("a/b", "p/q");
        assertEquals("a/b/C", reverse.map("x/y/W"));
        assertEquals("x/y/Z", reverse.map("x/y/Z"));
        assertEquals("a/b/", reverse.getPackageMappings().get("p/q/"));
        assertNull(reverse.getPackageMappings().get("x/y/"));
    }

    @Test
    void testRecreateReverse() {
        Mappings mappings = new Mappings();
        mappings.addClassMapping("a/b/C", "x/y/Z");
        Mappings initial = mappings.reverse(Mappings.ReverseCacheMode.RECREATE);

        //Adding a field mapping must invalidate the cached reverse instance
        mappings.addFieldMapping("a/b/C", "field", "I", "renamedField");
        Mappings recreated = mappings.reverse(Mappings.ReverseCacheMode.RECREATE);
        assertNotSame(initial, recreated);
        assertEquals("field", recreated.mapFieldName("x/y/Z", "renamedField", "I"));
        assertEquals("a/b/C", recreated.map("x/y/Z"));

        //Adding on the reverse side must invalidate the link as well
        recreated.addFieldMapping("x/y/Z", "rField", "I", "rRenamedField");
        assertNotSame(recreated, mappings.reverse(Mappings.ReverseCacheMode.RECREATE));
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
