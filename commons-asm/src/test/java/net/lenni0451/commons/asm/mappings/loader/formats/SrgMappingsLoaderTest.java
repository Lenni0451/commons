package net.lenni0451.commons.asm.mappings.loader.formats;

import net.lenni0451.commons.asm.mappings.Mappings;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SrgMappingsLoaderTest {

    private static final String MAPPINGS = String.join("\n",
            "PK: . x",
            "CL: a b",
            "FD: a/c x/b/d",
            "MD: a/e ()I x/b/f"
    );

    @Test
    void test() {
        SrgMappingsLoader loader = new SrgMappingsLoader(new ByteArrayInputStream(MAPPINGS.getBytes(StandardCharsets.UTF_8)));
        Mappings mappings = loader.getMappings();
        assertEquals(1, mappings.getPackageMappings().size());
        assertEquals("x/", mappings.mapClassPackage(""));
        assertEquals(1, mappings.getClassMappings().size());
        assertEquals("x/b", mappings.map("a"));
        assertEquals(1, mappings.getFieldMappings().size());
        assertEquals("d", mappings.mapFieldName("a", "c", "I"));
        assertEquals(1, mappings.getMethodMappings().size());
        assertEquals("f", mappings.mapMethodName("a", "e", "()I"));
    }

}
