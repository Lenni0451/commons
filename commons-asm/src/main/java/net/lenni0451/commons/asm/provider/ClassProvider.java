package net.lenni0451.commons.asm.provider;

import net.lenni0451.commons.asm.io.ClassIO;
import org.objectweb.asm.tree.ClassNode;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Map;

/**
 * A provider for class bytecode.
 */
public interface ClassProvider {

    /**
     * Get the bytecode of a class by its name.<br>
     * The name should be in the format of {@code "package/Name"} but it is advised that every implementation should support both formats.
     *
     * @param name The name of the class
     * @return The bytecode of the class
     * @throws ClassNotFoundException If the class could not be found
     */
    @Nonnull
    byte[] getClass(final String name) throws ClassNotFoundException;

    /**
     * Get a map of all classes that are available in this provider.<br>
     * Not every implementation has to support this and can throw an  {@link UnsupportedOperationException} if it is not supported.<br>
     * The key format is {@code "package/Name"}.
     *
     * @return A map of all classes
     * @throws UnsupportedOperationException If this operation is not supported
     */
    @Nonnull
    default Map<String, ClassSupplier> getAllClasses() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /**
     * Get the bytecode of a class by its name and convert it to a {@link ClassNode}.
     *
     * @param name The name of the class
     * @return The class node of the class
     * @throws ClassNotFoundException If the class could not be found
     * @see #getClass(String)
     */
    default ClassNode getClassNode(final String name) throws ClassNotFoundException {
        return ClassIO.fromBytes(this.getClass(name));
    }

    /**
     * Delegate all missing classes to the given class provider.<br>
     * If this provider does not have a class it will ask the given provider for it.<br>
     * This also delegates the {@link #getAllClasses()} method and mixes the results/ignores them if unsupported.
     *
     * @param classProvider The class provider to delegate to
     * @return A new class provider that delegates to the given provider
     * @see DelegatingClassProvider
     */
    default ClassProvider then(final ClassProvider classProvider) {
        return new DelegatingClassProvider(this, classProvider);
    }


    @FunctionalInterface
    interface ClassSupplier {
        /**
         * Get the bytecode of the class.
         *
         * @return The bytecode of the class
         * @throws IOException If the bytecode could not be read
         */
        byte[] get() throws IOException;
    }

}
