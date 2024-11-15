package net.lenni0451.commons.asm.mappings;

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

public class MappingsFiller {

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