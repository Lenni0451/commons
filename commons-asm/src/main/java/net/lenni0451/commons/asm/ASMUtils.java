package net.lenni0451.commons.asm;

import lombok.experimental.UtilityClass;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import javax.annotation.Nullable;
import javax.lang.model.SourceVersion;
import java.util.Locale;
import java.util.function.Predicate;

import static net.lenni0451.commons.asm.Types.*;

@UtilityClass
public class ASMUtils {

    /**
     * Replace all slashes with dots.
     *
     * @param s The string to replace
     * @return The replaced string
     */
    public static String dot(final String s) {
        return s.replace('/', '.');
    }

    /**
     * Replace all dots with slashes.
     *
     * @param s The string to replace
     * @return The replaced string
     */
    public static String slash(final String s) {
        return s.replace('.', '/');
    }

    /**
     * Get a field from a class node.<br>
     * The field is searched by its name and descriptor.
     *
     * @param classNode The class node to search in
     * @param name      The name of the field
     * @param desc      The descriptor of the field (null for any descriptor)
     * @return The field node or null if no field was found
     */
    public static FieldNode getField(final ClassNode classNode, final String name, @Nullable final String desc) {
        for (FieldNode fieldNode : classNode.fields) {
            if (fieldNode.name.equals(name) && (desc == null || fieldNode.desc.equals(desc))) {
                return fieldNode;
            }
        }
        return null;
    }

    /**
     * Get a method from a class node.<br>
     * The method is searched by its name and descriptor.
     *
     * @param classNode The class node to search in
     * @param name      The name of the method
     * @param desc      The descriptor of the method
     * @return The method node or null if no method was found
     */
    public static MethodNode getMethod(final ClassNode classNode, final String name, final String desc) {
        for (MethodNode methodNode : classNode.methods) {
            if (methodNode.name.equals(name) && methodNode.desc.equals(desc)) {
                return methodNode;
            }
        }
        return null;
    }

    /**
     * Calculate the variable index of the first free variable in a method.
     *
     * @param methodNode The method to calculate the free variable index for
     * @return The free variable index
     */
    public static int freeVarIndex(final MethodNode methodNode) {
        int index = Modifiers.has(methodNode.access, Opcodes.ACC_STATIC) ? 0 : 1;
        for (Type type : argumentTypes(methodNode)) index += type.getSize();
        for (AbstractInsnNode instruction : methodNode.instructions) {
            if (instruction instanceof VarInsnNode) {
                VarInsnNode varInsnNode = (VarInsnNode) instruction;
                if (varInsnNode.var >= index) {
                    index = varInsnNode.var;
                    if (varInsnNode.getOpcode() == Opcodes.LLOAD || varInsnNode.getOpcode() == Opcodes.DLOAD) index++;
                    if (varInsnNode.getOpcode() == Opcodes.LSTORE || varInsnNode.getOpcode() == Opcodes.DSTORE) index++;
                    index++;
                }
            } else if (instruction instanceof IincInsnNode) {
                IincInsnNode iincInsnNode = (IincInsnNode) instruction;
                if (iincInsnNode.var >= index) index = iincInsnNode.var + 1;
            }
        }
        return index;
    }

