package net.lenni0451.commons.asm;

import org.objectweb.asm.Opcodes;

public class Modifiers {

    public static int add(final int modifiers, final int flag) {
        return modifiers | flag;
    }

    public static int remove(final int modifiers, final int flag) {
        return modifiers & ~flag;
    }

    public static int set(final int modifiers, final int flag, final boolean state) {
        return state ? add(modifiers, flag) : remove(modifiers, flag);
    }

    public static boolean has(final int modifiers, final int flag) {
        return (modifiers & flag) != 0;
    }

    public static boolean hasAny(final int modifiers, final int... flags) {
        for (int flag : flags) {
            if ((modifiers & flag) != 0) return true;
        }
        return false;
    }

    public static boolean hasAll(final int modifiers, final int... flags) {
        for (int flag : flags) {
            if ((modifiers & flag) == 0) return false;
        }
        return true;
    }

    public static int setAccess(final int modifiers, final int access) {
        return add(remove(modifiers, Opcodes.ACC_PUBLIC | Opcodes.ACC_PROTECTED | Opcodes.ACC_PRIVATE), access);
    }

}
