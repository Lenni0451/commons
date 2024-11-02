package net.lenni0451.commons.asm.mappings;

import net.lenni0451.commons.asm.io.ClassIO;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.tree.ClassNode;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Remapper {

    public static Map<String, byte[]> remapJarEntries(final Map<String, byte[]> entries, final org.objectweb.asm.commons.Remapper remapper) {
        Map<String, byte[]> out = new HashMap<>();
        for (Map.Entry<String, byte[]> entry : entries.entrySet()) {
            if (entry.getKey().toLowerCase(Locale.ROOT).endsWith(".class")) {
                ClassNode classNode = ClassIO.fromBytes(entry.getValue());
                classNode = remap(classNode, remapper);
                out.put(classNode.name + ".class", ClassIO.toStacklessBytes(classNode));
            } else {
                out.put(entry.getKey(), entry.getValue());
            }
        }
        return out;
    }

    public static ClassNode remap(final ClassNode classNode, final org.objectweb.asm.commons.Remapper remapper) {
        ClassNode remappedNode = new ClassNode();
        ClassRemapper classRemapper = new ClassRemapper(remappedNode, remapper);
        classNode.accept(classRemapper);
        return remappedNode;
    }

}
