package net.lenni0451.commons.asm.mappings.loader.formats;

import lombok.AllArgsConstructor;
import net.lenni0451.commons.asm.mappings.Mappings;
import net.lenni0451.commons.asm.mappings.loader.MappingsLoader;

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

}
