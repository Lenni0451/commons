package net.lenni0451.commons.asm.mappings.meta;

import lombok.Data;
import lombok.With;

import javax.annotation.Nonnull;

@Data
@With
public class ParameterMetaMapping {

    private final int index;
    @Nonnull
    private final String name;
    @Nonnull
    private final String[] javadoc;

    public boolean hasJavadoc() {
        for (String line : this.javadoc) {
            if (!line.trim().isEmpty()) return true;
        }
        return false;
    }

}
