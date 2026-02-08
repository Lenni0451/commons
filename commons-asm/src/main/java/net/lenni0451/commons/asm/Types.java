package net.lenni0451.commons.asm;

import lombok.experimental.UtilityClass;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@UtilityClass
public class Types {

    /**
     * Check if the given type is a primitive type.<br>
     * Primitive types are: {@code void}, {@code boolean}, {@code byte}, {@code short}, {@code char}, {@code int}, {@code long}, {@code float}, {@code double}
     *
     * @param type The type to check
     * @return If the given type is a primitive type
     */
    public static boolean isPrimitive(final Type type) {
        if (type.equals(Type.VOID_TYPE)) return true;
        if (type.equals(Type.BOOLEAN_TYPE)) return true;
        if (type.equals(Type.BYTE_TYPE)) return true;
        if (type.equals(Type.SHORT_TYPE)) return true;
        if (type.equals(Type.CHAR_TYPE)) return true;
        if (type.equals(Type.INT_TYPE)) return true;
        if (type.equals(Type.LONG_TYPE)) return true;
        if (type.equals(Type.FLOAT_TYPE)) return true;
        if (type.equals(Type.DOUBLE_TYPE)) return true;
        return false;
    }

    /**
     * Parse the given object into a {@link Type}.<br>
     * Supported types:
     * <table>
     *     <tr>
     *         <td>{@link String}</td>
     *         <td>Type or object type</td>
     *     </tr>
     *     <tr>
     *         <td>{@link Class}</td>
     *         <td></td>
     *     </tr>
     *     <tr>
     *         <td>{@link Field}</td>
     *         <td>Type of the field</td>
     *     </tr>
     *     <tr>
     *         <td>{@link FieldNode}</td>
     *         <td>Descriptor of the field</td>
     *     </tr>
     *     <tr>
     *         <td>{@link Method}</td>
     *         <td>Method descriptor</td>
     *     </tr>
     *     <tr>
     *         <td>{@link MethodNode}</td>
     *         <td>Method descriptor</td>
     *     </tr>
     *     <tr>
     *         <td>{@link Constructor}</td>
     *         <td>Method descriptor</td>
     *     </tr>
     *     <tr>
     *         <td>{@link Type}</td>
     *         <td>Will be returned as is</td>
     *     </tr>
     * </table>
     *
     * @param ob The object to parse
     * @return The parsed type
     */
    public static Type type(final Object ob) {
        if (ob instanceof String) {
            String s = (String) ob;
            try {
                return Type.getType(s);
            } catch (Throwable t) {
                return Type.getObjectType(s);
            }
        }
        if (ob instanceof Class) return Type.getType((Class<?>) ob);
        if (ob instanceof Field) return Type.getType(((Field) ob).getType());
        if (ob instanceof FieldNode) return Type.getType(((FieldNode) ob).desc);
        if (ob instanceof Method) return Type.getType((Method) ob);
        if (ob instanceof MethodNode) return Type.getType(((MethodNode) ob).desc);
        if (ob instanceof Constructor) return Type.getType((Constructor<?>) ob);
        if (ob instanceof Type) return (Type) ob;
        throw new IllegalArgumentException("Unable to convert " + ob + " into a type");
    }

    /**
     * Get the return type of the given object.<br>
     * Supported types:
     * <table>
     *     <tr>
     *         <td>{@link String}</td>
     *         <td>Method descriptor</td>
     *     </tr>
     *     <tr>
     *         <td>{@link Method}</td>
     *         <td></td>
     *     </tr>
     *     <tr>
     *         <td>{@link MethodNode}</td>
     *         <td></td>
     *     </tr>
     *     <tr>
     *         <td>{@link MethodInsnNode}</td>
     *         <td></td>
     *     </tr>
     *     <tr>
     *         <td>{@link Type}</td>
     *         <td>Will be returned as is</td>
     *     </tr>
     * </table>
     *
     * @param ob The object to get the return type from
     * @return The return type
     */
    public static Type returnType(final Object ob) {
        if (ob instanceof String) return Type.getReturnType((String) ob);
        if (ob instanceof Method) return Type.getReturnType((Method) ob);
        if (ob instanceof MethodNode) return Type.getReturnType(((MethodNode) ob).desc);
        if (ob instanceof MethodInsnNode) return Type.getReturnType(((MethodInsnNode) ob).desc);
        if (ob instanceof Type) return ((Type) ob).getReturnType();
        throw new IllegalArgumentException("Unable to get return type of " + ob);
    }

    /**
     * Get the argument types of the given object.<br>
     * Supported types:
     * <table>
     *     <tr>
     *         <td>{@link String}</td>
     *         <td>Method descriptor</td>
     *     </tr>
     *     <tr>
     *         <td>{@link Method}</td>
     *         <td></td>
     *     </tr>
     *     <tr>
     *         <td>{@link MethodNode}</td>
     *         <td></td>
     *     </tr>
     *     <tr>
     *         <td>{@link MethodInsnNode}</td>
     *         <td></td>
     *     </tr>
     * </table>
     *
     * @param ob The object to get the argument types from
     * @return The argument types
     */
    public static Type[] argumentTypes(final Object ob) {
        if (ob instanceof String) return Type.getArgumentTypes((String) ob);
        if (ob instanceof Method) return Type.getArgumentTypes((Method) ob);
        if (ob instanceof MethodNode) return Type.getArgumentTypes(((MethodNode) ob).desc);
        if (ob instanceof MethodInsnNode) return Type.getArgumentTypes(((MethodInsnNode) ob).desc);
        throw new IllegalArgumentException("Unable to get argument types of " + ob);
    }

    /**
     * Get the internal name of the given object.<br>
     * See {@link #type(Object)} for supported types.
     *
     * @param ob The object to get the internal name from
     * @return The internal name
     */
    public static String internalName(final Object ob) {
        try {
            return type(ob).getInternalName();
        } catch (Throwable t) {
            throw new IllegalArgumentException("Unable to get internal name of " + ob, t);
        }
    }

    /**
     * Get the descriptor of the given object.<br>
     * See {@link #type(Object)} for supported types.
     *
     * @param ob The object to get the descriptor from
     * @return The descriptor
     */
    public static String typeDescriptor(final Object ob) {
        try {
            return type(ob).getDescriptor();
        } catch (Throwable t) {
            throw new IllegalArgumentException("Unable to convert " + ob + " into a descriptor", t);
        }
    }

    /**
     * Get the method descriptor of the given method.
     *
     * @param method The method to get the descriptor from
     * @return The method descriptor
     */
    public static String methodDescriptor(final Method method) {
        return Type.getMethodDescriptor(method);
    }

    /**
     * Get the method descriptor of the given constructor.
     *
     * @param constructor The constructor to get the descriptor from
     * @return The method descriptor
     */
    public static String methodDescriptor(final Constructor<?> constructor) {
        return Type.getConstructorDescriptor(constructor);
    }

    /**
     * Get method the descriptor for the given types.<br>
     * See {@link #type(Object)} for supported types.
     *
     * @param returnType The return type of the method
     * @param arguments  The arguments of the method
     * @return The descriptor of the method
     */
    public static String methodDescriptor(final Object returnType, final Object... arguments) {
        StringBuilder out = new StringBuilder("(");
        for (Object argument : arguments) {
            out.append(typeDescriptor(argument));
        }
        out.append(")").append(typeDescriptor(returnType));
        return out.toString();
    }

}
