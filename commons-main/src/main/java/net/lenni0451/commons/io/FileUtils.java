package net.lenni0451.commons.io;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import net.lenni0451.commons.Sneaky;

import javax.annotation.WillNotClose;
import java.io.*;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public class FileUtils {

    /**
     * Create a file from an array of parts.
     *
     * @param parts The parts
     * @return The created file
     */
    public static File create(final String... parts) {
        return new File(String.join(File.separator, parts));
    }

    /**
     * Create a file from a parent file and an array of parts.
     *
     * @param file  The parent file
     * @param parts The parts
     * @return The created file
     */
    public static File create(final File file, final String... parts) {
        return new File(file, String.join(File.separator, parts));
    }

    /**
     * List all files in a directory recursively.
     *
     * @param file The directory to list
     * @return A list of all files in the directory
     * @throws IOException If an I/O error occurs
     */
    @SneakyThrows
    public static List<File> listFiles(final File file) {
        Sneaky.fake(IOException.class);
        try (Stream<Path> s = Files.walk(file.toPath(), FileVisitOption.FOLLOW_LINKS)) {
            return s.map(Path::toFile).collect(Collectors.toList());
        }
    }

    /**
     * List all files in a directory recursively.
     *
     * @param file   The directory to list
     * @param filter The filter to use
     * @return A list of all files in the directory
     * @throws IOException If an I/O error occurs
     */
    @SneakyThrows
    public static List<File> listFiles(final File file, final Predicate<File> filter) {
        Sneaky.fake(IOException.class);
        try (Stream<Path> s = Files.walk(file.toPath(), FileVisitOption.FOLLOW_LINKS)) {
            return s.map(Path::toFile).filter(filter).collect(Collectors.toList());
        }
    }

    /**
     * Read all bytes from a stream and write them to a file.
     *
     * @param file The file to write to
     * @param is   The stream to read from
     * @throws IOException If an I/O error occurs
     */
    @WillNotClose
    public static void read(final File file, final InputStream is) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(IOUtils.readAll(is));
        }
    }

    /**
     * Read all bytes from a file and write them to a stream.
     *
     * @param file The file to read from
     * @param os   The stream to write to
     * @throws IOException If an I/O error occurs
     */
    @WillNotClose
    public static void write(final File file, final OutputStream os) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            os.write(IOUtils.readAll(fis));
        }
    }

}
