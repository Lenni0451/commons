package net.lenni0451.commons.asm.mappings.loader;

import net.lenni0451.commons.asm.mappings.Mappings;

import javax.annotation.WillClose;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public abstract class MappingsLoader {

    private final MappingsProvider mappingsProvider;
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

    private MappingsLoader(final MappingsProvider mappingsProvider) {
        this.mappingsProvider = mappingsProvider;
    }

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

    public synchronized final void load() throws Throwable {
        if (this.mappings != null) return;
        this.mappings = this.load(this.readLines());
    }

    protected abstract Mappings load(final List<String> lines) throws Throwable;

    private List<String> readLines() throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(this.mappingsProvider.load()))) {
            return br.lines().filter(s -> !s.trim().isEmpty()).collect(Collectors.toList());
        }
    }


    @FunctionalInterface
    private interface MappingsProvider {
        InputStream load() throws IOException;
    }

}
