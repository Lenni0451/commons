package net.lenni0451.commons.asm.mappings;

import lombok.experimental.UtilityClass;
import net.lenni0451.commons.asm.Modifiers;
import net.lenni0451.commons.asm.info.ClassInfo;
import net.lenni0451.commons.asm.info.ClassInfoProvider;
import net.lenni0451.commons.asm.info.MemberDeclaration;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.*;

import static net.lenni0451.commons.asm.Types.*;

@UtilityClass
public class MappingsFiller {

    /**
     * Fill all super members of all classes in the mappings.
     *
     * @param mappings          The mappings
     * @param classInfoProvider The class info provider
     * @see #fillSuperMembers(ClassNode, Set, Mappings)
     */
    public static void fillAllSuperMembers(final Mappings mappings, final ClassInfoProvider classInfoProvider) {
        for (String clazz : getAllMentionedClasses(mappings)) {
            try {
                ClassInfo classInfo = classInfoProvider.of(clazz);
                Set<ClassInfo> resolvedSuperClasses = classInfo.recursiveResolveSuperClasses(false);
                Set<ClassNode> superClasses = new LinkedHashSet<>();
                for (ClassInfo superClass : resolvedSuperClasses) superClasses.add(superClass.getClassNode());
                fillSuperMembers(classInfo.getClassNode(), superClasses, mappings);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    /**
     * Copy the field and method mappings of the super classes to the given class.<br>
     * This is required for the correct remapping of fields and methods.<br>
     * <br>
     * When invoking methods of super classes, the method owner of the instruction is the class where the method is invoked on and not the owner of the method itself.<br>
     * This means during the remapping process the method owner is wrong and the method will not be found in the mappings.<br>
     * To fix this, the method mappings of the super classes are copied to the class where the method could be invoked on.
     *
     * @param node         The class node
     * @param superClasses The super classes of the class
     * @param mappings     The mappings
     */
    public static void fillSuperMembers(final ClassNode node, final Set<ClassNode> superClasses, final Mappings mappings) {
        Set<String> mappedFields = new HashSet<>();
        Set<String> mappedMethods = new HashSet<>();
        for (ClassNode superClass : superClasses) {
            for (FieldNode field : superClass.fields) {
                if (Modifiers.has(field.access, Opcodes.ACC_PRIVATE)) continue;
                String mappedName = mappings.mapFieldName(superClass.name, field.name, field.desc);
                if (field.name.equals(mappedName)) continue;

                if (mappedFields.add(mappedName + field.desc)) {
                    mappings.addFieldMapping(node.name, field.name, field.desc, mappedName, true);
                }
            }
            for (MethodNode method : superClass.methods) {
                if (Modifiers.has(method.access, Opcodes.ACC_PRIVATE) || method.name.startsWith("<")) continue;
                String mappedName = mappings.mapMethodName(superClass.name, method.name, method.desc);
                if (method.name.equals(mappedName)) continue;

                if (mappedMethods.add(mappedName + method.desc)) {
                    mappings.addMethodMapping(node.name, method.name, method.desc, mappedName, true);
                }
            }
        }
    }

    private static Set<String> getAllMentionedClasses(final Mappings mappings) {
        Set<String> classes = new HashSet<>();
        Set<Type> types = new HashSet<>();
        classes.addAll(mappings.classMappings.keySet());
        for (Map.Entry<String, String> entry : mappings.fieldMappings.entrySet()) {
            MemberDeclaration member = MemberDeclaration.fromFieldMapping(entry.getKey());
            classes.add(member.getOwner());
            types.add(type(member.getDescriptor()));
        }
        for (Map.Entry<String, String> entry : mappings.methodMappings.entrySet()) {
            MemberDeclaration member = MemberDeclaration.fromMethodMapping(entry.getKey());
            classes.add(member.getOwner());
            Collections.addAll(types, argumentTypes(member.getDescriptor()));
            types.add(returnType(member.getDescriptor()));
        }
        for (Type type : types) {
            if (type.getSort() == Type.ARRAY) type = type.getElementType();
            if (type.getSort() == Type.OBJECT) classes.add(type.getInternalName());
        }
        return classes;
    }

}
