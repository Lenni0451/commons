package net.lenni0451.commons.asm.compare;

import org.junit.jupiter.api.Test;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InstructionComparatorTest {

    @Test
    void test() {
        InstructionComparator comparator = new InstructionComparator();
        assertTrue(comparator.equals(new InsnNode(Opcodes.ICONST_4), new InsnNode(Opcodes.ICONST_4)));
        assertFalse(comparator.equals(new InsnNode(Opcodes.ICONST_4), new LdcInsnNode(4)));
        assertTrue(comparator.equals(new LdcInsnNode(4), new LdcInsnNode(4)));
        assertTrue(comparator.equals(new AbstractInsnNode[]{new IntInsnNode(4, 1)}, new AbstractInsnNode[]{new IntInsnNode(4, 1)}));
    }

}
