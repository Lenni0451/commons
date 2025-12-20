package net.lenni0451.commons.unchecked;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Sneaky {

    /**
     * A fake method to declare a checked exception without actually throwing it.
     *
     * @param exceptionType The exception type
     * @param <T>           The exception type
     * @throws T The declared exception
     */
    public static <T extends Throwable> void fake(final Class<T> exceptionType) throws T {
    }

    /**
     * Throw a throwable sneakily without declaring it.
     *
     * @param t The throwable to throw
     */
    @SneakyThrows
    public static void sneakyThrow(final Throwable t) {
        throw t;
    }

}
