package net.lenni0451.commons.asm.compare;

import org.objectweb.asm.tree.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A comparator for {@link AbstractInsnNode} which can be used to compare two instruction lists.
 */
public class InstructionComparator {

    private final List<Integer> ignoredInstructionTypes;

    /**
     * Creates a new instruction comparator which ignores frames and line numbers.
     */
    public InstructionComparator() {
        //Frames and line numbers are not important for the actual code flow
        //Line numbers especially can be different with exactly the same code
        this(AbstractInsnNode.FRAME, AbstractInsnNode.LINE);
    }

    public InstructionComparator(final int... ignoredInstructionTypes) {
        this.ignoredInstructionTypes = Arrays.stream(ignoredInstructionTypes).boxed().collect(Collectors.toList());
    }

    /**
     * Check if two {@link InsnList} are equal.
     *
     * @param insnList1 The first instruction list
     * @param insnList2 The second instruction list
     * @return If the two instruction lists are equal
     */
    public boolean equals(final InsnList insnList1, final InsnList insnList2) {
        return this.equals(insnList1.toArray(), insnList2.toArray());
    }

    /**
     * Check if two lists of {@link AbstractInsnNode} are equal.
     *
     * @param insnList1 The first list of instructions
     * @param insnList2 The second list of instructions
     * @return If the two lists are equal
     */
    public boolean equals(final List<? extends AbstractInsnNode> insnList1, final List<? extends AbstractInsnNode> insnList2) {
        return this.equals(insnList1.toArray(new AbstractInsnNode[0]), insnList2.toArray(new AbstractInsnNode[0]));
    }

    /**
     * Compare two arrays of {@link AbstractInsnNode}.
     *
     * @param insns1 The first array of instructions
     * @param insns2 The second array of instructions
     * @return If the two arrays are equal
     */
    public boolean equals(AbstractInsnNode[] insns1, AbstractInsnNode[] insns2) {
        if (insns1 == insns2) return true;
        if (insns1 == null || insns2 == null) return false;
        insns1 = this.filterIgnoredInstructions(insns1);
        insns2 = this.filterIgnoredInstructions(insns2);
        if (insns1.length != insns2.length) return false;

        for (int i = 0; i < insns1.length; i++) {
            if (!this.equals(insns1[i], insns2[i])) return false;
        }
        return true;
    }

