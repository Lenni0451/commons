package net.lenni0451.commons.asm.mappings.meta;

import lombok.Data;
import lombok.With;

import java.util.List;

@Data
@With
public class ClassMetaMapping {

    private final String name;
    private final String[] javadoc;
    private final List<FieldMetaMapping> fields;
    private final List<MethodMetaMapping> methods;

    public boolean isEmpty() {
        return this.javadoc.length == 0 && this.fields.stream().allMatch(FieldMetaMapping::isEmpty) && this.methods.stream().allMatch(MethodMetaMapping::isEmpty);
    }

}
