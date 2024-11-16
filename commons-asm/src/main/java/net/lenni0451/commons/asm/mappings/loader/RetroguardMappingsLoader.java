package net.lenni0451.commons.asm.mappings.loader;

import net.lenni0451.commons.asm.mappings.Mappings;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

/**
 * A loader for Retroguard mappings.<br>
 * Not all Retroguard operations are supported, only the most commonly used ones.
 */
public class RetroguardMappingsLoader extends MappingsLoader {

    public RetroguardMappingsLoader(final InputStream inputStream) {
        super(inputStream);
    }

    public RetroguardMappingsLoader(final File file) {
        super(file);
    }

    public RetroguardMappingsLoader(final Path path) {
        super(path);
    }

    @Override
    protected Mappings load(List<String> lines) {
        Mappings mappings = new Mappings();
        for (String line : lines) {
            String[] parts = line.trim().split(" ");
            if (parts[0].equals(".class_map")) {
                String name = parts[1];
                String newName = parts[2];
                mappings.addClassMapping(name, newName);
            } else if (parts[0].equals(".field_map")) {
                String owner = parts[1].substring(0, parts[1].lastIndexOf('/'));
                String name = parts[1].substring(parts[1].lastIndexOf('/') + 1);
                String newName = parts[2];
                mappings.addFieldMapping(owner, name, null, newName);
            } else if (parts[0].equals(".method_map")) {
                String owner = parts[1].substring(0, parts[1].lastIndexOf('/'));
                String name = parts[1].substring(parts[1].lastIndexOf('/') + 1);
                String desc = parts[2];
                String newName = parts[3];
                mappings.addMethodMapping(owner, name, desc, newName);
            }
            //No else block because Retroguard has a lot of other options which are not relevant for remapping
        }
        return mappings;
    }

}