    /**
     * Compare two {@link AbstractInsnNode}.<br>
     * This method ignores the {@link AbstractInsnNode#getNext()} and {@link AbstractInsnNode#getPrevious()} instructions.
     *
     * @param insn1 The first instruction
     * @param insn2 The second instruction
     * @return If the two instructions are equal
     */
    public boolean equals(final AbstractInsnNode insn1, final AbstractInsnNode insn2) {
        if (insn1 == insn2) return true;
        if (insn1 == null || insn2 == null) return false;
        if (insn1.getOpcode() != insn2.getOpcode()) return false;
        if (insn1.getType() != insn2.getType()) return false;

        switch (insn1.getType()) {
            case AbstractInsnNode.INSN:
                //The opcode is the only thing that matters
                return true;
            case AbstractInsnNode.INT_INSN:
                IntInsnNode intInsn1 = (IntInsnNode) insn1;
                IntInsnNode intInsn2 = (IntInsnNode) insn2;
                return intInsn1.operand == intInsn2.operand;
            case AbstractInsnNode.VAR_INSN:
                VarInsnNode varInsn1 = (VarInsnNode) insn1;
                VarInsnNode varInsn2 = (VarInsnNode) insn2;
                return varInsn1.var == varInsn2.var;
            case AbstractInsnNode.TYPE_INSN:
                TypeInsnNode typeInsn1 = (TypeInsnNode) insn1;
                TypeInsnNode typeInsn2 = (TypeInsnNode) insn2;
                return Objects.equals(typeInsn1.desc, typeInsn2.desc);
            case AbstractInsnNode.FIELD_INSN:
                FieldInsnNode fieldInsn1 = (FieldInsnNode) insn1;
                FieldInsnNode fieldInsn2 = (FieldInsnNode) insn2;
                return Objects.equals(fieldInsn1.owner, fieldInsn2.owner)
                        && Objects.equals(fieldInsn1.name, fieldInsn2.name)
                        && Objects.equals(fieldInsn1.desc, fieldInsn2.desc);
            case AbstractInsnNode.METHOD_INSN:
                MethodInsnNode methodInsn1 = (MethodInsnNode) insn1;
                MethodInsnNode methodInsn2 = (MethodInsnNode) insn2;
                return Objects.equals(methodInsn1.owner, methodInsn2.owner)
                        && Objects.equals(methodInsn1.name, methodInsn2.name)
                        && Objects.equals(methodInsn1.desc, methodInsn2.desc)
                        && methodInsn1.itf == methodInsn2.itf;
            case AbstractInsnNode.INVOKE_DYNAMIC_INSN:
                InvokeDynamicInsnNode invokeDynamicInsn1 = (InvokeDynamicInsnNode) insn1;
                InvokeDynamicInsnNode invokeDynamicInsn2 = (InvokeDynamicInsnNode) insn2;
                return Objects.equals(invokeDynamicInsn1.name, invokeDynamicInsn2.name)
                        && Objects.equals(invokeDynamicInsn1.desc, invokeDynamicInsn2.desc)
                        && Objects.equals(invokeDynamicInsn1.bsm, invokeDynamicInsn2.bsm)
                        && this.listEquals(Arrays.asList(invokeDynamicInsn1.bsmArgs), Arrays.asList(invokeDynamicInsn2.bsmArgs));
            case AbstractInsnNode.JUMP_INSN:
                JumpInsnNode jumpInsn1 = (JumpInsnNode) insn1;
                JumpInsnNode jumpInsn2 = (JumpInsnNode) insn2;
                return this.equals(jumpInsn1.label, jumpInsn2.label);
            case AbstractInsnNode.LABEL:
                //Label instances are not equal, so we can't compare them. It should be enough to know that they exist in both lists
                return true;
            case AbstractInsnNode.LDC_INSN:
                LdcInsnNode ldcInsn1 = (LdcInsnNode) insn1;
                LdcInsnNode ldcInsn2 = (LdcInsnNode) insn2;
                return Objects.equals(ldcInsn1.cst, ldcInsn2.cst);
            case AbstractInsnNode.IINC_INSN:
                IincInsnNode iincInsn1 = (IincInsnNode) insn1;
                IincInsnNode iincInsn2 = (IincInsnNode) insn2;
                return iincInsn1.var == iincInsn2.var
                        && iincInsn1.incr == iincInsn2.incr;
            case AbstractInsnNode.TABLESWITCH_INSN:
                TableSwitchInsnNode tableSwitchInsn1 = (TableSwitchInsnNode) insn1;
                TableSwitchInsnNode tableSwitchInsn2 = (TableSwitchInsnNode) insn2;
                return tableSwitchInsn1.min == tableSwitchInsn2.min
                        && tableSwitchInsn1.max == tableSwitchInsn2.max
                        && this.equals(tableSwitchInsn1.dflt, tableSwitchInsn2.dflt)
                        && this.equals(tableSwitchInsn1.labels, tableSwitchInsn2.labels);
            case AbstractInsnNode.LOOKUPSWITCH_INSN:
                LookupSwitchInsnNode lookupSwitchInsn1 = (LookupSwitchInsnNode) insn1;
                LookupSwitchInsnNode lookupSwitchInsn2 = (LookupSwitchInsnNode) insn2;
                return this.equals(lookupSwitchInsn1.dflt, lookupSwitchInsn2.dflt)
                        && Objects.equals(lookupSwitchInsn1.keys, lookupSwitchInsn2.keys)
                        && this.equals(lookupSwitchInsn1.labels, lookupSwitchInsn2.labels);
            case AbstractInsnNode.MULTIANEWARRAY_INSN:
                MultiANewArrayInsnNode multiANewArrayInsn1 = (MultiANewArrayInsnNode) insn1;
                MultiANewArrayInsnNode multiANewArrayInsn2 = (MultiANewArrayInsnNode) insn2;
                return Objects.equals(multiANewArrayInsn1.desc, multiANewArrayInsn2.desc)
                        && multiANewArrayInsn1.dims == multiANewArrayInsn2.dims;
            case AbstractInsnNode.FRAME:
                FrameNode frameNode1 = (FrameNode) insn1;
                FrameNode frameNode2 = (FrameNode) insn2;
                return frameNode1.type == frameNode2.type
                        && this.listEquals(frameNode1.local, frameNode2.local)
                        && this.listEquals(frameNode1.stack, frameNode2.stack);
            case AbstractInsnNode.LINE:
                LineNumberNode lineNumberNode1 = (LineNumberNode) insn1;
                LineNumberNode lineNumberNode2 = (LineNumberNode) insn2;
                return lineNumberNode1.line == lineNumberNode2.line
                        && this.equals(lineNumberNode1.start, lineNumberNode2.start);
            default:
                throw new UnsupportedOperationException("Unknown AbstractInsnNode type: " + insn1.getType());
        }
    }

    private boolean listEquals(final List<Object> list1, final List<Object> list2) {
        if (list1 == list2) return true;
        if (list1 == null || list2 == null) return false;
        if (list1.size() != list2.size()) return false;

        for (int i = 0; i < list1.size(); i++) {
            Object obj1 = list1.get(i);
            Object obj2 = list2.get(i);
            if (obj1 == obj2) continue;
            if (obj1 == null || obj2 == null) return false;

            if (obj1 instanceof AbstractInsnNode && obj2 instanceof AbstractInsnNode) {
                if (!this.equals((AbstractInsnNode) obj1, (AbstractInsnNode) obj2)) return false;
            } else {
                if (!Objects.equals(obj1, obj2)) return false;
            }
        }
        return true;
    }

    private AbstractInsnNode[] filterIgnoredInstructions(final AbstractInsnNode[] insns) {
        return Arrays.stream(insns).filter(insn -> !this.ignoredInstructionTypes.contains(insn.getType())).toArray(AbstractInsnNode[]::new);
    }

}