    /**
     * Get the instructions to cast an object to a specific type.<br>
     * Boxed types will be unboxed.
     *
     * @param targetType The target type
     * @return The instructions to cast the object
     */
    public static InsnList castObjectTo(final Type targetType) {
        InsnList list = new InsnList();
        if (targetType.equals(Type.BOOLEAN_TYPE)) {
            list.add(new TypeInsnNode(Opcodes.CHECKCAST, internalName(Boolean.class)));
            list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, internalName(Boolean.class), "booleanValue", methodDescriptor(Type.BOOLEAN_TYPE), false));
        } else if (targetType.equals(Type.BYTE_TYPE)) {
            list.add(new TypeInsnNode(Opcodes.CHECKCAST, internalName(Byte.class)));
            list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, internalName(Byte.class), "byteValue", methodDescriptor(Type.BYTE_TYPE), false));
        } else if (targetType.equals(Type.SHORT_TYPE)) {
            list.add(new TypeInsnNode(Opcodes.CHECKCAST, internalName(Short.class)));
            list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, internalName(Short.class), "shortValue", methodDescriptor(Type.SHORT_TYPE), false));
        } else if (targetType.equals(Type.CHAR_TYPE)) {
            list.add(new TypeInsnNode(Opcodes.CHECKCAST, internalName(Character.class)));
            list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, internalName(Character.class), "charValue", methodDescriptor(Type.CHAR_TYPE), false));
        } else if (targetType.equals(Type.INT_TYPE)) {
            list.add(new TypeInsnNode(Opcodes.CHECKCAST, internalName(Integer.class)));
            list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, internalName(Integer.class), "intValue", methodDescriptor(Type.INT_TYPE), false));
        } else if (targetType.equals(Type.FLOAT_TYPE)) {
            list.add(new TypeInsnNode(Opcodes.CHECKCAST, internalName(Float.class)));
            list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, internalName(Float.class), "floatValue", methodDescriptor(Type.FLOAT_TYPE), false));
        } else if (targetType.equals(Type.LONG_TYPE)) {
            list.add(new TypeInsnNode(Opcodes.CHECKCAST, internalName(Long.class)));
            list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, internalName(Long.class), "longValue", methodDescriptor(Type.LONG_TYPE), false));
        } else if (targetType.equals(Type.DOUBLE_TYPE)) {
            list.add(new TypeInsnNode(Opcodes.CHECKCAST, internalName(Double.class)));
            list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, internalName(Double.class), "doubleValue", methodDescriptor(Type.DOUBLE_TYPE), false));
        } else {
            list.add(new TypeInsnNode(Opcodes.CHECKCAST, targetType.getInternalName()));
        }
        return list;
    }

    /**
     * Get the instruction to box a primitive type.
     *
     * @param primitive The primitive type to box
     * @return The instruction or null if the type is not a primitive type
     */
    @Nullable
    public static AbstractInsnNode boxPrimitive(final Type primitive) {
        if (primitive.equals(Type.BOOLEAN_TYPE)) {
            return new MethodInsnNode(Opcodes.INVOKESTATIC, internalName(Boolean.class), "valueOf", methodDescriptor(Boolean.class, boolean.class), false);
        } else if (primitive.equals(Type.BYTE_TYPE)) {
            return new MethodInsnNode(Opcodes.INVOKESTATIC, internalName(Byte.class), "valueOf", methodDescriptor(Byte.class, byte.class), false);
        } else if (primitive.equals(Type.SHORT_TYPE)) {
            return new MethodInsnNode(Opcodes.INVOKESTATIC, internalName(Short.class), "valueOf", methodDescriptor(Short.class, short.class), false);
        } else if (primitive.equals(Type.CHAR_TYPE)) {
            return new MethodInsnNode(Opcodes.INVOKESTATIC, internalName(Character.class), "valueOf", methodDescriptor(Character.class, char.class), false);
        } else if (primitive.equals(Type.INT_TYPE)) {
            return new MethodInsnNode(Opcodes.INVOKESTATIC, internalName(Integer.class), "valueOf", methodDescriptor(Integer.class, int.class), false);
        } else if (primitive.equals(Type.LONG_TYPE)) {
            return new MethodInsnNode(Opcodes.INVOKESTATIC, internalName(Long.class), "valueOf", methodDescriptor(Long.class, long.class), false);
        } else if (primitive.equals(Type.FLOAT_TYPE)) {
            return new MethodInsnNode(Opcodes.INVOKESTATIC, internalName(Float.class), "valueOf", methodDescriptor(Float.class, float.class), false);
        } else if (primitive.equals(Type.DOUBLE_TYPE)) {
            return new MethodInsnNode(Opcodes.INVOKESTATIC, internalName(Double.class), "valueOf", methodDescriptor(Double.class, double.class), false);
        } else {
            return null;
        }
    }

    /**
     * Get the number the given instruction pushes onto the stack.
     *
     * @param instruction The instruction to get the number from
     * @return The number or null if the instruction does not push a number onto the stack
     */
    @Nullable
    public static Number toNumber(@Nullable final AbstractInsnNode instruction) {
        if (instruction == null) return null;
        if (instruction.getOpcode() >= Opcodes.ICONST_M1 && instruction.getOpcode() <= Opcodes.ICONST_5) return instruction.getOpcode() - Opcodes.ICONST_0;
        else if (instruction.getOpcode() >= Opcodes.LCONST_0 && instruction.getOpcode() <= Opcodes.LCONST_1) return (long) (instruction.getOpcode() - Opcodes.LCONST_0);
        else if (instruction.getOpcode() >= Opcodes.FCONST_0 && instruction.getOpcode() <= Opcodes.FCONST_2) return (float) (instruction.getOpcode() - Opcodes.FCONST_0);
        else if (instruction.getOpcode() >= Opcodes.DCONST_0 && instruction.getOpcode() <= Opcodes.DCONST_1) return (double) (instruction.getOpcode() - Opcodes.DCONST_0);
        else if (instruction.getOpcode() == Opcodes.BIPUSH) return (byte) ((IntInsnNode) instruction).operand;
        else if (instruction.getOpcode() == Opcodes.SIPUSH) return (short) ((IntInsnNode) instruction).operand;
        else if (instruction.getOpcode() == Opcodes.LDC && ((LdcInsnNode) instruction).cst instanceof Number) return (Number) ((LdcInsnNode) instruction).cst;
        return null;
    }

    /**
     * Get the most optimal instruction to push the given integer onto the stack.
     *
     * @param i The integer to push
     * @return The instruction to push the integer
     */
    public static AbstractInsnNode intPush(final int i) {
        if (i >= -1 && i <= 5) return new InsnNode(Opcodes.ICONST_0 + i);
        else if (i >= Byte.MIN_VALUE && i <= Byte.MAX_VALUE) return new IntInsnNode(Opcodes.BIPUSH, i);
        else if (i >= Short.MIN_VALUE && i <= Short.MAX_VALUE) return new IntInsnNode(Opcodes.SIPUSH, i);
        else return new LdcInsnNode(i);
    }

    /**
     * Get the most optimal instruction to push the given long onto the stack.
     *
     * @param l The long to push
     * @return The instruction to push the long
     */
    public static AbstractInsnNode longPush(final long l) {
        if (l == 0) return new InsnNode(Opcodes.LCONST_0);
        else if (l == 1) return new InsnNode(Opcodes.LCONST_1);
        else return new LdcInsnNode(l);
    }

    /**
     * Get the most optimal instruction to push the given float onto the stack.
     *
     * @param f The float to push
     * @return The instruction to push the float
     */
    public static AbstractInsnNode floatPush(final float f) {
        if (f == 0) return new InsnNode(Opcodes.FCONST_0);
        else if (f == 1) return new InsnNode(Opcodes.FCONST_1);
        else if (f == 2) return new InsnNode(Opcodes.FCONST_2);
        else return new LdcInsnNode(f);
    }

    /**
     * Get the most optimal instruction to push the given double onto the stack.
     *
     * @param d The double to push
     * @return The instruction to push the double
     */
    public static AbstractInsnNode doublePush(final double d) {
        if (d == 0) return new InsnNode(Opcodes.DCONST_0);
        else if (d == 1) return new InsnNode(Opcodes.DCONST_1);
        else return new LdcInsnNode(d);
    }

    /**
     * Create a new empty class node.<br>
     * An empty constructor will be added to the class.
     *
     * @param name The name of the class
     * @return The created class node
     */
    public static ClassNode createEmptyClass(final String name) {
        ClassNode classNode = new ClassNode();
        classNode.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, slash(name), null, internalName(Object.class), null);

        MethodNode constructor = new MethodNode(Opcodes.ACC_PUBLIC, "<init>", methodDescriptor(Type.VOID_TYPE), null, null);
        constructor.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        constructor.instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, internalName(Object.class), "<init>", methodDescriptor(Type.VOID_TYPE), false));
        constructor.instructions.add(new InsnNode(Opcodes.RETURN));
        classNode.methods.add(constructor);

        return classNode;
    }

    /**
     * Calculate the indices of the parameters of a method.
     *
     * @param methodNode The method to calculate the indices for
     * @return The indices of the parameters
     */
    public static int[] parameterIndices(final MethodNode methodNode) {
        Type[] types = argumentTypes(methodNode);
        int[] indices = new int[types.length];
        int current = Modifiers.has(methodNode.access, Opcodes.ACC_STATIC) ? 0 : 1;
        for (int i = 0; i < types.length; i++) {
            Type type = types[i];
            indices[i] = current;
            current += type.getSize();
        }
        return indices;
    }

    /**
     * Get the instructions to swap the first two elements on the stack.
     *
     * @param top    The type of the top element
     * @param bottom The type of the bottom element
     * @return The instructions to swap the elements
     */
    public static InsnList swap(final Type top, final Type bottom) {
        InsnList insns = new InsnList();
        if (top.getSize() == 1 && bottom.getSize() == 1) {
            insns.add(new InsnNode(Opcodes.SWAP));
        } else if (top.getSize() == 2 && bottom.getSize() == 2) {
            insns.add(new InsnNode(Opcodes.DUP2_X2));
            insns.add(new InsnNode(Opcodes.POP2));
        } else if (top.getSize() == 2) {
            insns.add(new InsnNode(Opcodes.DUP_X2));
            insns.add(new InsnNode(Opcodes.POP));
        } else {
            insns.add(new InsnNode(Opcodes.DUP2_X1));
            insns.add(new InsnNode(Opcodes.POP2));
        }
        return insns;
    }

    /**
     * Get the instructions to load the arguments of a method onto the stack.
     *
     * @param method The method to load the arguments from
     * @return The instructions to load the arguments
     */
    public static InsnList loadMethodArgs(final MethodNode method) {
        Type[] args = Type.getArgumentTypes(method.desc);
        InsnList insns = new InsnList();
        int index = Modifiers.has(method.access, Opcodes.ACC_STATIC) ? 0 : 1;
        for (Type arg : args) {
            insns.add(new VarInsnNode(arg.getOpcode(Opcodes.ILOAD), index));
            index += arg.getSize();
        }
        return insns;
    }

    /**
     * Generate a variable name for a type that does not exist yet.
     *
     * @param type      The type to generate the name for
     * @param doesExist A predicate that checks if a name already exists (true if it exists)
     * @return The generated name
     */
    public static String generateVariableName(final Type type, final Predicate<String> doesExist) {
        String newName;
        if (type.getSort() == Type.ARRAY) {
            //Generate a name for an array type
            //[[Ljava/lang/Object; -> objectArrayArray
            newName = type.getElementType().getClassName();
            for (int j = 0; j < type.getDimensions(); j++) newName += "Array";
        } else {
            newName = type.getClassName();
        }
        if (type.getDescriptor().length() == 1) {
            //Primitive types are just lowercased
            newName = type.getDescriptor().toLowerCase(Locale.ROOT);
        } else {
            newName = newName.substring(newName.lastIndexOf('.') + 1); //Strip the package name
            newName = newName.substring(newName.lastIndexOf('$') + 1); //Strip the outer class name
            if (newName.toUpperCase(Locale.ROOT).equals(newName)) {
                //If the name is all uppercase, just lowercase it
                //e.g. UUID -> uuid
                newName = newName.toLowerCase(Locale.ROOT);
            } else {
                //Camel case the name
                //e.g. ClassInfoProvider -> classInfoProvider
                newName = newName.substring(0, 1).toLowerCase(Locale.ROOT) + newName.substring(1);
            }
        }
        if (SourceVersion.isKeyword(newName)) newName = "_" + newName; //Prepend an underscore if the name is a keyword

        int index = 2; //Start at 2. The first variable does not have a number
        String name = newName;
        while (doesExist.test(name)) name = newName + index++; //Append a number if the name already exists
        return name;
    }

}
