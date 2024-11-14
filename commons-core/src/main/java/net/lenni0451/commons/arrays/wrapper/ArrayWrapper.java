package net.lenni0451.commons.arrays.wrapper;

import net.lenni0451.commons.arrays.ArrayUtils;

import java.util.Arrays;

/**
 * A wrapper for arrays to make them easier to use and add some useful methods.
 *
 * @param <T> The type of the array
 */
public abstract class ArrayWrapper<T> {

    private T[] array;

    public ArrayWrapper(final T[] array) {
        this.array = array;
    }

    /**
     * @return The wrapped array
     */
    public T[] getArray() {
        return this.array;
    }

    /**
     * @return The length of the array
     */
    public int length() {
        return this.array.length;
    }

    /**
     * @return If the array is empty
     */
    public boolean isEmpty() {
        return this.array.length == 0;
    }

    /**
     * @return If the wrapped array is not empty
     */
    public boolean isNotEmpty() {
        return this.array.length != 0;
    }

    /**
     * Check if the index is in the array.
     *
     * @param index The index to check
     * @return If the index is in the array
     */
    public boolean contains(final int index) {
        return index >= 0 && index < this.array.length;
    }

    /**
     * Check if the value is in the array.
     *
     * @param value The value to check
     * @return If the value is in the array
     */
    public boolean contains(final T value) {
        return ArrayUtils.indexOf(this.array, value) != -1;
    }

    /**
     * Get the value at the index.
     *
     * @param index The index to get the value from
     * @return The value at the index
     * @throws ArrayIndexOutOfBoundsException If the index is not in the array
     */
    public T get(final int index) {
        return this.array[index];
    }

    /**
     * Get the value at the index or the default value if the index is not in the array.
     *
     * @param index The index to get the value from
     * @param def   The default value
     * @return The value at the index or the default value
     */
    public T get(final int index, final T def) {
        return this.contains(index) ? this.array[index] : def;
    }

    /**
     * Set the value at the index.
     *
     * @param index The index to set the value at
     * @param value The value to set
     */
    public void set(final int index, final T value) {
        this.array[index] = value;
    }

    /**
     * Add a value to the array.
     *
     * @param value The value to add
     */
    public void add(final T value) {
        this.array = ArrayUtils.add(this.array, value);
    }

    /**
     * Add multiple values to the array.
     *
     * @param values The values to add
     */
    @SafeVarargs
    public final void addAll(final T... values) {
        this.array = ArrayUtils.add(this.array, values);
    }

    /**
     * Add values from another {@link ArrayWrapper} to the array.
     *
     * @param values The {@link ArrayWrapper} to add the values from
     */
    public void addAll(final ArrayWrapper<T> values) {
        this.addAll(values.getArray());
    }

    /**
     * Remove a value from the array.
     *
     * @param index The index to remove the value from
     */
    public void remove(final int index) {
        this.array = ArrayUtils.removeAt(this.array, index);
    }

    /**
     * Remove a value from the array.<br>
     * This will only remove the first occurrence of the value.
     *
     * @param value The value to remove
     */
    public void remove(final T value) {
        this.array = ArrayUtils.remove(this.array, value);
    }

    /**
     * Get a slice of the array starting at the given index.
     *
     * @param start The index to start at
     * @return The sliced array
     */
    public T[] slice(final int start) {
        return this.slice(start, this.array.length);
    }

    /**
     * Get a slice of the array starting at the given index and ending at the given index.
     *
     * @param start The index to start at
     * @param end   The index to end at
     * @return The sliced array
     */
    public T[] slice(final int start, final int end) {
        return Arrays.copyOfRange(this.array, start, end);
    }

    /**
     * Check if the value at the index is a boolean.<br>
     * If the index is not in the array this will return false.
     *
     * @param index The index to check
     * @return If the value at the index is a boolean
     */
    public abstract boolean isBoolean(final int index);

    /**
     * Check if the value at the index is a byte.<br>
     * If the index is not in the array this will return false.
     *
     * @param index The index to check
     * @return If the value at the index is a byte
     */
    public abstract boolean isByte(final int index);

    /**
     * Check if the value at the index is a short.<br>
     * If the index is not in the array this will return false.
     *
     * @param index The index to check
     * @return If the value at the index is a short
     */
    public abstract boolean isShort(final int index);

    /**
     * Check if the value at the index is a char.<br>
     * If the index is not in the array this will return false.
     *
     * @param index The index to check
     * @return If the value at the index is a char
     */
    public abstract boolean isChar(final int index);

    /**
     * Check if the value at the index is an int.<br>
     * If the index is not in the array this will return false.
     *
     * @param index The index to check
     * @return If the value at the index is an int
     */
    public abstract boolean isInt(final int index);

