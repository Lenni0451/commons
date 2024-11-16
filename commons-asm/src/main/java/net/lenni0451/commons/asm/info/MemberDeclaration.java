package net.lenni0451.commons.asm.info;

/**
 * Represents a member declaration of a class.
 */
public class MemberDeclaration {

    /**
     * Parse a field mapping to a {@link MemberDeclaration}.<br>
     * Format: {@code owner.name[:descriptor]}
     *
     * @param mapping The mapping
     * @return The member declaration
     */
    public static MemberDeclaration fromFieldMapping(final String mapping) {
        String owner = mapping.substring(0, mapping.indexOf('.'));
        String name = mapping.substring(mapping.indexOf('.') + 1);
        String descriptor = null;
        if (name.contains(":")) {
            descriptor = name.substring(name.indexOf(':') + 1);
            name = name.substring(0, name.indexOf(':'));
        }
        return new MemberDeclaration(owner, name, descriptor);
    }

    /**
     * Parse a method mapping to a {@link MemberDeclaration}.<br>
     * Format: {@code owner.name(descriptor)}
     *
     * @param mapping The mapping
     * @return The member declaration
     */
    public static MemberDeclaration fromMethodMapping(final String mapping) {
        String owner = mapping.substring(0, mapping.indexOf('.'));
        String name = mapping.substring(mapping.indexOf('.') + 1, mapping.indexOf('('));
        String descriptor = mapping.substring(mapping.indexOf('('));
        return new MemberDeclaration(owner, name, descriptor);
    }


    private final String owner;
    private final String name;
    private final String descriptor;

    public MemberDeclaration(final String owner, final String name, final String descriptor) {
        this.owner = owner;
        this.name = name;
        this.descriptor = descriptor;
    }

    /**
     * @return The owner of the member
     */
    public String getOwner() {
        return this.owner;
    }

    /**
     * @return The name of the member
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return The descriptor of the member
     */
    public String getDescriptor() {
        return this.descriptor;
    }

    /**
     * @return If this member is a method
     */
    public boolean isMethod() {
        return this.descriptor != null && this.descriptor.startsWith("(");
    }

    /**
     * @return If this member is a field
     */
    public boolean isField() {
        return this.descriptor == null || !this.descriptor.startsWith("(");
    }

}
