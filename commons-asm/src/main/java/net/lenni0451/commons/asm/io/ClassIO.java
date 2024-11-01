package net.lenni0451.commons.asm.io;

import net.lenni0451.commons.asm.info.ClassInfoProvider;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

public class ClassIO {

    public static ClassNode fromBytes(final byte[] bytes) {
        return fromBytes(bytes, ClassReader.EXPAND_FRAMES);
    }

    public static ClassNode fromBytes(final byte[] bytes, final int flags) {
        ClassNode node = new ClassNode();
        new ClassReader(bytes).accept(node, flags);
        return node;
    }

    public static byte[] toBytes(final ClassNode node, final ClassInfoProvider classInfoProvider) {
        return toBytes(node, ClassWriter.COMPUTE_FRAMES, classInfoProvider);
    }

    public static byte[] toBytes(final ClassNode node, final int flags, final ClassInfoProvider classInfoProvider) {
        ClassWriter writer = new InfoClassWriter(flags, classInfoProvider);
        node.accept(writer);
        return writer.toByteArray();
    }

    public static byte[] toStacklessBytes(final ClassNode node) {
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        return writer.toByteArray();
    }

}
