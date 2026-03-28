package net.lenni0451.commons.asm.analysis;

import org.objectweb.asm.ConstantDynamic;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.Interpreter;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * An ASM Interpreter that tracks both the known constant values of the stack/locals and the Set of instructions (origins) that contributed to the current state.
 *
 * @see Analyzer
 */
public class ValueTrackingInterpreter extends Interpreter<TrackedValue> {

    public ValueTrackingInterpreter() {
        super(Opcodes.ASM9);
    }

    @Override
    public TrackedValue newValue(final Type type) {
        if (type == null) {
            return new TrackedValue(null, null, Collections.emptySet());
        } else if (type == Type.VOID_TYPE) {
            return null;
        } else {
            return new TrackedValue(type, null, Collections.emptySet());
        }
    }

    @Override
    public TrackedValue newOperation(final AbstractInsnNode insn) throws AnalyzerException {
        Type type;
        Object value = null;

        switch (insn.getOpcode()) {
            case Opcodes.ACONST_NULL:
                type = Type.getType(Object.class);
                break;
            case Opcodes.ICONST_M1:
            case Opcodes.ICONST_0:
            case Opcodes.ICONST_1:
            case Opcodes.ICONST_2:
            case Opcodes.ICONST_3:
            case Opcodes.ICONST_4:
            case Opcodes.ICONST_5:
                type = Type.INT_TYPE;
                value = insn.getOpcode() - Opcodes.ICONST_0;
                break;
            case Opcodes.LCONST_0:
            case Opcodes.LCONST_1:
                type = Type.LONG_TYPE;
                value = (long) (insn.getOpcode() - Opcodes.LCONST_0);
                break;
            case Opcodes.FCONST_0:
            case Opcodes.FCONST_1:
            case Opcodes.FCONST_2:
                type = Type.FLOAT_TYPE;
                value = (float) (insn.getOpcode() - Opcodes.FCONST_0);
                break;
            case Opcodes.DCONST_0:
            case Opcodes.DCONST_1:
                type = Type.DOUBLE_TYPE;
                value = (double) (insn.getOpcode() - Opcodes.DCONST_0);
                break;
            case Opcodes.BIPUSH:
            case Opcodes.SIPUSH:
                type = Type.INT_TYPE;
                value = ((IntInsnNode) insn).operand;
                break;
            case Opcodes.LDC:
                value = ((LdcInsnNode) insn).cst;
                if (value instanceof Integer) {
                    type = Type.INT_TYPE;
                } else if (value instanceof Float) {
                    type = Type.FLOAT_TYPE;
                } else if (value instanceof Long) {
                    type = Type.LONG_TYPE;
                } else if (value instanceof Double) {
                    type = Type.DOUBLE_TYPE;
                } else if (value instanceof String) {
                    type = Type.getType(String.class);
                } else if (value instanceof Type) {
                    int sort = ((Type) value).getSort();
                    if (sort == Type.OBJECT || sort == Type.ARRAY) {
                        type = Type.getType(Class.class);
                    } else if (sort == Type.METHOD) {
                        type = Type.getType(MethodType.class);
                    } else {
                        throw new AnalyzerException(insn, "Illegal LDC value " + value);
                    }
                } else if (value instanceof Handle) {
                    type = Type.getType(MethodHandle.class);
                } else if (value instanceof ConstantDynamic) {
                    type = Type.getType(((ConstantDynamic) value).getDescriptor());
                } else {
                    throw new AnalyzerException(insn, "Illegal LDC value " + value);
                }
                break;
            case Opcodes.JSR:
                type = Type.VOID_TYPE;
                break;
            case Opcodes.GETSTATIC:
                type = Type.getType(((FieldInsnNode) insn).desc);
                break;
            case Opcodes.NEW:
                type = Type.getObjectType(((TypeInsnNode) insn).desc);
                break;
            default:
                throw new IllegalArgumentException("Unsupported opcode for newOperation: " + insn.getOpcode());
        }
        return new TrackedValue(type, value, insn);
    }

    @Override
    public TrackedValue copyOperation(final AbstractInsnNode insn, final TrackedValue value) {
        return new TrackedValue(value.getType(), value.getValue(), insn);
    }

