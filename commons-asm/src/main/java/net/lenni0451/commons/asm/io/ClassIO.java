package net.lenni0451.commons.asm.io;

import lombok.experimental.UtilityClass;
import net.lenni0451.commons.asm.info.ClassInfoProvider;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

@UtilityClass
public class ClassIO {

    /**
     * Read a class from a byte array.<br>
     * This will use the {@link ClassReader#EXPAND_FRAMES} flag.
     *
     * @param bytes The bytecode of the class
     * @return The class node
     */
    public static ClassNode fromBytes(final byte[] bytes) {
        return fromBytes(bytes, ClassReader.EXPAND_FRAMES);
    }

    /**
     * Read a class from a byte array.
     *
     * @param bytes The bytecode of the class
     * @param flags The flags to use for reading the class
     * @return The class node
     */
    public static ClassNode fromBytes(final byte[] bytes, final int flags) {
        ClassNode node = new ClassNode();
        new ClassReader(bytes).accept(node, flags);
        return node;
    }

    /**
     * Write a class to a byte array.<br>
     * The {@code classInfoProvider} is used for stack map frame calculation.
     *
     * @param node              The class node
     * @param classInfoProvider The class info provider
     * @return The bytecode of the class
     */
    public static byte[] toBytes(final ClassNode node, final ClassInfoProvider classInfoProvider) {
        return toBytes(node, ClassWriter.COMPUTE_FRAMES, classInfoProvider);
    }

    /**
     * Write a class to a byte array.<br>
     * The {@code classInfoProvider} is used for stack map frame calculation, if enabled.
     *
     * @param node              The class node
     * @param flags             The flags to use for writing the class
     * @param classInfoProvider The class info provider
     * @return The bytecode of the class
     */
    public static byte[] toBytes(final ClassNode node, final int flags, final ClassInfoProvider classInfoProvider) {
        ClassWriter writer = new InfoClassWriter(flags, classInfoProvider);
        node.accept(writer);
        return writer.toByteArray();
    }

    /**
     * Write a class to a byte array without computing the stack map frames.<br>
     * This will use the {@link ClassWriter#COMPUTE_MAXS} flag.
     *
     * @param node The class node
     * @return The bytecode of the class
     */
    public static byte[] toStacklessBytes(final ClassNode node) {
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        return writer.toByteArray();
    }

}
