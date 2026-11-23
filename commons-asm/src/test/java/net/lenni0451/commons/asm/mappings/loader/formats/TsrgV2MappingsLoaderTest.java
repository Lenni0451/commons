package net.lenni0451.commons.asm.mappings.loader.formats;

import net.lenni0451.commons.asm.mappings.Mappings;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TsrgV2MappingsLoaderTest {

    private static final String MAPPINGS = String.join("\n",
            "tsrg2 obf mapped",
            "a/b/ x/y/",
            "c d",
            "\tf1 g1",
            "\tf2 I g2",
            "\tm1 ()V n1",
            "\t\tstatic",
            "\t\t0 p1 q1",
            "\tm2 (I)J n2",
            "\t\t0 px qx"
    );

    @Test
    void test() {
        TsrgV2MappingsLoader loader = new TsrgV2MappingsLoader(new ByteArrayInputStream(MAPPINGS.getBytes(StandardCharsets.UTF_8)), "obf", "mapped");
        Mappings mappings = loader.getMappings();
        assertEquals("x/y/C", mappings.mapClassPackage("a/b/C"));
        assertEquals(1, mappings.getClassMappings().size());
        assertEquals("d", mappings.map("c"));
        assertEquals(2, mappings.getFieldMappings().size());
        assertEquals("g1", mappings.mapFieldName("c", "f1", "I"));
        assertEquals("g2", mappings.mapFieldName("c", "f2", "I"));
        assertEquals(2, mappings.getMethodMappings().size());
        assertEquals("n1", mappings.mapMethodName("c", "m1", "()V"));
        assertEquals("n2", mappings.mapMethodName("c", "m2", "(I)J"));
    }

    @Test
    void testNamespaces() {
        String content = String.join("\n",
                "tsrg2 a b c",
                "k1 k2 k3",
                "\tf fb fc",
                "\tm ()V mb mc"
        );
        TsrgV2MappingsLoader loader = new TsrgV2MappingsLoader(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)), "a", "c");
        Mappings mappings = loader.getMappings();
        assertEquals("k3", mappings.map("k1"));
        assertEquals("fc", mappings.mapFieldName("k1", "f", null));
        assertEquals("mc", mappings.mapMethodName("k1", "m", "()V"));
    }

    @Test
    void testDescriptorsRemappedToSourceNamespace() {
        // Descriptors are always in the first namespace and must be remapped to the source namespace
        String content = String.join("\n",
                "tsrg2 obf srg mcp",
                "a net/Foo net/Foo",
                "b net/Bar net/Bar",
                "\tx La; f_1_ value",
                "\ty (La;I)Lb; m_1_ convert"
        );
        TsrgV2MappingsLoader loader = new TsrgV2MappingsLoader(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)), "srg", "mcp");
        Mappings mappings = loader.getMappings();
        assertEquals("value", mappings.mapFieldName("net/Bar", "f_1_", "Lnet/Foo;"));
        assertEquals("convert", mappings.mapMethodName("net/Bar", "m_1_", "(Lnet/Foo;I)Lnet/Bar;"));
    }

    @Test
    void testMissingNamespace() {
        TsrgV2MappingsLoader loader = new TsrgV2MappingsLoader(new ByteArrayInputStream(MAPPINGS.getBytes(StandardCharsets.UTF_8)), "missing", "mapped");
        assertThrows(RuntimeException.class, loader::getMappings);
    }

}
