package net.lenni0451.commons.arrays.wrapper;

public class ObjectArrayWrapper extends ArrayWrapper<Object> {

    public ObjectArrayWrapper(final Object[] array) {
        super(array);
    }

    /**
     * Get a wrapped slice of the array starting at the given index.
     *
     * @param start The index to start at
     * @return The sliced array
     */
    public ObjectArrayWrapper wrappedSlice(final int start) {
        return new ObjectArrayWrapper(this.slice(start));
    }

    /**
     * Get a wrapped slice of the array starting at the given index and ending at the given index.
     *
     * @param start The index to start at
     * @param end   The index to end at
     * @return The sliced array
     */
    public ObjectArrayWrapper wrappedSlice(final int start, final int end) {
        return new ObjectArrayWrapper(this.slice(start, end));
    }

    @Override
    public boolean isBoolean(int index) {
        if (!this.contains(index)) return false;
        return this.get(index) instanceof Boolean;
    }

    @Override
    public boolean isByte(int index) {
        if (!this.contains(index)) return false;
        return this.get(index) instanceof Byte;
    }

    @Override
    public boolean isShort(int index) {
        if (!this.contains(index)) return false;
        return this.get(index) instanceof Short;
    }

    @Override
    public boolean isChar(int index) {
        if (!this.contains(index)) return false;
        return this.get(index) instanceof Character;
    }

    @Override
    public boolean isInt(int index) {
        if (!this.contains(index)) return false;
        return this.get(index) instanceof Integer;
    }

    @Override
    public boolean isLong(int index) {
        if (!this.contains(index)) return false;
        return this.get(index) instanceof Long;
    }

    @Override
    public boolean isFloat(int index) {
        if (!this.contains(index)) return false;
        return this.get(index) instanceof Float;
    }

    @Override
    public boolean isDouble(int index) {
        if (!this.contains(index)) return false;
        return this.get(index) instanceof Double;
    }

    @Override
    public boolean getBoolean(int index, boolean def) {
        if (!this.isBoolean(index)) return def;
        return (boolean) this.get(index);
    }

    @Override
    public byte getByte(int index, byte def) {
        if (!this.isByte(index)) return def;
        return (byte) this.get(index);
    }

    @Override
    public short getShort(int index, short def) {
        if (!this.isShort(index)) return def;
        return (short) this.get(index);
    }

    @Override
    public char getChar(int index, char def) {
        if (!this.isChar(index)) return def;
        return (char) this.get(index);
    }

    @Override
    public int getInt(int index, int def) {
        if (!this.isInt(index)) return def;
        return (int) this.get(index);
    }

    @Override
    public long getLong(int index, long def) {
        if (!this.isLong(index)) return def;
        return (long) this.get(index);
    }

    @Override
    public float getFloat(int index, float def) {
        if (!this.isFloat(index)) return def;
        return (float) this.get(index);
    }

    @Override
    public double getDouble(int index, double def) {
        if (!this.isDouble(index)) return def;
        return (double) this.get(index);
    }

}
