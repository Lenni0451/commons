package net.lenni0451.commons.asm;

import lombok.experimental.UtilityClass;
import org.objectweb.asm.Opcodes;

@UtilityClass
public class Modifiers {

    /**
     * Add a flag to the given modifiers.
     *
     * @param modifiers The modifiers
     * @param flag      The flag to add
     * @return The new modifiers
     */
    public static int add(final int modifiers, final int flag) {
        return modifiers | flag;
    }

    /**
     * Remove a flag from the given modifiers.
     *
     * @param modifiers The modifiers
     * @param flag      The flag to remove
     * @return The new modifiers
     */
    public static int remove(final int modifiers, final int flag) {
        return modifiers & ~flag;
    }

    /**
     * Set a flag of the given modifiers to the given state.
     *
     * @param modifiers The modifiers
     * @param flag      The flag to set
     * @param state     The state to set the flag to
     * @return The new modifiers
     */
    public static int set(final int modifiers, final int flag, final boolean state) {
        return state ? add(modifiers, flag) : remove(modifiers, flag);
    }

    /**
     * Check if the given modifiers have the given flag.
     *
     * @param modifiers The modifiers
     * @param flag      The flag to check
     * @return If the given modifiers have the given flag
     */
    public static boolean has(final int modifiers, final int flag) {
        return (modifiers & flag) != 0;
    }

    /**
     * Check if the given modifiers have any of the given flags.
     *
     * @param modifiers The modifiers
     * @param flags     The flags to check
     * @return If the given modifiers have any of the given flags
     */
    public static boolean hasAny(final int modifiers, final int... flags) {
        for (int flag : flags) {
            if ((modifiers & flag) != 0) return true;
        }
        return false;
    }

    /**
     * Check if the given modifiers have all of the given flags.
     *
     * @param modifiers The modifiers
     * @param flags     The flags to check
     * @return If the given modifiers have all of the given flags
     */
    public static boolean hasAll(final int modifiers, final int... flags) {
        for (int flag : flags) {
            if ((modifiers & flag) == 0) return false;
        }
        return true;
    }

    /**
     * Set the access of the given modifiers.<br>
     * {@link Opcodes#ACC_PUBLIC}, {@link Opcodes#ACC_PROTECTED}, {@link Opcodes#ACC_PRIVATE} will be removed and the given access will be added.
     *
     * @param modifiers The modifiers
     * @param access    The access to set
     * @return The new modifiers
     */
    public static int setAccess(final int modifiers, final int access) {
        return add(remove(modifiers, Opcodes.ACC_PUBLIC | Opcodes.ACC_PROTECTED | Opcodes.ACC_PRIVATE), access);
    }

}