    /**
     * Check if the value at the index is a long.<br>
     * If the index is not in the array this will return false.
     *
     * @param index The index to check
     * @return If the value at the index is a long
     */
    public abstract boolean isLong(final int index);

    /**
     * Check if the value at the index is a float.<br>
     * If the index is not in the array this will return false.
     *
     * @param index The index to check
     * @return If the value at the index is a float
     */
    public abstract boolean isFloat(final int index);

    /**
     * Check if the value at the index is a double.<br>
     * If the index is not in the array this will return false.
     *
     * @param index The index to check
     * @return If the value at the index is a double
     */
    public abstract boolean isDouble(final int index);

    /**
     * Get the value at the index as a boolean.<br>
     * If the index is not in the array this will return {@code false}.
     *
     * @param index The index to get the value from
     * @return The value at the index as a boolean
     */
    public boolean getBoolean(final int index) {
        return this.getBoolean(index, false);
    }

    /**
     * Get the value at the index as a byte.<br>
     * If the index is not in the array this will return {@code 0}.
     *
     * @param index The index to get the value from
     * @return The value at the index as a byte
     */
    public byte getByte(final int index) {
        return this.getByte(index, (byte) 0);
    }

    /**
     * Get the value at the index as a short.<br>
     * If the index is not in the array this will return {@code 0}.
     *
     * @param index The index to get the value from
     * @return The value at the index as a short
     */
    public short getShort(final int index) {
        return this.getShort(index, (short) 0);
    }

    /**
     * Get the value at the index as a char.<br>
     * If the index is not in the array this will return {@code 0}.
     *
     * @param index The index to get the value from
     * @return The value at the index as a char
     */
    public char getChar(final int index) {
        return this.getChar(index, (char) 0);
    }

    /**
     * Get the value at the index as an int.<br>
     * If the index is not in the array this will return {@code 0}.
     *
     * @param index The index to get the value from
     * @return The value at the index as an int
     */
    public int getInt(final int index) {
        return this.getInt(index, 0);
    }

    /**
     * Get the value at the index as a long.<br>
     * If the index is not in the array this will return {@code 0}.
     *
     * @param index The index to get the value from
     * @return The value at the index as a long
     */
    public long getLong(final int index) {
        return this.getLong(index, 0);
    }

    /**
     * Get the value at the index as a float.<br>
     * If the index is not in the array this will return {@code 0}.
     *
     * @param index The index to get the value from
     * @return The value at the index as a float
     */
    public float getFloat(final int index) {
        return this.getFloat(index, 0);
    }

    /**
     * Get the value at the index as a double.<br>
     * If the index is not in the array this will return {@code 0}.
     *
     * @param index The index to get the value from
     * @return The value at the index as a double
     */
    public double getDouble(final int index) {
        return this.getDouble(index, 0);
    }

    /**
     * Get the value at the index as a boolean or the default value if the index is not in the array.
     *
     * @param index The index to get the value from
     * @param def   The default value
     * @return The value at the index as a boolean or the default value
     */
    public abstract boolean getBoolean(final int index, final boolean def);

    /**
     * Get the value at the index as a byte or the default value if the index is not in the array.
     *
     * @param index The index to get the value from
     * @param def   The default value
     * @return The value at the index as a byte or the default value
     */
    public abstract byte getByte(final int index, final byte def);

    /**
     * Get the value at the index as a char or the default value if the index is not in the array.
     *
     * @param index The index to get the value from
     * @param def   The default value
     * @return The value at the index as a char or the default value
     */
    public abstract short getShort(final int index, final short def);

    /**
     * Get the value at the index as a char or the default value if the index is not in the array.
     *
     * @param index The index to get the value from
     * @param def   The default value
     * @return The value at the index as a char or the default value
     */
    public abstract char getChar(final int index, final char def);

    /**
     * Get the value at the index as an int or the default value if the index is not in the array.
     *
     * @param index The index to get the value from
     * @param def   The default value
     * @return The value at the index as an int or the default value
     */
    public abstract int getInt(final int index, final int def);

    /**
     * Get the value at the index as a long or the default value if the index is not in the array.
     *
     * @param index The index to get the value from
     * @param def   The default value
     * @return The value at the index as a long or the default value
     */
    public abstract long getLong(final int index, final long def);

    /**
     * Get the value at the index as a float or the default value if the index is not in the array.
     *
     * @param index The index to get the value from
     * @param def   The default value
     * @return The value at the index as a float or the default value
     */
    public abstract float getFloat(final int index, final float def);

    /**
     * Get the value at the index as a double or the default value if the index is not in the array.
     *
     * @param index The index to get the value from
     * @param def   The default value
     * @return The value at the index as a double or the default value
     */
    public abstract double getDouble(final int index, final double def);

}
