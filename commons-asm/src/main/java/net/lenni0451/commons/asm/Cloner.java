package net.lenni0451.commons.asm;

import org.objectweb.asm.tree.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Cloner {

    public static ClassNode clone(final ClassNode classNode) {
        ClassNode clone = new ClassNode();
        classNode.accept(clone);
        return clone;
    }

    public static FieldNode clone(final FieldNode fieldNode) {
        FieldNode clone = new FieldNode(fieldNode.access, fieldNode.name, fieldNode.desc, fieldNode.signature, fieldNode.value);
        if (fieldNode.visibleAnnotations != null) {
            clone.visibleAnnotations = new ArrayList<>();
            fieldNode.visibleAnnotations.forEach(annotationNode -> clone.visibleAnnotations.add(clone(annotationNode)));
        }
        if (fieldNode.invisibleAnnotations != null) {
            clone.invisibleAnnotations = new ArrayList<>();
            fieldNode.invisibleAnnotations.forEach(annotationNode -> clone.invisibleAnnotations.add(clone(annotationNode)));
        }
        if (fieldNode.visibleTypeAnnotations != null) {
            clone.visibleTypeAnnotations = new ArrayList<>();
            fieldNode.visibleTypeAnnotations.forEach(typeAnnotationNode -> clone.visibleTypeAnnotations.add(clone(typeAnnotationNode)));
        }
        if (fieldNode.invisibleTypeAnnotations != null) {
            clone.invisibleTypeAnnotations = new ArrayList<>();
            fieldNode.invisibleTypeAnnotations.forEach(typeAnnotationNode -> clone.invisibleTypeAnnotations.add(clone(typeAnnotationNode)));
        }
        return clone;
    }

    public static MethodNode clone(final MethodNode methodNode) {
        MethodNode clone = new MethodNode(methodNode.access, methodNode.name, methodNode.desc, methodNode.signature, methodNode.exceptions == null ? new String[0] : methodNode.exceptions.toArray(new String[0]));
        methodNode.accept(clone);
        return clone;
    }

    public static InsnList clone(final InsnList insnList) {
        InsnList clone = new InsnList();
        Map<LabelNode, LabelNode> labels = cloneLabels(insnList);
        for (AbstractInsnNode insnNode : insnList) {
            clone.add(insnNode.clone(labels));
        }
        return clone;
    }

    public static Map<LabelNode, LabelNode> cloneLabels(final InsnList insnList) {
        Map<LabelNode, LabelNode> clone = new HashMap<>();
        for (AbstractInsnNode insnNode : insnList) {
            if (insnNode instanceof LabelNode) clone.put((LabelNode) insnNode, new LabelNode());
        }
        return clone;
    }

    public static AnnotationNode clone(final AnnotationNode annotationNode) {
        AnnotationNode clone = new AnnotationNode(annotationNode.desc);
        annotationNode.accept(clone);
        return clone;
    }

    public static TypeAnnotationNode clone(final TypeAnnotationNode typeAnnotationNode) {
        TypeAnnotationNode clone = new TypeAnnotationNode(typeAnnotationNode.typeRef, typeAnnotationNode.typePath, typeAnnotationNode.desc);
        typeAnnotationNode.accept(clone);
        return clone;
    }

}
