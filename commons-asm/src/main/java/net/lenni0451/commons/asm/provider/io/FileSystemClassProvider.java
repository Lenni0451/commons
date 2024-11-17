package net.lenni0451.commons.asm.provider.io;

import java.nio.file.FileSystem;

/**
 * A class provider that uses a {@link FileSystem} to provide classes.<br>
 * The main implementation is done by {@link PathClassProvider}.
 */
public class FileSystemClassProvider extends PathClassProvider {

    private final FileSystem fileSystem;
    private final boolean close;

    public FileSystemClassProvider(final FileSystem fileSystem, final boolean close) {
        super(fileSystem.getRootDirectories().iterator().next());
        this.fileSystem = fileSystem;
        this.close = close;
    }

    public FileSystem getFileSystem() {
        return this.fileSystem;
    }

    @Override
    public void close() throws Exception {
        super.close();
        if (this.close) this.fileSystem.close();
    }

}
