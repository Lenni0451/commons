package net.lenni0451.commons.asm.mappings.loader.formats;

import lombok.AllArgsConstructor;
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
 * A loader for TSRG v2 mappings.<br>
 * Parameter names and the static marker are not part of the {@link Mappings} model and are ignored.
 */
public class TsrgV2MappingsLoader extends MappingsLoader {

    private final String fromNamespace;
    private final String toNamespace;

    public TsrgV2MappingsLoader(final InputStream inputStream, final String fromNamespace, final String toNamespace) {
        super(inputStream);
        this.fromNamespace = fromNamespace;
        this.toNamespace = toNamespace;
    }

    public TsrgV2MappingsLoader(final File file, final String fromNamespace, final String toNamespace) {
        super(file);
        this.fromNamespace = fromNamespace;
        this.toNamespace = toNamespace;
    }

    public TsrgV2MappingsLoader(final Path path, final String fromNamespace, final String toNamespace) {
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
        int nameCount = 0;
        String currentClass = null;
        for (String line : lines) {
            String[] parts = line.trim().split("\\s+");
            if (fromIndex == -1) { // Header
                if (parts.length < 3 || !parts[0].equals("tsrg2")) throw new IllegalStateException("Invalid tsrg2 header (expected 'tsrg2 <namespaces...>', got '" + line + "')");
                List<String> namespaces = Arrays.asList(Arrays.copyOfRange(parts, 1, parts.length));
                fromIndex = namespaces.indexOf(this.fromNamespace);
                toIndex = namespaces.indexOf(this.toNamespace);
                if (fromIndex == -1) throw new IllegalStateException("Namespace '" + this.fromNamespace + "' not found in tsrg2 mappings (available: " + namespaces + ")");
                if (toIndex == -1) throw new IllegalStateException("Namespace '" + this.toNamespace + "' not found in tsrg2 mappings (available: " + namespaces + ")");
                nameCount = namespaces.size();
            } else if (!line.startsWith("\t")) { // Class or package mapping
                if (parts.length != nameCount) throw new IllegalStateException("Invalid class mapping: " + line);
                String name = parts[fromIndex];
                String newName = parts[toIndex];
                if (name.endsWith("/")) { // Package
                    mappings.addPackageMapping(name, newName);
                    baseToSource.addPackageMapping(parts[0], name);
                    currentClass = null;
                } else { //Class
                    mappings.addClassMapping(name, newName);
                    baseToSource.addClassMapping(parts[0], name);
                    currentClass = name;
                }
            } else if (line.startsWith("\t\t")) { // Parameter name or static marker
                continue; // Not part of the Mappings model
            } else { //Field or method mapping
                if (currentClass == null) throw new IllegalStateException("Member mapping without class: " + line);
                if (parts.length == nameCount) { //Field without descriptor
                    unmappedMembers.add(new UnmappedMember(false, currentClass, parts[fromIndex], null, parts[toIndex]));
                } else if (parts.length == nameCount + 1) {
                    String descriptor = parts[1];
                    String name = parts[fromIndex == 0 ? 0 : fromIndex + 1];
                    String newName = parts[toIndex == 0 ? 0 : toIndex + 1];
                    unmappedMembers.add(new UnmappedMember(descriptor.startsWith("("), currentClass, name, descriptor, newName));
                } else {
                    throw new IllegalStateException("Invalid member mapping: " + line);
                }
            }
        }
        if (fromIndex == -1) throw new IllegalStateException("Missing tsrg2 header");
        this.finalizeMemberMappings(mappings, baseToSource, unmappedMembers);
        return mappings;
    }

    private void finalizeMemberMappings(final Mappings mappings, final Mappings baseToSource, final List<UnmappedMember> unmappedMembers) {
        for (UnmappedMember member : unmappedMembers) {
            if (member.method) {
                mappings.addMethodMapping(member.owner, member.name, baseToSource.mapMethodDesc(member.descriptor), member.toName);
            } else {
                mappings.addFieldMapping(member.owner, member.name, member.descriptor == null ? null : baseToSource.mapDesc(member.descriptor), member.toName);
            }
        }
    }


    @AllArgsConstructor
    private static class UnmappedMember {
        private final boolean method;
        private final String owner;
        private final String name;
        @Nullable
        private final String descriptor;
        private final String toName;
    }

}
