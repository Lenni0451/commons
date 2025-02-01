package net.lenni0451.commons.asm.mappings;

import lombok.experimental.UtilityClass;
import net.lenni0451.commons.asm.info.MemberDeclaration;

import java.util.Map;

@UtilityClass
class Reverser {

    public static Mappings init(final Mappings mappings) {
        Mappings reversed = mappings.emptyCopy();
        recalculatePackages(mappings, reversed);
        return reversed;
    }

    public static void recalculatePackages(final Mappings from, final Mappings to) {
        to.packageMappings.clear();
        for (Map.Entry<String, String> entry : from.packageMappings.entrySet()) {
            to.packageMappings.put(entry.getValue(), entry.getKey());
        }
        recalculateClasses(from, to);
    }

    public static void recalculateClasses(final Mappings from, final Mappings to) {
        to.classMappings.clear();
        for (Map.Entry<String, String> entry : from.classMappings.entrySet()) {
            to.classMappings.put(from.map(entry.getKey()), entry.getKey());
        }
        recalculateFields(from, to);
        recalculateMethods(from, to);
    }

    public static void recalculateFields(final Mappings from, final Mappings to) {
        to.fieldMappings.clear();
        for (Map.Entry<String, String> entry : from.fieldMappings.entrySet()) {
            MemberDeclaration member = MemberDeclaration.fromFieldMapping(entry.getKey());
            to.fieldMappings.put(from.map(member.getOwner()) + "." + entry.getValue() + (member.getDescriptor() == null ? "" : (":" + from.mapDesc(member.getDescriptor()))), member.getName());
        }
    }

    public static void recalculateMethods(final Mappings from, final Mappings to) {
        to.methodMappings.clear();
        for (Map.Entry<String, String> entry : from.methodMappings.entrySet()) {
            MemberDeclaration member = MemberDeclaration.fromMethodMapping(entry.getKey());
            to.methodMappings.put(from.map(member.getOwner()) + "." + entry.getValue() + from.mapMethodDesc(member.getDescriptor()), member.getName());
        }
    }

}
