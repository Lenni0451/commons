package net.lenni0451.commons.asm.mappings.loader;

import net.lenni0451.commons.asm.mappings.Mappings;
import net.lenni0451.commons.asm.mappings.meta.ClassMetaMapping;

import java.util.Collections;
import java.util.List;

public interface MappingsProvider {

    /**
     * Create an empty provider that returns an empty mappings instance.
     *
     * @return An empty mappings provider
     */
    static MappingsProvider empty() {
        return Mappings::new;
    }

    /**
     * Create a provider that returns the given mappings instance directly.
     *
     * @param mappings The mappings to return
     * @return A direct mappings provider
     */
    static MappingsProvider direct(final Mappings mappings) {
        return () -> mappings;
    }

    /**
     * Create a provider that returns a copy of the given mappings instance.<br>
     * The initially passed mappings instance is copied when calling this method.
     *
     * @param mappings The mappings to copy
     * @return A copying mappings provider
     */
    static MappingsProvider copying(final Mappings mappings) {
        Mappings copy = new Mappings();
        return copy::copy;
    }


    /**
     * @return The loaded mappings
     */
    Mappings getMappings();

    /**
     * @return A copy of the loaded mappings
     */
    default Mappings copyMappings() {
        return this.getMappings().copy();
    }

    /**
     * Get the meta mappings for the mappings.
     *
     * @return The meta mappings
     * @see ClassMetaMapping
     */
    default List<ClassMetaMapping> getMetaMappings() {
        return Collections.emptyList();
    }

    /**
     * Get a provider that returns the given mappings instance.<br>
     * The meta mappings of the original provider are preserved.
     *
     * @param mappings The mappings to return
     * @return A mappings provider that returns the given mappings instance
     */
    default MappingsProvider withMappings(final Mappings mappings) {
        return new MappingsProvider() {
            @Override
            public Mappings getMappings() {
                return mappings;
            }

            @Override
            public List<ClassMetaMapping> getMetaMappings() {
                return MappingsProvider.this.getMetaMappings();
            }
        };
    }

    /**
     * Get a provider that returns the given meta mappings.<br>
     * The mappings of the original provider are preserved.
     *
     * @param metaMappings The meta mappings to return
     * @return A mappings provider that returns the given meta mappings
     */
    default MappingsProvider withMetaMappings(final List<ClassMetaMapping> metaMappings) {
        return new MappingsProvider() {
            @Override
            public Mappings getMappings() {
                return MappingsProvider.this.getMappings();
            }

            @Override
            public List<ClassMetaMapping> getMetaMappings() {
                return metaMappings;
            }
        };
    }

}
