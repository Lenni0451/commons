package net.lenni0451.commons.asm.mappings.loader;

import net.lenni0451.commons.asm.mappings.Mappings;

import java.io.ByteArrayInputStream;
import java.util.List;

/**
 * A loader which does not load any mappings.
 */
public class EmptyMappingsLoader extends MappingsLoader {

    public EmptyMappingsLoader() {
        super(new ByteArrayInputStream(new byte[0]));
    }

    @Override
    protected Mappings load(List<String> lines) {
        return new Mappings();
    }

}