    @Override
    public TrackedValue unaryOperation(final AbstractInsnNode insn, final TrackedValue value) throws AnalyzerException {
        Type type = null;
        Object resultValue = null;
        switch (insn.getOpcode()) {
            case Opcodes.INEG:
                type = Type.INT_TYPE;
                resultValue = this.transformNumber(insn, "INEG", value.getValue(), n -> -n.intValue());
                break;
            case Opcodes.IINC:
                type = Type.INT_TYPE;
                resultValue = this.transformNumber(insn, "IINC", value.getValue(), n -> n.intValue() + ((IincInsnNode) insn).incr);
                break;
            case Opcodes.L2I:
                type = Type.INT_TYPE;
                resultValue = this.transformNumber(insn, "L2I", value.getValue(), Number::intValue);
                break;
            case Opcodes.F2I:
                type = Type.INT_TYPE;
                resultValue = this.transformNumber(insn, "F2I", value.getValue(), Number::intValue);
                break;
            case Opcodes.D2I:
                type = Type.INT_TYPE;
                resultValue = this.transformNumber(insn, "D2I", value.getValue(), Number::intValue);
                break;
            case Opcodes.I2B:
                type = Type.BYTE_TYPE;
                resultValue = this.transformNumber(insn, "I2B", value.getValue(), Number::byteValue);
                break;
            case Opcodes.I2C:
                type = Type.CHAR_TYPE;
                resultValue = this.transformNumber(insn, "I2C", value.getValue(), n -> (int) (char) n.intValue());
                break;
            case Opcodes.I2S:
                type = Type.SHORT_TYPE;
                resultValue = this.transformNumber(insn, "I2S", value.getValue(), Number::shortValue);
                break;
            case Opcodes.FNEG:
                type = Type.FLOAT_TYPE;
                resultValue = this.transformNumber(insn, "FNEG", value.getValue(), n -> -n.floatValue());
                break;
            case Opcodes.I2F:
                type = Type.FLOAT_TYPE;
                resultValue = this.transformNumber(insn, "I2F", value.getValue(), Number::floatValue);
                break;
            case Opcodes.L2F:
                type = Type.FLOAT_TYPE;
                resultValue = this.transformNumber(insn, "L2F", value.getValue(), Number::floatValue);
                break;
            case Opcodes.D2F:
                type = Type.FLOAT_TYPE;
                resultValue = this.transformNumber(insn, "D2F", value.getValue(), Number::floatValue);
                break;
            case Opcodes.LNEG:
                type = Type.LONG_TYPE;
                resultValue = this.transformNumber(insn, "LNEG", value.getValue(), n -> -n.longValue());
                break;
            case Opcodes.I2L:
                type = Type.LONG_TYPE;
                resultValue = this.transformNumber(insn, "I2L", value.getValue(), Number::longValue);
                break;
            case Opcodes.F2L:
                type = Type.LONG_TYPE;
                resultValue = this.transformNumber(insn, "F2L", value.getValue(), Number::longValue);
                break;
            case Opcodes.D2L:
                type = Type.LONG_TYPE;
                resultValue = this.transformNumber(insn, "D2L", value.getValue(), Number::longValue);
                break;
            case Opcodes.DNEG:
                type = Type.DOUBLE_TYPE;
                resultValue = this.transformNumber(insn, "DNEG", value.getValue(), n -> -n.doubleValue());
                break;
            case Opcodes.I2D:
                type = Type.DOUBLE_TYPE;
                resultValue = this.transformNumber(insn, "I2D", value.getValue(), Number::doubleValue);
                break;
            case Opcodes.L2D:
                type = Type.DOUBLE_TYPE;
                resultValue = this.transformNumber(insn, "L2D", value.getValue(), Number::doubleValue);
                break;
            case Opcodes.F2D:
                type = Type.DOUBLE_TYPE;
                resultValue = this.transformNumber(insn, "F2D", value.getValue(), Number::doubleValue);
                break;
            case Opcodes.IFEQ:
            case Opcodes.IFNE:
            case Opcodes.IFLT:
            case Opcodes.IFGE:
            case Opcodes.IFGT:
            case Opcodes.IFLE:
            case Opcodes.TABLESWITCH:
            case Opcodes.LOOKUPSWITCH:
            case Opcodes.IRETURN:
            case Opcodes.LRETURN:
            case Opcodes.FRETURN:
            case Opcodes.DRETURN:
            case Opcodes.ARETURN:
            case Opcodes.PUTSTATIC:
                break;
            case Opcodes.GETFIELD:
                type = Type.getType(((FieldInsnNode) insn).desc);
                break;
            case Opcodes.NEWARRAY:
                switch (((IntInsnNode) insn).operand) {
                    case Opcodes.T_BOOLEAN:
                        type = Type.getType(boolean[].class);
                        break;
                    case Opcodes.T_CHAR:
                        type = Type.getType(char[].class);
                        break;
                    case Opcodes.T_BYTE:
                        type = Type.getType(byte[].class);
                        break;
                    case Opcodes.T_SHORT:
                        type = Type.getType(short[].class);
                        break;
                    case Opcodes.T_INT:
                        type = Type.getType(int[].class);
                        break;
                    case Opcodes.T_FLOAT:
                        type = Type.getType(float[].class);
                        break;
                    case Opcodes.T_DOUBLE:
                        type = Type.getType(double[].class);
                        break;
                    case Opcodes.T_LONG:
                        type = Type.getType(long[].class);
                        break;
                    default:
                        throw new AnalyzerException(insn, "Invalid array type: " + ((IntInsnNode) insn).operand);
                }
                break;
            case Opcodes.ANEWARRAY:
                type = Type.getType("[" + Type.getObjectType(((TypeInsnNode) insn).desc));
                break;
            case Opcodes.ARRAYLENGTH:
                type = Type.INT_TYPE;
                break;
            case Opcodes.CHECKCAST:
                type = Type.getObjectType(((TypeInsnNode) insn).desc);
                resultValue = value.getValue();
                break;
            case Opcodes.ATHROW:
                break;
            case Opcodes.INSTANCEOF:
                type = Type.INT_TYPE;
                break;
            case Opcodes.MONITORENTER:
            case Opcodes.MONITOREXIT:
            case Opcodes.IFNULL:
            case Opcodes.IFNONNULL:
                break;
            default:
                throw new IllegalArgumentException("Unsupported opcode for unaryOperation: " + insn.getOpcode());
        }
        if (type == null) return null;
        return new TrackedValue(type, resultValue, insn);
    }

