package net.lenni0451.commons.asm.mappings.loader.formats;

import net.lenni0451.commons.asm.mappings.Mappings;
import net.lenni0451.commons.asm.mappings.loader.MappingsLoader;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.lenni0451.commons.asm.ASMUtils.slash;

/**
 * A loader for Proguard mappings.
 */
public class ProguardMappingsLoader extends MappingsLoader {

    private static final Pattern CLASS = Pattern.compile("^([^ ]+) ?-> ?([^ ]+):$");
    private static final Pattern FIELD = Pattern.compile("^ {4}([^ ]+) ([^ (]+) ?-> ?(.+)$");
    private static final Pattern METHOD = Pattern.compile("^ {4}(?>\\d+:)*([^ ]+) ([^ ()]+)(\\([^ ()]*\\))(?>:\\d+)* ?-> ?(.+)$");

    public ProguardMappingsLoader(final InputStream inputStream) {
        super(inputStream);
    }

    public ProguardMappingsLoader(final File file) {
        super(file);
    }

    public ProguardMappingsLoader(final Path path) {
        super(path);
    }

    @Override
    protected Mappings load(List<String> lines) {
        Mappings mappings = new Mappings();
        String currentClass = null;
        for (String line : lines) {
            if (line.startsWith("#")) continue;

            Matcher matcher = CLASS.matcher(line);
            if (matcher.find()) {
                currentClass = slash(matcher.group(1));
                String newName = slash(matcher.group(2));

                if (!currentClass.equals(newName)) mappings.addClassMapping(currentClass, newName);
                continue;
            }
            matcher = FIELD.matcher(line);
            if (matcher.find()) {
                if (currentClass == null) throw new IllegalStateException("Field line without class: " + line);
                String descriptor = this.typeToInternal(matcher.group(1));
                String name = matcher.group(2);
                String newName = matcher.group(3);

                if (!name.equals(newName)) mappings.addFieldMapping(currentClass, name, descriptor, newName);
                continue;
            }
            matcher = METHOD.matcher(line);
            if (matcher.find()) {
                if (currentClass == null) throw new IllegalStateException("Method line without class: " + line);
                String returnType = this.typeToInternal(matcher.group(1));
                String name = matcher.group(2);
                String descriptor = this.descriptorToInternal(matcher.group(3));
                String newName = matcher.group(4);

                if (!name.equals(newName)) mappings.addMethodMapping(currentClass, name, descriptor + returnType, newName);
                continue;
            }
            throw new IllegalStateException("Unknown line: " + line);
        }
        return mappings;
    }

    private String typeToInternal(String type) {
        StringBuilder arrayCount = new StringBuilder();
        while (type.endsWith("[]")) {
            arrayCount.append("[");
            type = type.substring(0, type.length() - 2);
        }

        switch (type) {
            case "void":
                return arrayCount + "V";
            case "boolean":
                return arrayCount + "Z";
            case "byte":
                return arrayCount + "B";
            case "short":
                return arrayCount + "S";
            case "char":
                return arrayCount + "C";
            case "int":
                return arrayCount + "I";
            case "long":
                return arrayCount + "J";
            case "float":
                return arrayCount + "F";
            case "double":
                return arrayCount + "D";
            default:
                return arrayCount + "L" + slash(type) + ";";
        }
    }

    private String descriptorToInternal(String descriptor) {
        descriptor = descriptor.substring(1, descriptor.length() - 1);
        if (descriptor.isEmpty()) return "()";

        String[] parts = descriptor.split(",");
        StringBuilder out = new StringBuilder();
        for (String part : parts) out.append(this.typeToInternal(part));
        return "(" + out + ")";
    }

}
