package net.lenni0451.commons.asm.mappings.meta;

import lombok.Data;
import lombok.With;

@Data
@With
public class ParameterMetaMapping {

    private final int index;
    private final String name;
    private final String[] javadoc;

}
