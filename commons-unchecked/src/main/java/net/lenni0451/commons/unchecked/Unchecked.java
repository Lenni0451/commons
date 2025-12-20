package net.lenni0451.commons.unchecked;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Unchecked {

    /**
     * Cast an object to a specific type without a warning.
     *
     * @param o   The object to cast
     * @param <T> The target type
     * @return The casted object
     * @throws ClassCastException If the object cannot be casted to the target type
     */
    public static <T> T cast(final Object o) {
        return (T) o;
    }

}
