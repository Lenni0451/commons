package net.lenni0451.commons.asm.mappings.loader;

import net.lenni0451.commons.asm.mappings.Mappings;

import javax.annotation.WillClose;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An abstract class for various mappings loaders.
 */
public abstract class MappingsLoader implements MappingsProvider {

    private final IOProvider ioProvider;
    private Mappings mappings;

    public MappingsLoader(@WillClose final InputStream inputStream) {
        this(() -> inputStream);
    }

    public MappingsLoader(final File file) {
        this(file.toPath());
    }

    public MappingsLoader(final Path path) {
        this(() -> Files.newInputStream(path));
    }

    private MappingsLoader(final IOProvider ioProvider) {
        this.ioProvider = ioProvider;
    }

    /**
     * Get and load the mappings if they are not loaded yet.<br>
     * All exceptions that might occur when loading the mappings are rethrown using a {@link RuntimeException}.
     *
     * @return The loaded mappings
     */
    @Override
    public Mappings getMappings() {
        if (this.mappings == null) {
            try {
                this.load();
            } catch (Throwable t) {
                throw new RuntimeException("Failed to load mappings", t);
            }
        }
        return this.mappings;
    }

    /**
     * Load the mappings from the provided source if not already loaded.
     *
     * @return The loaded mappings
     * @throws Throwable If an error occurs while loading the mappings
     */
    public synchronized final Mappings load() throws Throwable {
        if (this.mappings != null) return this.mappings;
        List<String> lines = this.readLines();
        this.mappings = this.load(lines);
        return this.mappings;
    }

    /**
     * Load the mappings from the provided lines.
     *
     * @param lines The mappings lines
     * @return The loaded mappings
     * @throws Throwable If an error occurs while loading the mappings
     */
    protected abstract Mappings load(final List<String> lines) throws Throwable;

    private List<String> readLines() throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(this.ioProvider.load()))) {
            return br.lines().filter(s -> !s.trim().isEmpty()).collect(Collectors.toList());
        }
    }


    @FunctionalInterface
    private interface IOProvider {
        InputStream load() throws IOException;
    }

}
