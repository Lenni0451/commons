package net.lenni0451.commons.asm.analysis;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.analysis.Value;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Set;

/**
 * Value representation holding the Type, the literal resolved Object value (if known), and a Set of origin instructions.
 */
@Getter
@EqualsAndHashCode
public class TrackedValue implements Value {

    private final Type type;
    /**
     * The resolved literal value (Integer, Float, String, ...).<br>
     * {@code null} if the value is unknown or cannot be resolved at compile/analysis time.
     *
     * @see LdcInsnNode#cst
     */
    @Nullable
    private final Object value;
    /**
     * The set of instructions that contributed to this value.<br>
     * Will contain multiple instructions if control flow merges (e.g. an IF/ELSE).
     */
    private final Set<AbstractInsnNode> origins;

    public TrackedValue(final Type type, final Object value, final AbstractInsnNode origin) {
        this(type, value, origin == null ? Collections.emptySet() : Collections.singleton(origin));
    }

    public TrackedValue(final Type type, final @Nullable Object value, @Nullable final Set<AbstractInsnNode> origins) {
        this.type = type;
        this.value = value;
        this.origins = origins == null ? Collections.emptySet() : Collections.unmodifiableSet(origins);
    }

    @Override
    public int getSize() {
        return this.type == Type.LONG_TYPE || this.type == Type.DOUBLE_TYPE ? 2 : 1;
    }

    @Override
    public String toString() {
        String type = this.type == null ? "null" : this.type.getDescriptor();
        return type + (this.value != null ? "(" + this.value + ")" : "");
    }

}
