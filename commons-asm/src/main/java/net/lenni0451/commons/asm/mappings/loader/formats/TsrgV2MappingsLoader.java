package net.lenni0451.commons.asm.mappings.loader.formats;

import net.lenni0451.commons.asm.mappings.Mappings;
import net.lenni0451.commons.asm.mappings.loader.MappingsLoader;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
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
        int fromIndex = -1;
        int toIndex = -1;
        int nameCount = 0;
        String currentClass = null;
        for (String line : lines) {
            String[] parts = line.trim().split("\\s+");
            if (fromIndex == -1) { //Header
                if (parts.length < 3 || !parts[0].equals("tsrg2")) throw new IllegalStateException("Invalid tsrg2 header (expected 'tsrg2 <namespaces...>', got '" + line + "')");
                List<String> namespaces = Arrays.asList(Arrays.copyOfRange(parts, 1, parts.length));
                fromIndex = namespaces.indexOf(this.fromNamespace);
                toIndex = namespaces.indexOf(this.toNamespace);
                if (fromIndex == -1) throw new IllegalStateException("Namespace '" + this.fromNamespace + "' not found in tsrg2 mappings (available: " + namespaces + ")");
                if (toIndex == -1) throw new IllegalStateException("Namespace '" + this.toNamespace + "' not found in tsrg2 mappings (available: " + namespaces + ")");
                nameCount = namespaces.size();
            } else if (!line.startsWith("\t")) { //Class or package mapping
                if (parts.length != nameCount) throw new IllegalStateException("Invalid class mapping: " + line);
                String name = parts[fromIndex];
                String newName = parts[toIndex];
                if (name.endsWith("/")) { //Package
                    mappings.addPackageMapping(name, newName);
                    currentClass = null;
                } else { //Class
                    mappings.addClassMapping(name, newName);
                    currentClass = name;
                }
            } else if (line.startsWith("\t\t")) { //Parameter name or static marker
                continue; //Not part of the Mappings model
            } else { //Field or method mapping
                if (currentClass == null) throw new IllegalStateException("Member mapping without class: " + line);
                if (parts.length == nameCount) { //Field without descriptor
                    mappings.addFieldMapping(currentClass, parts[fromIndex], null, parts[toIndex]);
                } else if (parts.length == nameCount + 1) {
                    String descriptor = parts[1];
                    String name = parts[fromIndex == 0 ? 0 : fromIndex + 1];
                    String newName = parts[toIndex == 0 ? 0 : toIndex + 1];
                    if (descriptor.startsWith("(")) { //Method
                        mappings.addMethodMapping(currentClass, name, descriptor, newName);
                    } else { //Field with descriptor
                        mappings.addFieldMapping(currentClass, name, descriptor, newName);
                    }
                } else {
                    throw new IllegalStateException("Invalid member mapping: " + line);
                }
            }
        }
        if (fromIndex == -1) throw new IllegalStateException("Missing tsrg2 header");
        return mappings;
    }

}
