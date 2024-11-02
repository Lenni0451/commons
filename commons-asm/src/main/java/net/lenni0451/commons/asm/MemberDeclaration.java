package net.lenni0451.commons.asm;

public class MemberDeclaration {

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

    public String getOwner() {
        return this.owner;
    }

    public String getName() {
        return this.name;
    }

    public String getDescriptor() {
        return this.descriptor;
    }

}
