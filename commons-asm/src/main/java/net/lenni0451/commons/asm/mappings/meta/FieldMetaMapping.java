package net.lenni0451.commons.asm.mappings.meta;

import lombok.Data;
import lombok.With;

@Data
@With
public class FieldMetaMapping {

    private final String name;
    private final String descriptor;
    private final String[] javadoc;

    public boolean isEmpty() {
        return this.javadoc.length == 0;
    }

}
