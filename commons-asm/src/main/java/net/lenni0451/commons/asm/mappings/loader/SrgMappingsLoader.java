package net.lenni0451.commons.asm.mappings.loader;

import net.lenni0451.commons.asm.mappings.Mappings;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

public class SrgMappingsLoader extends MappingsLoader {

    public SrgMappingsLoader(final InputStream inputStream) {
        super(inputStream);
    }

    public SrgMappingsLoader(final File file) {
        super(file);
    }

    public SrgMappingsLoader(final Path path) {
        super(path);
    }

    @Override
    protected Mappings load(List<String> lines) {
        Mappings mappings = new Mappings();
        for (String line : lines) {
            String[] parts = line.trim().split(" ");
            if (parts[0].equals("PK:")) {
                String name = parts[1];
                String newName = parts[2];
                mappings.addPackageMapping(name, newName);
            } else if (parts[0].equals("CL:")) {
                String name = parts[1];
                String newName = parts[2];
                mappings.addClassMapping(name, newName);
            } else if (parts[0].equals("FD:")) {
                if (parts.length == 3) { //SRG
                    String owner = parts[1].substring(0, parts[1].lastIndexOf('/'));
                    String name = parts[1].substring(parts[1].lastIndexOf('/') + 1);
                    String newName = parts[2].substring(parts[2].lastIndexOf('/') + 1);
                    mappings.addFieldMapping(owner, name, null, newName);
                } else if (parts.length == 5) { //XSRG
                    String owner = parts[1].substring(0, parts[1].lastIndexOf('/'));
                    String name = parts[1].substring(parts[1].lastIndexOf('/') + 1);
                    String desc = parts[2];
                    String newName = parts[3].substring(parts[3].lastIndexOf('/') + 1);
                    mappings.addFieldMapping(owner, name, desc, newName);
                } else {
                    throw new IllegalStateException("Invalid field line: " + line);
                }
            } else if (parts[0].equals("MD:")) {
                if (parts.length == 4 /*SRG*/ || parts.length == 5 /*XSRG*/) {
                    String owner = parts[1].substring(0, parts[1].lastIndexOf('/'));
                    String name = parts[1].substring(parts[1].lastIndexOf('/') + 1);
                    String desc = parts[2];
                    String newName = parts[3].substring(parts[3].lastIndexOf('/') + 1);
                    mappings.addMethodMapping(owner, name, desc, newName);
                } else {
                    throw new IllegalStateException("Invalid method line: " + line);
                }
            } else {
                throw new IllegalStateException("Unknown line: " + line);
            }
        }
        return mappings;
    }

}
