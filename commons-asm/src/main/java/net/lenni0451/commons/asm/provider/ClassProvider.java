package net.lenni0451.commons.asm.provider;

import net.lenni0451.commons.asm.io.ClassIO;
import org.objectweb.asm.tree.ClassNode;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Map;

/**
 * A provider for class bytecode.
 */
public interface ClassProvider extends AutoCloseable {

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
     * Close the class provider and free all resources.
     *
     * @throws Exception If an error occurred while closing the class provider
     */
    @Override
    default void close() throws Exception {
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
     * Delegate all missing classes to the given class providers.<br>
     * The class providers are checked in the order they are given.<br>
     * See {@link DelegatingClassProvider#getAllClasses()} for more information on the behavior of this method.
     *
     * @param classProviders The class providers to delegate to
     * @return A new class provider that delegates to the given provider
     * @see DelegatingClassProvider
     */
    default ClassProvider then(final ClassProvider... classProviders) {
        ClassProvider[] providers = new ClassProvider[classProviders.length + 1];
        providers[0] = this;
        System.arraycopy(classProviders, 0, providers, 1, classProviders.length);
        return new DelegatingClassProvider(providers);
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
