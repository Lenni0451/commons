package net.lenni0451.commons.asm.mappings.loader.formats;

import net.lenni0451.commons.asm.mappings.Mappings;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RetroguardMappingsLoaderTest {

    private static final String MAPPINGS = String.join("\n",
            ".class_map a b",
            ".field_map a/c d",
            ".method_map a/e ()I f"
    );

    @Test
    void test() {
        RetroguardMappingsLoader loader = new RetroguardMappingsLoader(new ByteArrayInputStream(MAPPINGS.getBytes(StandardCharsets.UTF_8)));
        Mappings mappings = loader.getMappings();
        assertEquals(1, mappings.getClassMappings().size());
        assertEquals("b", mappings.map("a"));
        assertEquals(1, mappings.getFieldMappings().size());
        assertEquals("d", mappings.mapFieldName("a", "c", "I"));
        assertEquals(1, mappings.getMethodMappings().size());
        assertEquals("f", mappings.mapMethodName("a", "e", "()I"));
    }

}
