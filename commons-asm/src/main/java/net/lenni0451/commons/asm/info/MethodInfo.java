package net.lenni0451.commons.asm.info;

public interface MethodInfo {

    /**
     * @return The modifiers of the method
     */
    int getModifiers();

    /**
     * @return The name of the method
     */
    String getName();

    /**
     * @return The descriptor of the method
     */
    String getDescriptor();

}
