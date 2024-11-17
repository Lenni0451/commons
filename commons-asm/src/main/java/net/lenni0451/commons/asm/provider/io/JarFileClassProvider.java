package net.lenni0451.commons.asm.provider.io;

import lombok.SneakyThrows;
import net.lenni0451.commons.asm.provider.ClassProvider;
import net.lenni0451.commons.asm.provider.DelegatingClassProvider;
import net.lenni0451.commons.asm.provider.LazyClassProvider;

import java.io.File;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.util.Collection;
import java.util.Collections;

/**
 * A class provider that searches for classes in a jar file.<br>
 * The main implementation is done by {@link FileSystemClassProvider}.
 */
public class JarFileClassProvider extends FileSystemClassProvider {

    /**
     * Create a lazy delegating class provider using the given jar files.<br>
     * The jar files will only be opened when a class is requested which was not found in the previous jar files.
     *
     * @param jarFiles The jar files to search for classes
     * @return The created class provider
     */
    public static ClassProvider lazyDelegating(final Collection<File> jarFiles) {
        ClassProvider[] providers = new ClassProvider[jarFiles.size()];
        int i = 0;
        for (File jarFile : jarFiles) {
            providers[i++] = new LazyClassProvider(() -> new JarFileClassProvider(jarFile));
        }
        return new DelegatingClassProvider(providers);
    }


    public JarFileClassProvider(final File jarFile) {
        super(openFileSystem(jarFile), true);
    }

    @SneakyThrows
    private static FileSystem openFileSystem(final File jarFile) {
        URI uri = new URI("jar:" + jarFile.toURI());
        try {
            return FileSystems.getFileSystem(uri);
        } catch (FileSystemNotFoundException e) {
            return FileSystems.newFileSystem(uri, Collections.emptyMap());
        }
    }

}