    @Override
    public TrackedValue binaryOperation(final AbstractInsnNode insn, final TrackedValue value1, final TrackedValue value2) throws AnalyzerException {
        Type type = null;
        Object resultValue = null;
        switch (insn.getOpcode()) {
            case Opcodes.IALOAD:
                type = Type.INT_TYPE;
                break;
            case Opcodes.LALOAD:
                type = Type.LONG_TYPE;
                break;
            case Opcodes.FALOAD:
                type = Type.FLOAT_TYPE;
                break;
            case Opcodes.DALOAD:
                type = Type.DOUBLE_TYPE;
                break;
            case Opcodes.AALOAD:
                type = Type.getType(Object.class);
                break;
            case Opcodes.BALOAD:
                type = Type.BYTE_TYPE;
                break;
            case Opcodes.CALOAD:
                type = Type.CHAR_TYPE;
                break;
            case Opcodes.SALOAD:
                type = Type.SHORT_TYPE;
                break;
            case Opcodes.IADD:
                type = Type.INT_TYPE;
                resultValue = this.transformNumbers(insn, "IADD", value1.getValue(), value2.getValue(), (a, b) -> a.intValue() + b.intValue());
                break;
            case Opcodes.ISUB:
                type = Type.INT_TYPE;
                resultValue = this.transformNumbers(insn, "ISUB", value1.getValue(), value2.getValue(), (a, b) -> a.intValue() - b.intValue());
                break;
            case Opcodes.IMUL:
                type = Type.INT_TYPE;
                resultValue = this.transformNumbers(insn, "IMUL", value1.getValue(), value2.getValue(), (a, b) -> a.intValue() * b.intValue());
                break;
            case Opcodes.IDIV:
                type = Type.INT_TYPE;
                resultValue = this.transformNumbers(insn, "IDIV", value1.getValue(), value2.getValue(), (a, b) -> a.intValue() / b.intValue());
                break;
            case Opcodes.IREM:
                type = Type.INT_TYPE;
                resultValue = this.transformNumbers(insn, "IREM", value1.getValue(), value2.getValue(), (a, b) -> a.intValue() % b.intValue());
                break;
            case Opcodes.ISHL:
                type = Type.INT_TYPE;
                resultValue = this.transformNumbers(insn, "ISHL", value1.getValue(), value2.getValue(), (a, b) -> a.intValue() << b.intValue());
                break;
            case Opcodes.ISHR:
                type = Type.INT_TYPE;
                resultValue = this.transformNumbers(insn, "ISHR", value1.getValue(), value2.getValue(), (a, b) -> a.intValue() >> b.intValue());
                break;
            case Opcodes.IUSHR:
                type = Type.INT_TYPE;
                resultValue = this.transformNumbers(insn, "IUSHR", value1.getValue(), value2.getValue(), (a, b) -> a.intValue() >>> b.intValue());
                break;
            case Opcodes.IAND:
                type = Type.INT_TYPE;
                resultValue = this.transformNumbers(insn, "IAND", value1.getValue(), value2.getValue(), (a, b) -> a.intValue() & b.intValue());
                break;
            case Opcodes.IOR:
                type = Type.INT_TYPE;
                resultValue = this.transformNumbers(insn, "IOR", value1.getValue(), value2.getValue(), (a, b) -> a.intValue() | b.intValue());
                break;
            case Opcodes.IXOR:
                type = Type.INT_TYPE;
                resultValue = this.transformNumbers(insn, "IXOR", value1.getValue(), value2.getValue(), (a, b) -> a.intValue() ^ b.intValue());
                break;
            case Opcodes.FADD:
                type = Type.FLOAT_TYPE;
                resultValue = this.transformNumbers(insn, "FADD", value1.getValue(), value2.getValue(), (a, b) -> a.floatValue() + b.floatValue());
                break;
            case Opcodes.FSUB:
                type = Type.FLOAT_TYPE;
                resultValue = this.transformNumbers(insn, "FSUB", value1.getValue(), value2.getValue(), (a, b) -> a.floatValue() - b.floatValue());
                break;
            case Opcodes.FMUL:
                type = Type.FLOAT_TYPE;
                resultValue = this.transformNumbers(insn, "FMUL", value1.getValue(), value2.getValue(), (a, b) -> a.floatValue() * b.floatValue());
                break;
            case Opcodes.FDIV:
                type = Type.FLOAT_TYPE;
                resultValue = this.transformNumbers(insn, "FDIV", value1.getValue(), value2.getValue(), (a, b) -> a.floatValue() / b.floatValue());
                break;
            case Opcodes.FREM:
                type = Type.FLOAT_TYPE;
                resultValue = this.transformNumbers(insn, "FREM", value1.getValue(), value2.getValue(), (a, b) -> a.floatValue() % b.floatValue());
                break;
            case Opcodes.LADD:
                type = Type.LONG_TYPE;
                resultValue = this.transformNumbers(insn, "LADD", value1.getValue(), value2.getValue(), (a, b) -> a.longValue() + b.longValue());
                break;
            case Opcodes.LSUB:
                type = Type.LONG_TYPE;
                resultValue = this.transformNumbers(insn, "LSUB", value1.getValue(), value2.getValue(), (a, b) -> a.longValue() - b.longValue());
                break;
            case Opcodes.LMUL:
                type = Type.LONG_TYPE;
                resultValue = this.transformNumbers(insn, "LMUL", value1.getValue(), value2.getValue(), (a, b) -> a.longValue() * b.longValue());
                break;
            case Opcodes.LDIV:
                type = Type.LONG_TYPE;
                resultValue = this.transformNumbers(insn, "LDIV", value1.getValue(), value2.getValue(), (a, b) -> a.longValue() / b.longValue());
                break;
            case Opcodes.LREM:
                type = Type.LONG_TYPE;
                resultValue = this.transformNumbers(insn, "LREM", value1.getValue(), value2.getValue(), (a, b) -> a.longValue() % b.longValue());
                break;
            case Opcodes.LSHL:
                type = Type.LONG_TYPE;
                resultValue = this.transformNumbers(insn, "LSHL", value1.getValue(), value2.getValue(), (a, b) -> a.longValue() << b.longValue());
                break;
            case Opcodes.LSHR:
                type = Type.LONG_TYPE;
                resultValue = this.transformNumbers(insn, "LSHR", value1.getValue(), value2.getValue(), (a, b) -> a.longValue() >> b.longValue());
                break;
            case Opcodes.LUSHR:
                type = Type.LONG_TYPE;
                resultValue = this.transformNumbers(insn, "LUSHR", value1.getValue(), value2.getValue(), (a, b) -> a.longValue() >>> b.longValue());
                break;
            case Opcodes.LAND:
                type = Type.LONG_TYPE;
                resultValue = this.transformNumbers(insn, "LAND", value1.getValue(), value2.getValue(), (a, b) -> a.longValue() & b.longValue());
                break;
            case Opcodes.LOR:
                type = Type.LONG_TYPE;
                resultValue = this.transformNumbers(insn, "LOR", value1.getValue(), value2.getValue(), (a, b) -> a.longValue() | b.longValue());
                break;
            case Opcodes.LXOR:
                type = Type.LONG_TYPE;
                resultValue = this.transformNumbers(insn, "LXOR", value1.getValue(), value2.getValue(), (a, b) -> a.longValue() ^ b.longValue());
                break;
            case Opcodes.DADD:
                type = Type.DOUBLE_TYPE;
                resultValue = this.transformNumbers(insn, "DADD", value1.getValue(), value2.getValue(), (a, b) -> a.doubleValue() + b.doubleValue());
                break;
            case Opcodes.DSUB:
                type = Type.DOUBLE_TYPE;
                resultValue = this.transformNumbers(insn, "DSUB", value1.getValue(), value2.getValue(), (a, b) -> a.doubleValue() - b.doubleValue());
                break;
            case Opcodes.DMUL:
                type = Type.DOUBLE_TYPE;
                resultValue = this.transformNumbers(insn, "DMUL", value1.getValue(), value2.getValue(), (a, b) -> a.doubleValue() * b.doubleValue());
                break;
            case Opcodes.DDIV:
                type = Type.DOUBLE_TYPE;
                resultValue = this.transformNumbers(insn, "DDIV", value1.getValue(), value2.getValue(), (a, b) -> a.doubleValue() / b.doubleValue());
                break;
            case Opcodes.DREM:
                type = Type.DOUBLE_TYPE;
                resultValue = this.transformNumbers(insn, "DREM", value1.getValue(), value2.getValue(), (a, b) -> a.doubleValue() % b.doubleValue());
                break;
            case Opcodes.LCMP:
                type = Type.INT_TYPE;
                resultValue = this.transformNumbers(insn, "LCMP", value1.getValue(), value2.getValue(), (a, b) -> Long.compare(a.longValue(), b.longValue()));
                break;
            case Opcodes.FCMPL:
                type = Type.INT_TYPE;
                resultValue = this.transformNumbers(insn, "FCMPL", value1.getValue(), value2.getValue(), (a, b) -> {
                    if (Float.isNaN(a.floatValue()) || Float.isNaN(b.floatValue())) return -1;
                    return Float.compare(a.floatValue(), b.floatValue());
                });
                break;
            case Opcodes.FCMPG:
                type = Type.INT_TYPE;
                resultValue = this.transformNumbers(insn, "FCMPG", value1.getValue(), value2.getValue(), (a, b) -> {
                    if (Float.isNaN(a.floatValue()) || Float.isNaN(b.floatValue())) return 1;
                    return Float.compare(a.floatValue(), b.floatValue());
                });
                break;
            case Opcodes.DCMPL:
                type = Type.INT_TYPE;
                resultValue = this.transformNumbers(insn, "DCMPL", value1.getValue(), value2.getValue(), (a, b) -> {
                    if (Double.isNaN(a.doubleValue()) || Double.isNaN(b.doubleValue())) return -1;
                    return Double.compare(a.doubleValue(), b.doubleValue());
                });
                break;
            case Opcodes.DCMPG:
                type = Type.INT_TYPE;
                resultValue = this.transformNumbers(insn, "DCMPG", value1.getValue(), value2.getValue(), (a, b) -> {
                    if (Double.isNaN(a.doubleValue()) || Double.isNaN(b.doubleValue())) return 1;
                    return Double.compare(a.doubleValue(), b.doubleValue());
                });
                break;
            case Opcodes.IF_ICMPEQ:
            case Opcodes.IF_ICMPNE:
            case Opcodes.IF_ICMPLT:
            case Opcodes.IF_ICMPGE:
            case Opcodes.IF_ICMPGT:
            case Opcodes.IF_ICMPLE:
            case Opcodes.IF_ACMPEQ:
            case Opcodes.IF_ACMPNE:
            case Opcodes.PUTFIELD:
                break;
            default:
                throw new IllegalArgumentException("Unsupported opcode for binaryOperation: " + insn.getOpcode());
        }
        if (type == null) return null;
        return new TrackedValue(type, resultValue, insn);
    }

