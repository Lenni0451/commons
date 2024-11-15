package net.lenni0451.commons.asm.mappings.meta;

import lombok.Data;
import lombok.With;

import javax.annotation.Nonnull;
import java.util.List;

@Data
@With
public class ClassMetaMapping {

    @Nonnull
    private final String name;
    @Nonnull
    private final String[] javadoc;
    @Nonnull
    private final List<FieldMetaMapping> fields;
    @Nonnull
    private final List<MethodMetaMapping> methods;

    public boolean hasJavadoc() {
        for (String line : this.javadoc) {
            if (!line.trim().isEmpty()) return true;
        }
        return false;
    }

    public boolean isEmpty() {
        return this.javadoc.length == 0 && this.fields.stream().allMatch(FieldMetaMapping::isEmpty) && this.methods.stream().allMatch(MethodMetaMapping::isEmpty);
    }

}
