package net.lenni0451.commons.asm.mappings.loader;

import org.jetbrains.annotations.Nullable;

import javax.annotation.WillClose;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * The mappings formats supported by the {@link MappingsLoader} implementations.
 */
public enum MappingsFormat {

    /**
     * Proguard mappings ({@code a.b.C -> d.e.F:}).
     */
    PROGUARD,
    /**
     * Retroguard mappings ({@code .class_map/.field_map/.method_map}).
     */
    RETROGUARD,
    /**
     * SRG and XSRG mappings ({@code PK:/CL:/FD:/MD:}).
     */
    SRG,
    /**
     * TSRG v1 mappings (indented member entries, optional {@code tsrg} header).
     */
    TSRG_V1,
    /**
     * TSRG v2 mappings ({@code tsrg2} header, parameters, multiple namespaces).
     */
    TSRG_V2,
    /**
     * Tiny v1 mappings ({@code v1} header).
     */
    TINY_V1,
    /**
     * Tiny v2 mappings ({@code tiny 2} header).
     */
    TINY_V2;

    /**
     * Detect the mappings format of the given file by looking at its content.
     *
     * @param file The mappings file
     * @return The detected mappings format or {@code null} if it could not be detected
     * @throws IOException If an error occurs while reading the file
     */
    public static @Nullable MappingsFormat detect(final File file) throws IOException {
        return detect(file.toPath());
    }

    /**
     * Detect the mappings format of the given path by looking at its content.
     *
     * @param path The mappings file
     * @return The detected mappings format or {@code null} if it could not be detected
     * @throws IOException If an error occurs while reading the file
     */
    public static @Nullable MappingsFormat detect(final Path path) throws IOException {
        return detect(Files.newBufferedReader(path, StandardCharsets.UTF_8));
    }

    /**
     * Detect the mappings format of the given bytes by looking at their content.
     *
     * @param bytes The raw content of the mappings file
     * @return The detected mappings format or {@code null} if it could not be detected
     */
    public static @Nullable MappingsFormat detect(final byte[] bytes) {
        try {
            return detect(new InputStreamReader(new ByteArrayInputStream(bytes), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new UncheckedIOException(e); //Cannot happen for a ByteArrayInputStream
        }
    }

    /**
     * Detect the mappings format of the given reader by looking at its content.<br>
     * The reader is expected to provide UTF-8 decoded content and will be closed by this method.
     *
     * @param reader The mappings reader
     * @return The detected mappings format or {@code null} if it could not be detected
     * @throws IOException If an error occurs while reading the reader
     */
    public static @Nullable MappingsFormat detect(@WillClose final Reader reader) throws IOException {
        try (BufferedReader br = reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader)) {
            boolean headerChecked = false;
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) continue;
                if (!headerChecked) {
                    headerChecked = true;
                    String[] headerParts = trimmed.split("\\s+");
                    if (headerParts[0].equals("tiny") && headerParts.length >= 2) { // Tiny v2: tiny <version> ...
                        return headerParts[1].equals("2") ? TINY_V2 : null;
                    }
                    if (headerParts.length >= 3) { // Headers with namespaces
                        switch (headerParts[0]) {
                            case "v1":    // Tiny v1: v1 <namespaces...>
                                return TINY_V1;
                            case "tsrg2": // TSRG v2: tsrg2 <namespaces...>
                                return TSRG_V2;
                            case "tsrg":  // TSRG v1: tsrg <left> <right>
                                return TSRG_V1;
                        }
                    }
                }
                if (trimmed.startsWith("PK:") || trimmed.startsWith("CL:") || trimmed.startsWith("FD:") || trimmed.startsWith("MD:")) {
                    return SRG;
                } else if (trimmed.startsWith(".class_map") || trimmed.startsWith(".field_map") || trimmed.startsWith(".method_map")) {
                    return RETROGUARD;
                } else if (trimmed.contains(" -> ") && trimmed.endsWith(":")) {
                    return PROGUARD;
                } else if (line.startsWith("\t")) {
                    return TSRG_V1;
                }
            }

            return null;
        }
    }

}
