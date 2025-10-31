package net.lenni0451.commons.asm.mappings;

import net.lenni0451.commons.asm.info.MemberDeclaration;

import java.util.Map;

public class MappingsBridge {

    /**
     * Precalculate mappings from a source mapping to a target mapping.<br>
     * This can be used to bridge two different mappings without having to map through the intermediary every time.
     *
     * @param from The source mappings
     * @param to   The target mappings
     * @return The precalculated "bridge" mappings
     */
    public static Mappings precalculate(final Mappings from, final Mappings to) {
        Mappings precalculated = new Mappings();
        for (Map.Entry<String, String> entry : from.getPackageMappings().entrySet()) {
            precalculated.addPackageMapping(
                    entry.getKey(),
                    to.mapPackageName(entry.getValue())
            );
        }
        for (Map.Entry<String, String> entry : from.getClassMappings().entrySet()) {
            precalculated.addClassMapping(
                    entry.getKey(),
                    to.map(entry.getValue())
            );
        }
        for (Map.Entry<String, String> entry : from.getFieldMappings().entrySet()) {
            MemberDeclaration fieldDeclaration = MemberDeclaration.fromFieldMapping(entry.getKey());
            String intermediaryOwner = from.map(fieldDeclaration.getOwner());
            String intermediaryDescriptor = fieldDeclaration.getDescriptor() == null ? null : from.mapDesc(fieldDeclaration.getDescriptor());
            precalculated.addFieldMapping(
                    fieldDeclaration.getOwner(),
                    fieldDeclaration.getName(),
                    fieldDeclaration.getDescriptor(),
                    to.mapFieldName(intermediaryOwner, entry.getValue(), intermediaryDescriptor)
            );
        }
        for (Map.Entry<String, String> entry : from.getMethodMappings().entrySet()) {
            MemberDeclaration methodDeclaration = MemberDeclaration.fromMethodMapping(entry.getKey());
            String intermediaryOwner = from.map(methodDeclaration.getOwner());
            String intermediaryDescriptor = methodDeclaration.getDescriptor() == null ? null : from.mapMethodDesc(methodDeclaration.getDescriptor());
            precalculated.addMethodMapping(
                    methodDeclaration.getOwner(),
                    methodDeclaration.getName(),
                    methodDeclaration.getDescriptor(),
                    to.mapMethodName(intermediaryOwner, entry.getValue(), intermediaryDescriptor)
            );
        }
        return precalculated;
    }

}
