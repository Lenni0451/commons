package net.lenni0451.commons.asm.mappings;

import java.util.Map;

class Reverser {

    public static void init(final Mappings mappings) {
        Mappings reversed = mappings.emptyCopy();
        recalculatePackages(mappings, reversed);
        mappings.reverse = reversed;
        reversed.reverse = mappings;
    }

    public static void recalculatePackages(final Mappings from, final Mappings to) {
        to.packageMappings.clear();
        for (Map.Entry<String, String> entry : from.packageMappings.entrySet()) {
            to.packageMappings.put(entry.getValue(), entry.getKey());
        }
        recalculateClasses(from, to);
    }

    public static void recalculateClasses(final Mappings from, final Mappings to) {
        to.classMappings.clear();
        for (Map.Entry<String, String> entry : from.classMappings.entrySet()) {
            to.classMappings.put(from.map(entry.getKey()), entry.getKey());
        }
        recalculateFields(from, to);
        recalculateMethods(from, to);
    }

    public static void recalculateFields(final Mappings from, final Mappings to) {
        to.fieldMappings.clear();
        for (Map.Entry<String, String> entry : from.fieldMappings.entrySet()) {
            String owner = entry.getKey().substring(0, entry.getKey().indexOf('.'));
            String name = entry.getKey().substring(entry.getKey().indexOf('.') + 1);
            String descriptor = null;
            if (name.contains(":")) {
                descriptor = name.substring(name.indexOf(':') + 1);
                name = name.substring(0, name.indexOf(':'));
            }
            to.fieldMappings.put(from.map(owner) + "." + entry.getValue() + (descriptor == null ? "" : (":" + from.mapDesc(descriptor))), name);
        }
    }

    public static void recalculateMethods(final Mappings from, final Mappings to) {
        to.methodMappings.clear();
        for (Map.Entry<String, String> entry : from.methodMappings.entrySet()) {
            String owner = entry.getKey().substring(0, entry.getKey().indexOf('.'));
            String name = entry.getKey().substring(entry.getKey().indexOf('.') + 1, entry.getKey().indexOf('('));
            String descriptor = entry.getKey().substring(entry.getKey().indexOf('('));
            to.methodMappings.put(from.map(owner) + "." + entry.getValue() + from.mapMethodDesc(descriptor), name);
        }
    }

}
