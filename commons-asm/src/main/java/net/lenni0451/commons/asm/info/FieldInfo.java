package net.lenni0451.commons.asm.info;

public interface FieldInfo {

    /**
     * @return The modifiers of the field
     */
    int getModifiers();

    /**
     * @return The name of the field
     */
    String getName();

    /**
     * @return The descriptor of the field
     */
    String getDescriptor();

}
