package net.lenni0451.commons.asm.mappings.meta;

import lombok.Data;
import lombok.With;

import javax.annotation.Nonnull;

/**
 * Metadata mappings for a field.<br>
 * Some mappings formats may support additional information like javadoc.
 */
@Data
@With
public class FieldMetaMapping {

    @Nonnull
    private final String name;
    @Nonnull
    private final String descriptor;
    @Nonnull
    private final String[] javadoc;

    /**
     * @return If the field has javadoc
     */
    public boolean hasJavadoc() {
        for (String line : this.javadoc) {
            if (!line.trim().isEmpty()) return true;
        }
        return false;
    }

    /**
     * @return If there are no javadoc lines
     */
    public boolean isEmpty() {
        return this.javadoc.length == 0;
    }

}
