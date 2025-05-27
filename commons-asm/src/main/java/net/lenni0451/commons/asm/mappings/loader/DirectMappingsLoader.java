package net.lenni0451.commons.asm.mappings.loader;

import net.lenni0451.commons.asm.mappings.Mappings;

import java.io.ByteArrayInputStream;
import java.util.List;

/**
 * A loader that directly uses the provided mappings without any modifications.<br>
 * The mappings are copied to ensure that the original mappings are not modified.
 */
public class DirectMappingsLoader extends MappingsLoader {

    private final Mappings mappings;

    public DirectMappingsLoader(final Mappings mappings) {
        super(new ByteArrayInputStream(new byte[0]));
        this.mappings = mappings.copy();
    }

    @Override
    protected Mappings load(List<String> lines) throws Throwable {
        return this.mappings.copy();
    }

}
