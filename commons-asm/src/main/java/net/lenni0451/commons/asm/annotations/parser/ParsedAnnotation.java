package net.lenni0451.commons.asm.annotations.parser;

import java.util.Map;

/**
 * An interface implemented by annotations parsed by the {@link AnnotationParser}.
 */
public interface ParsedAnnotation {

    /**
     * Check if the value of a key is set.
     *
     * @param key The key
     * @return If the value is set
     */
    boolean isSet(final String key);

    /**
     * @return The raw annotation values
     */
    Map<String, Object> getValues();

    /**
     * Get the raw value of a key.
     *
     * @param key The key
     * @return The value
     */
    Object getValue(final String key);

}
