package net.lenni0451.commons.asm.annotations.parser;

import org.junit.jupiter.api.Test;
import org.objectweb.asm.tree.AnnotationNode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.lenni0451.commons.asm.Types.typeDescriptor;
import static org.junit.jupiter.api.Assertions.*;

class AnnotationParserTest {

    @Test
    void parse() {
        AnnotationNode annotationNode = new AnnotationNode(typeDescriptor(Target.class));
        List<Object> values = new ArrayList<>();
        values.add("value");
        values.add(Arrays.asList(new String[]{typeDescriptor(ElementType.class), "FIELD"}, new String[]{typeDescriptor(ElementType.class), "METHOD"}));
        annotationNode.values = values;

        Target target = AnnotationParser.parse(AnnotationParserTest.class.getClassLoader(), Target.class, annotationNode);
        assertNotNull(target);
        assertArrayEquals(new ElementType[]{ElementType.FIELD, ElementType.METHOD}, target.value());
        assertInstanceOf(ParsedAnnotation.class, target);

        ParsedAnnotation parsedAnnotation = (ParsedAnnotation) target;
        assertTrue(parsedAnnotation.isSet("value"));
        assertFalse(parsedAnnotation.isSet("value2"));
        assertEquals(1, parsedAnnotation.getValues().size());
        assertNotNull(parsedAnnotation.getValues().get("value"));
        assertNotNull(parsedAnnotation.getValue("value"));
    }

}
