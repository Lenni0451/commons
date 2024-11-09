package net.lenni0451.commons.asm.annotations.parser;

import java.util.Map;

public interface ParsedAnnotation {

    boolean isSet(final String key);

    Map<String, Object> getValues();

    Object getValue(final String key);

}
