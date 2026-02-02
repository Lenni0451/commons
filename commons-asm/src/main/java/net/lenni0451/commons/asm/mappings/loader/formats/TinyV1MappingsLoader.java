package net.lenni0451.commons.asm.mappings.loader.formats;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import net.lenni0451.commons.asm.mappings.Mappings;
import net.lenni0451.commons.asm.mappings.loader.MappingsLoader;

import javax.annotation.Nullable;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A loader for TinyV1 mappings.
 */
public class TinyV1MappingsLoader extends MappingsLoader {

    private final String fromNamespace;
    private final String toNamespace;
    private final List<Property> properties = new ArrayList<>();

    public TinyV1MappingsLoader(final InputStream inputStream, final String fromNamespace, final String toNamespace) {
        super(inputStream);
        this.fromNamespace = fromNamespace;
        this.toNamespace = toNamespace;
    }

    public TinyV1MappingsLoader(final File file, final String fromNamespace, final String toNamespace) {
        super(file);
        this.fromNamespace = fromNamespace;
        this.toNamespace = toNamespace;
    }

    public TinyV1MappingsLoader(final Path path, final String fromNamespace, final String toNamespace) {
        super(path);
        this.fromNamespace = fromNamespace;
        this.toNamespace = toNamespace;
    }

    /**
     * Get the properties defined in the tiny mappings.<br>
     * Properties without a value will have {@code null} as value.<br>
     * <b>This loader will be initialized if this method is called. See {@link #getMappings()} for more information.</b>
     *
     * @return The properties
     */
    public List<Property> getProperties() {
        this.getMappings(); //Ensure mappings are loaded
        return this.properties;
    }

    @Override
    protected Mappings load(List<String> lines) {
        Mappings mappings = new Mappings();
        Mappings baseToSource = new Mappings();
        List<UnmappedMember> unmappedMembers = new ArrayList<>();

        int fromIndex = -1;
        int toIndex = -1;
        for (String line : lines) {
            String[] parts = line.trim().split("\t+");
            if (fromIndex == -1) {
                if (!parts[0].equals("v1")) throw new IllegalStateException("Invalid tiny header (expected 'v1', got '" + parts[0] + "')");
                if (parts.length < 3) throw new IllegalStateException("Invalid tiny header (missing namespaces)");

                List<String> namespaces = Arrays.asList(Arrays.copyOfRange(parts, 1, parts.length));
                fromIndex = namespaces.indexOf(this.fromNamespace);
                toIndex = namespaces.indexOf(this.toNamespace);
                if (fromIndex == -1) throw new IllegalStateException("Namespace '" + this.fromNamespace + "' not found in tiny mappings (available: " + namespaces + ")");
                if (toIndex == -1) throw new IllegalStateException("Namespace '" + this.toNamespace + "' not found in tiny mappings (available: " + namespaces + ")");
            } else if (line.startsWith("CLASS\t")) {
                String baseName = parts[1];
                String fromName = parts[1 + fromIndex];
                String toName = parts[1 + toIndex];

                baseToSource.addClassMapping(baseName, fromName);
                mappings.addClassMapping(fromName, toName);
            } else if (line.startsWith("FIELD\t")) {
                String owner = parts[1];
                String descriptor = parts[2];
                String fromName = parts[3 + fromIndex];
                String toName = parts[3 + toIndex];

                unmappedMembers.add(new UnmappedMember(false, owner, fromName, descriptor, toName));
            } else if (line.startsWith("METHOD\t")) {
                String owner = parts[1];
                String descriptor = parts[2];
                String fromName = parts[3 + fromIndex];
                String toName = parts[3 + toIndex];

                unmappedMembers.add(new UnmappedMember(true, owner, fromName, descriptor, toName));
            } else if (line.startsWith("# ")) {
                String[] propertyParts = line.substring(2).split(" ");
                if (propertyParts.length == 1) {
                    this.properties.add(new Property(propertyParts[0]));
                } else if (propertyParts.length >= 2) {
                    this.properties.add(new Property(propertyParts[0], propertyParts[1]));
                } else {
                    throw new IllegalStateException("Property with too many parts: " + line);
                }
            } else {
                throw new IllegalStateException("Unknown line: " + line);
            }
        }
        this.finalizeMemberMappings(mappings, baseToSource, unmappedMembers);
        return mappings;
    }

    private void finalizeMemberMappings(final Mappings mappings, final Mappings baseToSource, final List<UnmappedMember> unmappedMembers) {
        for (UnmappedMember member : unmappedMembers) {
            if (member.method) {
                mappings.addMethodMapping(baseToSource.map(member.owner), member.name, baseToSource.mapMethodDesc(member.descriptor), member.toName);
            } else {
                mappings.addFieldMapping(baseToSource.map(member.owner), member.name, baseToSource.mapDesc(member.descriptor), member.toName);
            }
        }
    }


    @AllArgsConstructor
    private static class UnmappedMember {
        private final boolean method;
        private final String owner;
        private final String name;
        private final String descriptor;
        private final String toName;
    }

    @Getter
    @ToString
    @EqualsAndHashCode
    @AllArgsConstructor
    public static class Property {
        private final String name;
        @Nullable
        private final String value;

        public Property(final String name) {
            this(name, null);
        }
    }

}
