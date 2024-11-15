package net.lenni0451.commons.asm.mappings.meta;

import lombok.Data;
import lombok.With;

import javax.annotation.Nonnull;

@Data
@With
public class FieldMetaMapping {

    @Nonnull
    private final String name;
    @Nonnull
    private final String descriptor;
    @Nonnull
    private final String[] javadoc;

    public boolean hasJavadoc() {
        for (String line : this.javadoc) {
            if (!line.trim().isEmpty()) return true;
        }
        return false;
    }

    public boolean isEmpty() {
        return this.javadoc.length == 0;
    }

}
