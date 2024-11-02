package net.lenni0451.commons.asm.mappings.meta;

import lombok.Data;
import lombok.With;

import java.util.List;

@Data
@With
public class MethodMetaMapping {

    private final String name;
    private final String descriptor;
    private final String[] javadoc;
    private final List<ParameterMetaMapping> parameters;

    public boolean isEmpty() {
        return this.javadoc.length == 0;
    }

}
