package net.lenni0451.commons.io;

import net.lenni0451.commons.Sneaky;
import net.lenni0451.commons.collections.Maps;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * A fast zip file using the {@link FileSystem} API.
 */
public class FileSystemZip implements AutoCloseable {

    private final File file;
    private final FileSystem zipFileSystem;
    private final Path rootPath;

    /**
     * Construct a new file system zip.<br>
     * If the file does not exist a new zip file will be created.
     *
     * @param file The file to read/write
     * @throws IOException If an I/O error occurs
     */
    public FileSystemZip(final File file) throws IOException {
        this.file = file;
        this.zipFileSystem = Sneaky.sneak(() -> FileSystems.newFileSystem(new URI("jar:" + file.toURI()), Maps.hashMap("create", "true")));
        this.rootPath = this.zipFileSystem.getRootDirectories().iterator().next();

        //Fake exception to let the compiler know that the exception can actually be thrown
        Sneaky.fake(IOException.class);
    }

    /**
     * @return The file that is being read/written
     */
    public File getFile() {
        return this.file;
    }

    /**
     * Add an entry to the zip file.<br>
     * If {@code bytes} is {@code null} a directory will be created.
     *
     * @param name  The name of the entry
     * @param bytes The data of the entry
     * @throws IOException If an I/O error occurs
     */
    public void addEntry(final String name, @Nullable final byte[] bytes) throws IOException {
        Path path = this.rootPath.resolve(name);
        if (bytes == null) {
            Files.createDirectories(path);
        } else {
            Files.createDirectories(path.getParent());
            Files.write(path, bytes);
        }
    }

    /**
     * Add a directory to the zip file.
     *
     * @param name The name of the directory
     * @throws IOException If an I/O error occurs
     */
    public void addDirectory(final String name) throws IOException {
        this.addEntry(name, null);
    }

    /**
     * Get an entry from the zip file.
     *
     * @param name The name of the entry
     * @return The entry
     * @throws FileNotFoundException If the entry does not exist
     */
    public Entry getEntry(final String name) throws FileNotFoundException {
        Path path = this.rootPath.resolve(name);
        if (!Files.exists(path)) throw new FileNotFoundException("File not found: " + name);
        return new Entry(path);
    }

    /**
     * Check if an entry exists.
     *
     * @param name The name of the entry
     * @return If the entry exists
     */
    public boolean hasEntry(final String name) {
        Path path = this.rootPath.resolve(name);
        return Files.exists(path);
    }

    /**
     * Check if an entry exists and is a file.
     *
     * @param name The name of the entry
     * @return If the entry exists
     */
    public boolean hasFile(final String name) {
        Path path = this.rootPath.resolve(name);
        return Files.exists(path) && !Files.isDirectory(path);
    }

    /**
     * Check if an entry exists and is a directory.
     *
     * @param name The name of the entry
     * @return If the entry exists
     */
    public boolean hasDirectory(final String name) {
        Path path = this.rootPath.resolve(name);
        return Files.exists(path) && Files.isDirectory(path);
    }

    /**
     * Iterate over all entries in the zip file.
     *
     * @param consumer The consumer to call for each entry
     * @throws IOException If an I/O error occurs
     */
    public void forEach(final Consumer<Entry> consumer) throws IOException {
        try (Stream<Path> paths = Files.walk(this.rootPath)) {
            paths.forEach(path -> consumer.accept(new Entry(path)));
        }
    }

    @Override
    public void close() throws Exception {
        this.zipFileSystem.close();
    }


    /**
     * The entry for a file in the zip file.
     */
    public class Entry {
        private final Path path;

        private Entry(final Path path) {
            this.path = path;
        }

        /**
         * @return The path of the entry
         */
        public Path getPath() {
            return this.path;
        }

        /**
         * @return The relative path of the entry
         */
        public String getRelativePath() {
            return FileSystemZip.this.rootPath.relativize(this.path).toString();
        }

        /**
         * @return The file name of the entry
         */
        public String getName() {
            Path fileName = this.path.getFileName();
            if (fileName == null) return "";
            return fileName.toString();
        }

        /**
         * @return If the entry exists
         */
        public boolean exists() {
            return Files.exists(this.path);
        }

        /**
         * @return If the entry is a file
         */
        public boolean isFile() {
            return !Files.isDirectory(this.path);
        }

        /**
         * @return If the entry is a directory
         */
        public boolean isDirectory() {
            return Files.isDirectory(this.path);
        }

        /**
         * Get the data of the entry.
         *
         * @return The data of the entry
         * @throws IllegalStateException If the entry is a directory
         * @throws IOException           If an I/O error occurs
         */
        public byte[] getData() throws IOException {
            if (this.isDirectory()) throw new IllegalStateException("Entry is a directory");
            return Files.readAllBytes(this.path);
        }

        /**
         * Set the data of the entry.<br>
         * If {@code bytes} is {@code null} a directory will be created.<br>
         * The entry can not be changed from a file to a directory or vice versa.
         *
         * @param bytes The data to set
         * @throws IllegalStateException If the entry has the wrong type
         * @throws IOException           If an I/O error occurs
         */
        public void setData(@Nullable final byte[] bytes) throws IOException {
            if (bytes == null) {
                if (this.isFile()) throw new IllegalStateException("Entry is a file");
                Files.createDirectories(this.path);
            } else {
                if (this.isDirectory()) throw new IllegalStateException("Entry is a directory");
                Files.createDirectories(this.path.getParent());
                Files.write(this.path, bytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            }
        }
    }

}
