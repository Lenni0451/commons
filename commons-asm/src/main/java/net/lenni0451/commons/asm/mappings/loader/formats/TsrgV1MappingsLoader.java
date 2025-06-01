package net.lenni0451.commons.asm.mappings.loader.formats;

import net.lenni0451.commons.asm.mappings.Mappings;
import net.lenni0451.commons.asm.mappings.loader.MappingsLoader;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

public class TsrgV1MappingsLoader extends MappingsLoader {

    public TsrgV1MappingsLoader(final InputStream inputStream) {
        super(inputStream);
    }

    public TsrgV1MappingsLoader(final File file) {
        super(file);
    }

    public TsrgV1MappingsLoader(final Path path) {
        super(path);
    }

    @Override
    protected Mappings load(List<String> lines) throws Throwable {
        Mappings mappings = new Mappings();
        String currentClass = null;
        for (String line : lines) {
            String[] parts = line.trim().split(" ");
            if (!line.startsWith("\t")) {
                currentClass = parts[0];
                String newName = parts[1];
                mappings.addClassMapping(currentClass, newName);
            } else {
                if (currentClass == null) throw new IllegalStateException("Field or method mapping without class: " + line);
                if (parts.length == 2) { //Field mapping
                    String name = parts[0];
                    String newName = parts[1];
                    mappings.addFieldMapping(currentClass, name, null, newName);
                } else if (parts.length == 3) { //Method mappings
                    String name = parts[0];
                    String desc = parts[1];
                    String newName = parts[2];
                    mappings.addMethodMapping(currentClass, name, desc, newName);
                } else {
                    throw new IllegalStateException("Invalid line: " + line);
                }
            }
        }
        return mappings;
    }

}
