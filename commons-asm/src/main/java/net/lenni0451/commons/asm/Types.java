package net.lenni0451.commons.asm;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Types {

    public static boolean isPrimitive(final Type type) {
        if (type.equals(Type.VOID_TYPE)) return true;
        else if (type.equals(Type.BOOLEAN_TYPE)) return true;
        else if (type.equals(Type.BYTE_TYPE)) return true;
        else if (type.equals(Type.SHORT_TYPE)) return true;
        else if (type.equals(Type.CHAR_TYPE)) return true;
        else if (type.equals(Type.INT_TYPE)) return true;
        else if (type.equals(Type.LONG_TYPE)) return true;
        else if (type.equals(Type.FLOAT_TYPE)) return true;
        else if (type.equals(Type.DOUBLE_TYPE)) return true;
        else return false;
    }

    public static Type type(final Object ob) {
        if (ob instanceof String) {
            String s = (String) ob;
            try {
                return Type.getType(s);
            } catch (Throwable t) {
                return Type.getObjectType(s);
            }
        } else if (ob instanceof Class) {
            return Type.getType((Class<?>) ob);
        } else if (ob instanceof Field) {
            return Type.getType(((Field) ob).getType());
        } else if (ob instanceof Method) {
            return Type.getType((Method) ob);
        } else if (ob instanceof Constructor) {
            return Type.getType((Constructor<?>) ob);
        } else if (ob instanceof Type) {
            return (Type) ob;
        }
        throw new IllegalArgumentException("Unable to convert " + ob + " into a type");
    }

    public static Type returnType(final Object ob) {
        if (ob instanceof String) return Type.getReturnType((String) ob);
        else if (ob instanceof Method) return Type.getReturnType((Method) ob);
        else if (ob instanceof MethodNode) return Type.getReturnType(((MethodNode) ob).desc);
        else if (ob instanceof MethodInsnNode) return Type.getReturnType(((MethodInsnNode) ob).desc);
        else if (ob instanceof Type) return ((Type) ob).getReturnType();
        throw new IllegalArgumentException("Unable to get return type of " + ob);
    }

    public static Type[] argumentTypes(final Object ob) {
        if (ob instanceof String) return Type.getArgumentTypes((String) ob);
        else if (ob instanceof Method) return Type.getArgumentTypes((Method) ob);
        else if (ob instanceof MethodNode) return Type.getArgumentTypes(((MethodNode) ob).desc);
        else if (ob instanceof MethodInsnNode) return Type.getArgumentTypes(((MethodInsnNode) ob).desc);
        throw new IllegalArgumentException("Unable to get argument types of " + ob);
    }

    public static String internalName(final Object ob) {
        try {
            return type(ob).getInternalName();
        } catch (Throwable t) {
            throw new IllegalArgumentException("Unable to get internal name of " + ob, t);
        }
    }

    public static String typeDescriptor(final Object ob) {
        try {
            return type(ob).getDescriptor();
        } catch (Throwable t) {
            throw new IllegalArgumentException("Unable to convert " + ob + " into a descriptor", t);
        }
    }

    public static String methodDescriptor(final Object returnType, final Object... arguments) {
        if (returnType instanceof Method) {
            if (arguments.length != 0) throw new IllegalArgumentException("Expected arguments to be empty");
            return Type.getMethodDescriptor((Method) returnType);
        } else if (returnType instanceof Constructor) {
            if (arguments.length != 0) throw new IllegalArgumentException("Expected arguments to be empty");
            return Type.getConstructorDescriptor((Constructor<?>) returnType);
        }

        StringBuilder out = new StringBuilder("(");
        for (Object argument : arguments) out.append(typeDescriptor(argument));
        out.append(")").append(typeDescriptor(returnType));
        return out.toString();
    }

}
