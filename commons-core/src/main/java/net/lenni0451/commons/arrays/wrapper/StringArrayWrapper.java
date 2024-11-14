package net.lenni0451.commons.arrays.wrapper;

import net.lenni0451.commons.math.NumberChecker;

import java.util.Arrays;

public class StringArrayWrapper extends ArrayWrapper<String> {

    public StringArrayWrapper(final String[] array) {
        super(array);
    }

    /**
     * Get a wrapped slice of the array starting at the given index.
     *
     * @param start The index to start at
     * @return The sliced array
     */
    public StringArrayWrapper wrappedSlice(final int start) {
        return new StringArrayWrapper(this.slice(start));
    }

    /**
     * Get a wrapped slice of the array starting at the given index and ending at the given index.
     *
     * @param start The index to start at
     * @param end   The index to end at
     * @return The sliced array
     */
    public StringArrayWrapper wrappedSlice(final int start, final int end) {
        return new StringArrayWrapper(this.slice(start, end));
    }

    @Override
    public boolean isBoolean(int index) {
        if (!this.contains(index)) return false;
        try {
            Boolean.parseBoolean(this.get(index));
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    @Override
    public boolean isByte(int index) {
        if (!this.contains(index)) return false;
        return NumberChecker.isByte(this.get(index));
    }

    @Override
    public boolean isShort(int index) {
        if (!this.contains(index)) return false;
        return NumberChecker.isShort(this.get(index));
    }

    @Override
    public boolean isChar(int index) {
        if (!this.contains(index)) return false;
        return this.get(index).length() == 1;
    }

    @Override
    public boolean isInt(int index) {
        if (!this.contains(index)) return false;
        return NumberChecker.isInteger(this.get(index));
    }

    @Override
    public boolean isLong(int index) {
        if (!this.contains(index)) return false;
        return NumberChecker.isLong(this.get(index));
    }

    @Override
    public boolean isFloat(int index) {
        if (!this.contains(index)) return false;
        return NumberChecker.isFloat(this.get(index));
    }

    @Override
    public boolean isDouble(int index) {
        if (!this.contains(index)) return false;
        return NumberChecker.isDouble(this.get(index));
    }

    @Override
    public boolean getBoolean(int index, boolean def) {
        if (!this.isBoolean(index)) return def;
        return Boolean.parseBoolean(this.get(index));
    }

    @Override
    public byte getByte(int index, byte def) {
        if (!this.isByte(index)) return def;
        return Byte.parseByte(this.get(index));
    }

    @Override
    public short getShort(int index, short def) {
        if (!this.isShort(index)) return def;
        return Short.parseShort(this.get(index));
    }

    @Override
    public char getChar(int index, char def) {
        if (!this.isChar(index)) return def;
        return this.get(index).charAt(0);
    }

    @Override
    public int getInt(int index, int def) {
        if (!this.isInt(index)) return def;
        return Integer.parseInt(this.get(index));
    }

    @Override
    public long getLong(int index, long def) {
        if (!this.isLong(index)) return def;
        return Long.parseLong(this.get(index));
    }

    @Override
    public float getFloat(int index, float def) {
        if (!this.isFloat(index)) return def;
        return Float.parseFloat(this.get(index));
    }

    @Override
    public double getDouble(int index, double def) {
        if (!this.isDouble(index)) return def;
        return Double.parseDouble(this.get(index));
    }

    /**
     * Joins the array with the given separator.
     *
     * @param separator The separator to use
     * @return The joined array
     */
    public String join(final String separator) {
        return String.join(separator, this.getArray());
    }

    /**
     * Joins the array with the given separator starting at the given index.
     *
     * @param separator The separator to use
     * @param start     The index to start at
     * @return The joined array
     */
    public String join(final String separator, final int start) {
        return String.join(separator, Arrays.copyOfRange(this.getArray(), start, this.length()));
    }

    /**
     * Joins the array with the given separator starting at the given index and ending at the given index.
     *
     * @param separator The separator to use
     * @param start     The index to start at
     * @param end       The index to end at
     * @return The joined array
     */
    public String join(final String separator, final int start, final int end) {
        return String.join(separator, Arrays.copyOfRange(this.getArray(), start, end));
    }

}
