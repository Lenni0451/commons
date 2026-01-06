package net.lenni0451.commons.asm.mappings;

import lombok.experimental.UtilityClass;
import net.lenni0451.commons.asm.Modifiers;
import net.lenni0451.commons.asm.info.*;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;

import static net.lenni0451.commons.asm.Types.*;

@UtilityClass
public class MappingsFiller {

    /**
     * Fill all super members of all classes in the mappings.<br>
     * It will fill the super members for all classes that are mentioned in the mappings.
     *
     * @param mappings          The mappings
     * @param classInfoProvider The class info provider
     * @see #fillSuperMembers(ClassNode, Set, Mappings)
     */
    public static void fillAllSuperMembers(final Mappings mappings, final ClassInfoProvider classInfoProvider) {
        fillAllSuperMembers(mappings, classInfoProvider, getAllMentionedClasses(mappings));
    }

    /**
     * Fill all super members of all classes in the mappings.<br>
     * Exceptions during the filling process will be printed to the standard error output, but the process will continue.
     *
     * @param mappings          The mappings
     * @param classInfoProvider The class info provider
     * @param classes           The classes to fill the super members for
     * @see #fillSuperMembers(ClassNode, Set, Mappings)
     */
    public static void fillAllSuperMembers(final Mappings mappings, final ClassInfoProvider classInfoProvider, final Set<String> classes) {
        fillAllSuperMembers(mappings, classInfoProvider, classes, (clazz, t) -> {
            System.err.println("Failed to fill super members for class: " + clazz);
            t.printStackTrace(System.err);
            return true;
        });
    }

    /**
     * Fill all super members of all classes in the mappings.
     *
     * @param mappings          The mappings
     * @param classInfoProvider The class info provider
     * @param classes           The classes to fill the super members for
     * @param onError           The error predicate receiving the class name and the throwable if an error occurs.
     *                          Returning {@code true} will continue with the next class, {@code false} will rethrow the exception.
     * @see #fillSuperMembers(ClassNode, Set, Mappings)
     */
    public static void fillAllSuperMembers(final Mappings mappings, final ClassInfoProvider classInfoProvider, final Set<String> classes, final BiPredicate<String, Throwable> onError) {
        for (String clazz : classes) {
            try {
                ClassInfo classInfo = classInfoProvider.of(clazz);
                Set<ClassInfo> resolvedSuperClasses = classInfo.getRecursiveSuperClasses();
                fillSuperMembers(classInfo, resolvedSuperClasses, mappings);
            } catch (Throwable t) {
                if (!onError.test(clazz, t)) {
                    throw t;
                }
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
     * @param info         The class info
     * @param superClasses The super classes of the class
     * @param mappings     The mappings
     */
    public static void fillSuperMembers(final ClassInfo info, final Set<ClassInfo> superClasses, final Mappings mappings) {
        Set<String> mappedFields = new HashSet<>();
        Set<String> mappedMethods = new HashSet<>();
        for (ClassInfo superClass : superClasses) {
            for (FieldInfo field : superClass.getFields()) {
                if (Modifiers.has(field.getModifiers(), Opcodes.ACC_PRIVATE)) continue;
                String mappedName = mappings.mapFieldName(superClass.getName(), field.getName(), field.getDescriptor());
                if (field.getName().equals(mappedName)) continue;

                if (mappedFields.add(mappedName + field.getDescriptor())) {
                    mappings.addFieldMapping(info.getName(), field.getName(), field.getDescriptor(), mappedName, true);
                }
            }
            for (MethodInfo method : superClass.getMethods()) {
                if (Modifiers.has(method.getModifiers(), Opcodes.ACC_PRIVATE) || method.getName().startsWith("<")) continue;
                String mappedName = mappings.mapMethodName(superClass.getName(), method.getName(), method.getDescriptor());
                if (method.getName().equals(mappedName)) continue;

                if (mappedMethods.add(mappedName + method.getDescriptor())) {
                    mappings.addMethodMapping(info.getName(), method.getName(), method.getDescriptor(), mappedName, true);
                }
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
        Set<Type> types = new HashSet<>();
        Set<String> classes = new HashSet<>(mappings.classMappings.keySet());
        for (Map.Entry<String, String> entry : mappings.fieldMappings.entrySet()) {
            MemberDeclaration member = MemberDeclaration.fromFieldMapping(entry.getKey());
            classes.add(member.getOwner());
            if (member.getDescriptor() != null) {
                //Field mappings can have null descriptors if they are not specified in the mappings
                types.add(type(member.getDescriptor()));
            }
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
