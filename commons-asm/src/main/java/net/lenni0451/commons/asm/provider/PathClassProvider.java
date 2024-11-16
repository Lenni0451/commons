package net.lenni0451.commons.asm.provider;

import lombok.SneakyThrows;

import javax.annotation.Nonnull;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

import static net.lenni0451.commons.asm.ASMUtils.slash;

/**
 * A class provider that searches for classes in a path.
 */
public class PathClassProvider implements ClassProvider {

    private final Path path;

    public PathClassProvider(final Path path) {
        this.path = path;
    }

    public Path getPath() {
        return this.path;
    }

    @Nonnull
    @Override
    public byte[] getClass(String name) throws ClassNotFoundException {
        try {
            Path file = this.path.resolve(slash(name) + ".class");
            if (Files.exists(file)) return Files.readAllBytes(file);
        } catch (Throwable t) {
            throw new ClassNotFoundException(name, t);
        }
        throw new ClassNotFoundException(name);
    }

    @Nonnull
    @Override
    @SneakyThrows
    public Map<String, ClassSupplier> getAllClasses() {
        try (Stream<Path> paths = Files.find(this.path, Integer.MAX_VALUE, this::isClass)) {
            return paths.collect(HashMap::new, (m, p) -> {
                String name = this.path.relativize(p).toString();
                name = name.substring(0, name.length() - 6);
                m.put(name.replace("\\", "/"), () -> Files.readAllBytes(p));
            }, Map::putAll);
        }
    }

    private boolean isClass(final Path path, final BasicFileAttributes attributes) {
        Path fileName = path.getFileName();
        if (fileName == null) return false;
        return fileName.toString().toLowerCase(Locale.ROOT).endsWith(".class") && attributes.isRegularFile();
    }

}
