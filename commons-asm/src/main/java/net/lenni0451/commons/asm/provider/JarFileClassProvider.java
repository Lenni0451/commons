package net.lenni0451.commons.asm.provider;

import lombok.SneakyThrows;

import java.io.File;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.util.Collections;

public class JarFileClassProvider extends FileSystemClassProvider {

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
