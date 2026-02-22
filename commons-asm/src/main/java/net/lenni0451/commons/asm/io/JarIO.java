package net.lenni0451.commons.asm.io;

import lombok.experimental.UtilityClass;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

@UtilityClass
public class JarIO {

    /**
     * Read a jar file and return a map of the entries.<br>
     * Directories will be ignored.
     *
     * @param file The jar file to read
     * @return The entries
     * @throws IOException If an I/O error occurs
     */
    public static Map<String, byte[]> read(final File file) throws IOException {
        Map<String, byte[]> entries = new HashMap<>();
        try (ZipFile zipFile = new ZipFile(file)) {
            Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
            while (enumeration.hasMoreElements()) {
                ZipEntry entry = enumeration.nextElement();
                if (entry.isDirectory()) continue;
                try (InputStream is = zipFile.getInputStream(entry)) {
                    entries.put(entry.getName(), read(is));
                }
            }
        }
        return entries;
    }

    private static byte[] read(final InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int read;
        while ((read = is.read(buffer)) >= 0) {
            baos.write(buffer, 0, read);
        }
        return baos.toByteArray();
    }

    /**
     * Write a jar file from a map of entries.<br>
     * The map can not contain directories.
     *
     * @param file    The jar file to write
     * @param entries The entries to write
     * @throws IOException If an I/O error occurs
     */
    public static void write(final File file, final Map<String, byte[]> entries) throws IOException {
        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(file.toPath()))) {
            for (Map.Entry<String, byte[]> entry : entries.entrySet()) {
                zos.putNextEntry(new ZipEntry(entry.getKey()));
                zos.write(entry.getValue());
                zos.closeEntry();
            }
        }
    }

}
