package net.lenni0451.commons.asm.annotations.parser;

import net.lenni0451.commons.asm.annotations.AnnotationUtils;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;

import static net.lenni0451.commons.asm.Types.type;

public class AnnotationParser {

    public static <T extends Annotation> T parse(final AnnotationNode annotationNode) throws ClassNotFoundException {
        return parse(AnnotationParser.class.getClassLoader(), annotationNode);
    }

    public static <T extends Annotation> T parse(final ClassLoader loader, final AnnotationNode annotationNode) throws ClassNotFoundException {
        Type type = type(annotationNode.desc);
        Class<?> clazz = loader.loadClass(type.getClassName());
        return parse(loader, (Class<T>) clazz, annotationNode);
    }

    public static <T extends Annotation> T parse(final Class<T> type, final AnnotationNode annotationNode) {
        return parse(type.getClassLoader(), type, annotationNode);
    }

    public static <T extends Annotation> T parse(final ClassLoader loader, final Class<T> type, final AnnotationNode annotationNode) {
        Map<String, Object> values = AnnotationUtils.listToMap(annotationNode.values);
        InvocationHandler invocationHandler = (proxy, method, args) -> {
            if (isMethod(method, Object.class, "toString", String.class)) {
                return type.getName() + "#Proxy@" + Integer.toHexString(System.identityHashCode(proxy));
            } else if (isMethod(method, Object.class, "hashCode", int.class)) {
                return System.identityHashCode(proxy);
            } else if (isMethod(method, Object.class, "equals", boolean.class, Object.class)) {
                return proxy == args[0];
            } else if (isMethod(method, Annotation.class, "annotationType", Class.class)) {
                return type;
            } else if (isMethod(method, ParsedAnnotation.class, "isSet", boolean.class, String.class)) {
                return values.containsKey((String) args[0]);
            } else if (isMethod(method, ParsedAnnotation.class, "getValues", Map.class)) {
                return values;
            } else if (isMethod(method, ParsedAnnotation.class, "getValue", Object.class, String.class)) {
                return values.get((String) args[0]);
            } else if (values.containsKey(method.getName())) {
                return parseValue(loader, method.getReturnType(), values.get(method.getName()));
            } else {
                Object defaultValue = method.getDefaultValue();
                if (defaultValue != null) return defaultValue;
                throw new NoSuchMethodException(method.getName());
            }
        };
        return (T) Proxy.newProxyInstance(loader, new Class[]{type, ParsedAnnotation.class}, invocationHandler);
    }

    private static Object parseValue(final ClassLoader loader, final Class<?> type, final Object value) throws ClassNotFoundException {
        if (value instanceof Type) {
            return loader.loadClass(((Type) value).getClassName());
        } else if (value instanceof String[]) {
            String[] array = (String[]) value;
            Type enumType = type(array[0]);
            Class<?> enumClass = loader.loadClass(enumType.getClassName());
            Object[] constants = enumClass.getEnumConstants();
            for (Object constant : constants) {
                Enum<?> enumConstant = (Enum<?>) constant;
                if (enumConstant.name().equals(array[1])) return enumConstant;
            }
            throw new IllegalArgumentException("Unknown enum constant: " + array[1]);
        } else if (value instanceof AnnotationNode) {
            return parse(loader, (Class<? extends Annotation>) type, (AnnotationNode) value);
        } else if (value instanceof List) {
            List<?> list = (List<?>) value;
            Object array = Array.newInstance(type.getComponentType(), list.size());
            for (int i = 0; i < list.size(); i++) {
                Array.set(array, i, parseValue(loader, type.getComponentType(), list.get(i)));
            }
            return array;
        }
        return value;
    }

    private static boolean isMethod(final Method method, final Class<?> owner, final String name, final Class<?> returnValue, final Class<?>... parameters) {
        if (!method.getName().equals(name)) return false;
        if (!method.getReturnType().equals(returnValue)) return false;
        if (method.getParameterCount() != parameters.length) return false;
        for (int i = 0; i < parameters.length; i++) {
            if (!method.getParameterTypes()[i].equals(parameters[i])) return false;
        }
        return true;
    }

}
