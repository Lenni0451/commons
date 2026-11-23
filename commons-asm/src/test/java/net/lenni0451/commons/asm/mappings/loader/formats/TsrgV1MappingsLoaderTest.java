package net.lenni0451.commons.asm.mappings.loader.formats;

import net.lenni0451.commons.asm.mappings.Mappings;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TsrgV1MappingsLoaderTest {

    private static final String MAPPINGS = String.join("\n",
            "a b",
            "\tc d",
            "\te ()I f"
    );
    private static final String MAPPINGS_WITH_HEADER = String.join("\n",
            "tsrg left right",
            "a b",
            "\tc d",
            "\te ()I f"
    );
    private static final String MAPPINGS_WITH_TAB_HEADER = String.join("\n",
            "tsrg\tleft\tright",
            "a b",
            "\tc d",
            "\te ()I f"
    );

    @Test
    void test() {
        test(MAPPINGS);
    }

    @Test
    void testHeader() {
        test(MAPPINGS_WITH_HEADER);
        test(MAPPINGS_WITH_TAB_HEADER);
    }

    @Test
    void testComment() {
        //A 2-token comment must not be parsed as a class mapping
        test("# a\n" + MAPPINGS);
    }

    @Test
    void testTsrgClassName() {
        //A class named 'tsrg' must not be skipped as the optional header
        TsrgV1MappingsLoader loader = new TsrgV1MappingsLoader(new ByteArrayInputStream("tsrg a\n\tc d".getBytes(StandardCharsets.UTF_8)));
        Mappings mappings = loader.getMappings();
        assertEquals("a", mappings.map("tsrg"));
        assertEquals("d", mappings.mapFieldName("tsrg", "c", "I"));
    }

    private static void test(final String content) {
        TsrgV1MappingsLoader loader = new TsrgV1MappingsLoader(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));
        Mappings mappings = loader.getMappings();
        assertEquals(1, mappings.getClassMappings().size());
        assertEquals("b", mappings.map("a"));
        assertEquals(1, mappings.getFieldMappings().size());
        assertEquals("d", mappings.mapFieldName("a", "c", "I"));
        assertEquals(1, mappings.getMethodMappings().size());
        assertEquals("f", mappings.mapMethodName("a", "e", "()I"));
    }

}
