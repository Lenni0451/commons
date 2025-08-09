package net.lenni0451.commons.io;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.WillNotClose;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
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
     * List all files in a directory recursively.<br>
     * If the file is not a directory an empty list will be returned.<br>
     * Directories will be excluded from the list.
     *
     * @param file The directory to list
     * @return A list of all files in the directory
     * @throws IOException If an I/O error occurs
     */
    @SneakyThrows
    public static List<File> listFiles(final File file) {
        if (!file.isDirectory()) return Collections.emptyList();
        try (Stream<Path> s = Files.walk(file.toPath(), FileVisitOption.FOLLOW_LINKS)) {
            return s.filter(Files::isRegularFile).map(Path::toFile).collect(Collectors.toList());
        }
    }

    /**
     * List all files in a directory recursively.<br>
     * If the file is not a directory an empty list will be returned.<br>
     * Directories will be excluded from the list.
     *
     * @param file   The directory to list
     * @param filter The filter to use
     * @return A list of all files in the directory
     * @throws IOException If an I/O error occurs
     */
    @SneakyThrows
    public static List<File> listFiles(final File file, final Predicate<File> filter) {
        if (!file.isDirectory()) return Collections.emptyList();
        try (Stream<Path> s = Files.walk(file.toPath(), FileVisitOption.FOLLOW_LINKS)) {
            return s.filter(Files::isRegularFile).map(Path::toFile).filter(filter).collect(Collectors.toList());
        }
    }

    /**
     * Recursively delete a file or directory using the Path API.<br>
     * If the file is a directory all files and directories in it will be deleted as well.
     *
     * @param file The file or directory to delete
     * @throws IOException If an I/O error occurs
     */
    @SneakyThrows
    public static void deletePathTree(final File file) {
        Files.walkFileTree(file.toPath(), new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                //Delete the file
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) {
                return FileVisitResult.TERMINATE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * Recursively delete a file or directory.<br>
     * If the file is a directory all files and directories in it will be deleted as well.
     *
     * @param file The file or directory to delete
     * @throws IOException If an I/O error occurs
     */
    @SneakyThrows
    public static void recursiveDelete(final File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) recursiveDelete(f);
            }
        }
        file.delete();
    }

    /**
     * Read all bytes from a stream and write them to a file.
     *
     * @param file The file to write to
     * @param is   The stream to read from
     * @throws IOException If an I/O error occurs
     */
    @Deprecated
    @WillNotClose
    @ApiStatus.ScheduledForRemoval //09.02.2026
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
    @Deprecated
    @WillNotClose
    @ApiStatus.ScheduledForRemoval //09.02.2026
    public static void write(final File file, final OutputStream os) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            os.write(IOUtils.readAll(fis));
        }
    }

}