    @Override
    public TrackedValue ternaryOperation(final AbstractInsnNode insn, final TrackedValue value1, final TrackedValue value2, final TrackedValue value3) {
        return null;
    }

    @Override
    public TrackedValue naryOperation(final AbstractInsnNode insn, final List<? extends TrackedValue> values) {
        Type type;
        if (insn.getOpcode() == Opcodes.MULTIANEWARRAY) {
            type = Type.getType(((MultiANewArrayInsnNode) insn).desc);
        } else if (insn.getOpcode() == Opcodes.INVOKEDYNAMIC) {
            type = Type.getReturnType(((InvokeDynamicInsnNode) insn).desc);
        } else {
            type = Type.getReturnType(((MethodInsnNode) insn).desc);
        }
        return new TrackedValue(type, null, insn);
    }

    @Override
    public void returnOperation(final AbstractInsnNode insn, final TrackedValue value, final TrackedValue expected) {
    }

    @Override
    public TrackedValue merge(final TrackedValue value1, final TrackedValue value2) {
        boolean typesMatch = Objects.equals(value1.getType(), value2.getType());
        boolean valuesMatch = Objects.equals(value1.getValue(), value2.getValue());
        boolean originsCovered = value1.getOrigins().containsAll(value2.getOrigins());
        if (typesMatch && valuesMatch && originsCovered) return value1;

        Type mergedType;
        if (typesMatch) {
            mergedType = value1.getType();
        } else if (value1.getType() != null && value2.getType() != null
                && (value1.getType().getSort() == Type.OBJECT || value1.getType().getSort() == Type.ARRAY)
                && (value2.getType().getSort() == Type.OBJECT || value2.getType().getSort() == Type.ARRAY)) {
            mergedType = Type.getType(Object.class);
        } else {
            mergedType = null;
        }

        Object mergedValue = valuesMatch ? value1.getValue() : null;
        Set<AbstractInsnNode> mergedOrigins = new HashSet<>(value1.getOrigins());
        mergedOrigins.addAll(value2.getOrigins());
        return new TrackedValue(mergedType, mergedValue, mergedOrigins);
    }

    private Object transformNumber(final AbstractInsnNode insn, final String insnName, final Object value, final Function<Number, Object> transformer) throws AnalyzerException {
        if (value != null) {
            if (value instanceof Number) {
                return transformer.apply((Number) value);
            } else {
                throw new AnalyzerException(insn, "Expected number for " + insnName + ", got " + value.getClass().getName());
            }
        }
        return null;
    }

    private Object transformNumbers(final AbstractInsnNode insn, final String insnName, final Object value1, final Object value2, final BiFunction<Number, Number, Object> transformer) throws AnalyzerException {
        if (value1 != null && value2 != null) {
            if (value1 instanceof Number && value2 instanceof Number) {
                try {
                    return transformer.apply((Number) value1, (Number) value2);
                } catch (ArithmeticException e) {
                    // In case of division by zero or similar issues, just return null to continue analysis
                    return null;
                }
            } else {
                throw new AnalyzerException(insn, "Expected numbers for " + insnName + ", got " + value1.getClass().getName() + " and " + value2.getClass().getName());
            }
        }
        return null;
    }

}
