package net.lenni0451.commons.asm.mappings.loader.formats;

import net.lenni0451.commons.asm.mappings.Mappings;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TinyV2MappingsLoaderTest {

    private static final String MAPPINGS = String.join("\n",
            "tiny\t2\t0\tobf\tnamed",
            "c\ta\tb",
            "\tf\tI\tc\td",
            "\tm\t()I\te\tf"
    );

    @Test
    void test() {
        TinyV2MappingsLoader loader = new TinyV2MappingsLoader(new ByteArrayInputStream(MAPPINGS.getBytes(StandardCharsets.UTF_8)), "obf", "named");
        Mappings mappings = loader.getMappings();
        assertEquals(1, mappings.getClassMappings().size());
        assertEquals("b", mappings.map("a"));
        assertEquals(1, mappings.getFieldMappings().size());
        assertEquals("d", mappings.mapFieldName("a", "c", "I"));
        assertEquals(1, mappings.getMethodMappings().size());
        assertEquals("f", mappings.mapMethodName("a", "e", "()I"));
    }

}
