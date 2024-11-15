package net.lenni0451.commons.asm;

import org.junit.jupiter.api.Test;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import static net.lenni0451.commons.asm.Types.methodDescriptor;
import static org.junit.jupiter.api.Assertions.*;

class ASMUtilsTest {

    @Test
    void dot() {
        assertEquals("java.lang.String", ASMUtils.dot("java/lang/String"));
    }

    @Test
    void slash() {
        assertEquals("java/lang/String", ASMUtils.slash("java.lang.String"));
    }

    @Test
    void getMethod() {
        ClassNode classNode = new ClassNode();
        classNode.methods.add(new MethodNode(Opcodes.ACC_PUBLIC, "test", methodDescriptor(void.class), null, null));

        assertNotNull(ASMUtils.getMethod(classNode, "test", methodDescriptor(void.class)));
        assertNull(ASMUtils.getMethod(classNode, "test", methodDescriptor(int.class)));
    }

    @Test
    void getField() {
        ClassNode classNode = new ClassNode();
        classNode.fields.add(new FieldNode(Opcodes.ACC_PUBLIC, "test", "I", null, null));

        assertNotNull(ASMUtils.getField(classNode, "test", "I"));
        assertNull(ASMUtils.getField(classNode, "test", "J"));
    }

    @Test
    void freeVarIndex() {
        MethodNode method = new MethodNode(0, "test", methodDescriptor(void.class, double.class, int.class), null, null);
        method.instructions.add(new InsnNode(Opcodes.NOP));
        method.instructions.add(new IincInsnNode(5, 10));
        method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
        method.instructions.add(new VarInsnNode(Opcodes.DSTORE, 11));

        assertEquals(13, ASMUtils.freeVarIndex(method));
    }

    @Test
    void toNumber() {
        assertEquals(4, ASMUtils.toNumber(new InsnNode(Opcodes.ICONST_4)));
        assertEquals((byte) 16, ASMUtils.toNumber(new IntInsnNode(Opcodes.BIPUSH, 16)));
        assertEquals((short) 46, ASMUtils.toNumber(new IntInsnNode(Opcodes.SIPUSH, 46)));
        assertEquals(100F, ASMUtils.toNumber(new LdcInsnNode(100F)));
        assertNull(ASMUtils.toNumber(new InsnNode(Opcodes.NOP)));
    }

    @Test
    void intPush() {
        assertEquals(Opcodes.ICONST_M1, ASMUtils.intPush(-1).getOpcode());
        assertEquals(Opcodes.ICONST_0, ASMUtils.intPush(0).getOpcode());
        assertEquals(Opcodes.ICONST_1, ASMUtils.intPush(1).getOpcode());
        assertEquals(Opcodes.ICONST_2, ASMUtils.intPush(2).getOpcode());
        assertEquals(Opcodes.ICONST_3, ASMUtils.intPush(3).getOpcode());
        assertEquals(Opcodes.ICONST_4, ASMUtils.intPush(4).getOpcode());
        assertEquals(Opcodes.ICONST_5, ASMUtils.intPush(5).getOpcode());
        assertEquals(Opcodes.BIPUSH, ASMUtils.intPush(16).getOpcode());
        assertEquals(Opcodes.SIPUSH, ASMUtils.intPush(1000).getOpcode());
        assertEquals(Opcodes.LDC, ASMUtils.intPush(600_000).getOpcode());
    }

    @Test
    void parameterIndices() {
        MethodNode method = new MethodNode(0, "test", methodDescriptor(void.class, int.class, double.class, String.class), null, null);
        int[] indices = ASMUtils.parameterIndices(method);
        assertArrayEquals(new int[]{1, 2, 4}, indices);
    }

}
