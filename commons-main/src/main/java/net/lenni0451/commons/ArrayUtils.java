package net.lenni0451.commons;

import java.lang.reflect.Array;

public class ArrayUtils {

    /*
     * |----------------------------------------------------------------|
     * |                             byte[]                             |
     * |----------------------------------------------------------------|
     */

    /**
     * Get the index of a byte in an array.
     *
     * @param array The array to search in
     * @param b     The byte to search for
     * @return The index of the byte or -1 if not found
     */
    public static int indexOf(final byte[] array, final byte b) {
        for (int x = 0; x < array.length; x++) {
            if (array[x] == b) return x;
        }
        return -1;
    }

    /**
     * Get the index of a byte in an array starting from the end.
     *
     * @param array The array to search in
     * @param b     The byte to search for
     * @return The index of the byte or -1 if not found
     */
    public static int indexOfLast(final byte[] array, final byte b) {
        for (int x = array.length - 1; x >= 0; x--) {
            if (array[x] == b) return x;
        }
        return -1;
    }

    /**
     * Get the index of a byte array in an array.
     *
     * @param array The array to search in
     * @param other The byte array to search for
     * @return The index of the byte array or -1 if not found
     */
    public static int indexOf(final byte[] array, final byte[] other) {
        for (int x = 0; x < array.length; x++) {
            if (array[x] == other[0]) {
                boolean found = true;
                for (int y = 1; y < other.length; y++) {
                    if (y + x >= array.length) return -1;
                    if (array[x + y] != other[y]) {
                        found = false;
                        break;
                    }
                }
                if (found) return x;
            }
        }
        return -1;
    }

    /**
     * Add a byte to the end of an array.
     *
     * @param array The array to add the byte to
     * @param b     The byte to add
     * @return The new array
     */
    public static byte[] add(final byte[] array, final byte b) {
        return add(array, array.length, b);
    }

    /**
     * Add a byte to the start of an array.
     *
     * @param array The array to add the byte to
     * @param b     The byte to add
     * @return The new array
     */
    public static byte[] prepend(final byte[] array, final byte b) {
        return add(array, 0, b);
    }

    /**
     * Add a byte to an array at a specific index.
     *
     * @param array The array to add the byte to
     * @param index The index to add the byte at
     * @param b     The byte to add
     * @return The new array
     */
    public static byte[] add(final byte[] array, final int index, final byte b) {
        byte[] newArray = new byte[array.length + 1];
        System.arraycopy(array, 0, newArray, 0, index);
        newArray[index] = b;
        System.arraycopy(array, index, newArray, index + 1, array.length - index);
        return newArray;
    }

    /**
     * Add multiple bytes to the end of an array.
     *
     * @param array The array to add the bytes to
     * @param bytes The bytes to add
     * @return The new array
     */
    public static byte[] add(final byte[] array, final byte... bytes) {
        return add(array, array.length, bytes);
    }

    /**
     * Add multiple bytes to the start of an array.
     *
     * @param array The array to add the bytes to
     * @param bytes The bytes to add
     * @return The new array
     */
    public static byte[] prepend(final byte[] array, final byte... bytes) {
        return add(array, 0, bytes);
    }

    /**
     * Add multiple bytes to an array at a specific index.
     *
     * @param array The array to add the bytes to
     * @param index The index to add the bytes at
     * @param bytes The bytes to add
     * @return The new array
     */
    public static byte[] add(final byte[] array, final int index, final byte... bytes) {
        byte[] newArray = new byte[array.length + bytes.length];
        System.arraycopy(array, 0, newArray, 0, index);
        System.arraycopy(bytes, 0, newArray, index, bytes.length);
        System.arraycopy(array, index, newArray, index + bytes.length, array.length - index);
        return newArray;
    }

    /**
     * Add multiple byte arrays to the end of an array.
     *
     * @param array The array to add the other arrays to
     * @param other The arrays to add
     * @return The new array
     */
    public static byte[] add(final byte[] array, final byte[]... other) {
        return add(array, array.length, other);
    }

    /**
     * Add multiple byte arrays to the start of an array.
     *
     * @param array The array to add the other arrays to
     * @param other The arrays to add
     * @return The new array
     */
    public static byte[] prepend(final byte[] array, final byte[]... other) {
        return add(array, 0, other);
    }

    /**
     * Add multiple byte arrays to an array at a specific index.
     *
     * @param array The array to add the other arrays to
     * @param index The index to add the other arrays at
     * @param other The arrays to add
     * @return The new array
     */
    public static byte[] add(final byte[] array, final int index, final byte[]... other) {
        int length = array.length;
        for (byte[] o : other) length += o.length;
        byte[] newArray = new byte[length];
        System.arraycopy(array, 0, newArray, 0, index);
        int i = index;
        for (byte[] o : other) {
            System.arraycopy(o, 0, newArray, i, o.length);
            i += o.length;
        }
        System.arraycopy(array, index, newArray, i, array.length - index);
        return newArray;
    }

    /**
     * Remove a given byte from an array.<br>
     * <b>Note:</b> This only removes the first occurrence of the byte.<br>
     * <b>Note:</b> If the byte is not found, the original array is returned.
     *
     * @param array The array to remove the byte from
     * @param b     The byte to remove
     * @return The new array
     */
    public static byte[] remove(final byte[] array, final byte b) {
        int index = indexOf(array, b);
        if (index == -1) return array;
        return removeAt(array, index);
    }

    /**
     * Remove the first byte from an array.
     *
     * @param array The array to remove the byte from
     * @return The new array
     */
    public static byte[] removeFirst(final byte[] array) {
        return removeAt(array, 0);
    }

    /**
     * Remove the last byte from an array.
     *
     * @param array The array to remove the byte from
     * @return The new array
     */
    public static byte[] removeLast(final byte[] array) {
        return removeAt(array, array.length - 1);
    }

    /**
     * Remove a byte from an array at a specific index.
     *
     * @param array The array to remove the byte from
     * @param index The index to remove the byte at
     * @return The new array
     */
    public static byte[] removeAt(final byte[] array, final int index) {
        return removeAt(array, index, 1);
    }

    /**
     * Remove multiple bytes from an array at a specific index.
     *
     * @param array  The array to remove the bytes from
     * @param index  The index to remove the bytes at
     * @param length The number of bytes to remove
     * @return The new array
     */
    public static byte[] removeAt(final byte[] array, final int index, final int length) {
        byte[] newArray = new byte[array.length - length];
        System.arraycopy(array, 0, newArray, 0, index);
        System.arraycopy(array, index + length, newArray, index, array.length - index - length);
        return newArray;
    }

    /*
     * |----------------------------------------------------------------|
     * |                            short[]                             |
     * |----------------------------------------------------------------|
     */

    /**
     * Get the index of a short in an array.
     *
     * @param array The array to search in
     * @param s     The short to search for
     * @return The index of the short or -1 if not found
     */
    public static int indexOf(final short[] array, final short s) {
        for (int x = 0; x < array.length; x++) {
            if (array[x] == s) return x;
        }
        return -1;
    }

    /**
     * Get the index of a short in an array starting from the end.
     *
     * @param array The array to search in
     * @param s     The short to search for
     * @return The index of the short or -1 if not found
     */
    public static int indexOfLast(final short[] array, final short s) {
        for (int x = array.length - 1; x >= 0; x--) {
            if (array[x] == s) return x;
        }
        return -1;
    }

    /**
     * Get the index of a short array in an array.
     *
     * @param array The array to search in
     * @param other The short array to search for
     * @return The index of the short array or -1 if not found
     */
    public static int indexOf(final short[] array, final short[] other) {
        for (int x = 0; x < array.length; x++) {
            if (array[x] == other[0]) {
                boolean found = true;
                for (int y = 1; y < other.length; y++) {
                    if (y + x >= array.length) return -1;
                    if (array[x + y] != other[y]) {
                        found = false;
                        break;
                    }
                }
                if (found) return x;
            }
        }
        return -1;
    }

    /**
     * Add a short to the end of an array.
     *
     * @param array The array to add the short to
     * @param s     The short to add
     * @return The new array
     */
    public static short[] add(final short[] array, final short s) {
        return add(array, array.length, s);
    }

    /**
     * Add a short to the start of an array.
     *
     * @param array The array to add the short to
     * @param s     The short to add
     * @return The new array
     */
    public static short[] prepend(final short[] array, final short s) {
        return add(array, 0, s);
    }

    /**
     * Add a short to an array at a specific index.
     *
     * @param array The array to add the short to
     * @param index The index to add the short at
     * @param s     The short to add
     * @return The new array
     */
    public static short[] add(final short[] array, final int index, final short s) {
        short[] newArray = new short[array.length + 1];
        System.arraycopy(array, 0, newArray, 0, index);
        newArray[index] = s;
        System.arraycopy(array, index, newArray, index + 1, array.length - index);
        return newArray;
    }

    /**
     * Add multiple shorts to the end of an array.
     *
     * @param array  The array to add the shorts to
     * @param shorts The shorts to add
     * @return The new array
     */
    public static short[] add(final short[] array, final short... shorts) {
        return add(array, array.length, shorts);
    }

    /**
     * Add multiple shorts to the start of an array.
     *
     * @param array  The array to add the shorts to
     * @param shorts The shorts to add
     * @return The new array
     */
    public static short[] prepend(final short[] array, final short... shorts) {
        return add(array, 0, shorts);
    }

    /**
     * Add multiple shorts to an array at a specific index.
     *
     * @param array  The array to add the shorts to
     * @param index  The index to add the shorts at
     * @param shorts The shorts to add
     * @return The new array
     */
    public static short[] add(final short[] array, final int index, final short... shorts) {
        short[] newArray = new short[array.length + shorts.length];
        System.arraycopy(array, 0, newArray, 0, index);
        System.arraycopy(shorts, 0, newArray, index, shorts.length);
        System.arraycopy(array, index, newArray, index + shorts.length, array.length - index);
        return newArray;
    }

    /**
     * Add multiple short arrays to the end of an array.
     *
     * @param array The array to add the other arrays to
     * @param other The arrays to add
     * @return The new array
     */
    public static short[] add(final short[] array, final short[]... other) {
        return add(array, array.length, other);
    }

    /**
     * Add multiple short arrays to the start of an array.
     *
     * @param array The array to add the other arrays to
     * @param other The arrays to add
     * @return The new array
     */
    public static short[] prepend(final short[] array, final short[]... other) {
        return add(array, 0, other);
    }

    /**
     * Add multiple short arrays to an array at a specific index.
     *
     * @param array The array to add the other arrays to
     * @param index The index to add the other arrays at
     * @param other The arrays to add
     * @return The new array
     */
    public static short[] add(final short[] array, final int index, final short[]... other) {
        int length = array.length;
        for (short[] o : other) length += o.length;
        short[] newArray = new short[length];
        System.arraycopy(array, 0, newArray, 0, index);
        int i = index;
        for (short[] o : other) {
            System.arraycopy(o, 0, newArray, i, o.length);
            i += o.length;
        }
        System.arraycopy(array, index, newArray, i, array.length - index);
        return newArray;
    }

    /**
     * Remove a given short from an array.<br>
     * <b>Note:</b> This only removes the first occurrence of the short.<br>
     * <b>Note:</b> If the short is not found, the original array is returned.
     *
     * @param array The array to remove the short from
     * @param s     The short to remove
     * @return The new array
     */
    public static short[] remove(final short[] array, final short s) {
        int index = indexOf(array, s);
        if (index == -1) return array;
        return removeAt(array, index);
    }

    /**
     * Remove the first short from an array.
     *
     * @param array The array to remove the short from
     * @return The new array
     */
    public static short[] removeFirst(final short[] array) {
        return removeAt(array, 0);
    }

    /**
     * Remove the last short from an array.
     *
     * @param array The array to remove the short from
     * @return The new array
     */
    public static short[] removeLast(final short[] array) {
        return removeAt(array, array.length - 1);
    }

    /**
     * Remove a short from an array at a specific index.
     *
     * @param array The array to remove the short from
     * @param index The index to remove the short at
     * @return The new array
     */
    public static short[] removeAt(final short[] array, final int index) {
        return removeAt(array, index, 1);
    }

    /**
     * Remove multiple shorts from an array at a specific index.
     *
     * @param array  The array to remove the shorts from
     * @param index  The index to remove the shorts at
     * @param length The number of shorts to remove
     * @return The new array
     */
    public static short[] removeAt(final short[] array, final int index, final int length) {
        short[] newArray = new short[array.length - length];
        System.arraycopy(array, 0, newArray, 0, index);
        System.arraycopy(array, index + length, newArray, index, array.length - index - length);
        return newArray;
    }

    /*
     * |----------------------------------------------------------------|
     * |                             char[]                             |
     * |----------------------------------------------------------------|
     */

    /**
     * Get the index of a char in an array.
     *
     * @param array The array to search in
     * @param c     The char to search for
     * @return The index of the char or -1 if not found
     */
    public static int indexOf(final char[] array, final char c) {
        for (int x = 0; x < array.length; x++) {
            if (array[x] == c) return x;
        }
        return -1;
    }

    /**
     * Get the index of a char in an array starting from the end.
     *
     * @param array The array to search in
     * @param c     The char to search for
     * @return The index of the char or -1 if not found
     */
    public static int indexOfLast(final char[] array, final char c) {
        for (int x = array.length - 1; x >= 0; x--) {
            if (array[x] == c) return x;
        }
        return -1;
    }

    /**
     * Get the index of a char array in an array.
     *
     * @param array The array to search in
     * @param other The char array to search for
     * @return The index of the char array or -1 if not found
     */
    public static int indexOf(final char[] array, final char[] other) {
        for (int x = 0; x < array.length; x++) {
            if (array[x] == other[0]) {
                boolean found = true;
                for (int y = 1; y < other.length; y++) {
                    if (y + x >= array.length) return -1;
                    if (array[x + y] != other[y]) {
                        found = false;
                        break;
                    }
                }
                if (found) return x;
            }
        }
        return -1;
    }

    /**
     * Add a char to the end of an array.
     *
     * @param array The array to add the char to
     * @param c     The char to add
     * @return The new array
     */
    public static char[] add(final char[] array, final char c) {
        return add(array, array.length, c);
    }

    /**
     * Add a char to the start of an array.
     *
     * @param array The array to add the char to
     * @param c     The char to add
     * @return The new array
     */
    public static char[] prepend(final char[] array, final char c) {
        return add(array, 0, c);
    }

    /**
     * Add a char to an array at a specific index.
     *
     * @param array The array to add the char to
     * @param index The index to add the char at
     * @param c     The char to add
     * @return The new array
     */
    public static char[] add(final char[] array, final int index, final char c) {
        char[] newArray = new char[array.length + 1];
        System.arraycopy(array, 0, newArray, 0, index);
        newArray[index] = c;
        System.arraycopy(array, index, newArray, index + 1, array.length - index);
        return newArray;
    }

    /**
     * Add multiple chars to the end of an array.
     *
     * @param array The array to add the chars to
     * @param chars The chars to add
     * @return The new array
     */
    public static char[] add(final char[] array, final char... chars) {
        return add(array, array.length, chars);
    }

    /**
     * Add multiple chars to the start of an array.
     *
     * @param array The array to add the chars to
     * @param chars The chars to add
     * @return The new array
     */
    public static char[] prepend(final char[] array, final char... chars) {
        return add(array, 0, chars);
    }

    /**
     * Add multiple chars to an array at a specific index.
     *
     * @param array The array to add the chars to
     * @param index The index to add the chars at
     * @param chars The chars to add
     * @return The new array
     */
    public static char[] add(final char[] array, final int index, final char... chars) {
        char[] newArray = new char[array.length + chars.length];
        System.arraycopy(array, 0, newArray, 0, index);
        System.arraycopy(chars, 0, newArray, index, chars.length);
        System.arraycopy(array, index, newArray, index + chars.length, array.length - index);
        return newArray;
    }

    /**
     * Add multiple char arrays to the end of an array.
     *
     * @param array The array to add the other arrays to
     * @param other The arrays to add
     * @return The new array
     */
    public static char[] add(final char[] array, final char[]... other) {
        return add(array, array.length, other);
    }

    /**
     * Add multiple char arrays to the start of an array.
     *
     * @param array The array to add the other arrays to
     * @param other The arrays to add
     * @return The new array
     */
    public static char[] prepend(final char[] array, final char[]... other) {
        return add(array, 0, other);
    }

    /**
     * Add multiple char arrays to an array at a specific index.
     *
     * @param array The array to add the other arrays to
     * @param index The index to add the other arrays at
     * @param other The arrays to add
     * @return The new array
     */
    public static char[] add(final char[] array, final int index, final char[]... other) {
        int length = array.length;
        for (char[] o : other) length += o.length;
        char[] newArray = new char[length];
        System.arraycopy(array, 0, newArray, 0, index);
        int i = index;
        for (char[] o : other) {
            System.arraycopy(o, 0, newArray, i, o.length);
            i += o.length;
        }
        System.arraycopy(array, index, newArray, i, array.length - index);
        return newArray;
    }

    /**
     * Remove a given char from an array.<br>
     * <b>Note:</b> This only removes the first occurrence of the char.<br>
     * <b>Note:</b> If the char is not found, the original array is returned.
     *
     * @param array The array to remove the char from
     * @param c     The char to remove
     * @return The new array
     */
    public static char[] remove(final char[] array, final char c) {
        int index = indexOf(array, c);
        if (index == -1) return array;
        return removeAt(array, index);
    }

    /**
     * Remove the first char from an array.
     *
     * @param array The array to remove the char from
     * @return The new array
     */
    public static char[] removeFirst(final char[] array) {
        return removeAt(array, 0);
    }

    /**
     * Remove the last char from an array.
     *
     * @param array The array to remove the char from
     * @return The new array
     */
    public static char[] removeLast(final char[] array) {
        return removeAt(array, array.length - 1);
    }

    /**
     * Remove a char from an array at a specific index.
     *
     * @param array The array to remove the char from
     * @param index The index to remove the char at
     * @return The new array
     */
    public static char[] removeAt(final char[] array, final int index) {
        return removeAt(array, index, 1);
    }

    /**
     * Remove multiple chars from an array at a specific index.
     *
     * @param array  The array to remove the chars from
     * @param index  The index to remove the chars at
     * @param length The number of chars to remove
     * @return The new array
     */
    public static char[] removeAt(final char[] array, final int index, final int length) {
        char[] newArray = new char[array.length - length];
        System.arraycopy(array, 0, newArray, 0, index);
        System.arraycopy(array, index + length, newArray, index, array.length - index - length);
        return newArray;
    }

    /*
     * |----------------------------------------------------------------|
     * |                              int[]                             |
     * |----------------------------------------------------------------|
     */

    /**
     * Get the index of an int in an array.
     *
     * @param array The array to search in
     * @param i     The int to search for
     * @return The index of the int or -1 if not found
     */
    public static int indexOf(final int[] array, final int i) {
        for (int x = 0; x < array.length; x++) {
            if (array[x] == i) return x;
        }
        return -1;
    }

    /**
     * Get the index of an int in an array starting from the end.
     *
     * @param array The array to search in
     * @param i     The int to search for
     * @return The index of the int or -1 if not found
     */
    public static int indexOfLast(final int[] array, final int i) {
        for (int x = array.length - 1; x >= 0; x--) {
            if (array[x] == i) return x;
        }
        return -1;
    }

    /**
     * Get the index of an int array in an array.
     *
     * @param array The array to search in
     * @param other The int array to search for
     * @return The index of the int array or -1 if not found
     */
    public static int indexOf(final int[] array, final int[] other) {
        for (int x = 0; x < array.length; x++) {
            if (array[x] == other[0]) {
                boolean found = true;
                for (int y = 1; y < other.length; y++) {
                    if (y + x >= array.length) return -1;
                    if (array[x + y] != other[y]) {
                        found = false;
                        break;
                    }
                }
                if (found) return x;
            }
        }
        return -1;
    }

    /**
     * Add an int to the end of an array.
     *
     * @param array The array to add the int to
     * @param i     The int to add
     * @return The new array
     */
    public static int[] add(final int[] array, final int i) {
        return add(array, array.length, i);
    }

    /**
     * Add an int to the start of an array.
     *
     * @param array The array to add the int to
     * @param i     The int to add
     * @return The new array
     */
    public static int[] prepend(final int[] array, final int i) {
        return add(array, 0, i);
    }

    /**
     * Add an int to an array at a specific index.
     *
     * @param array The array to add the int to
     * @param index The index to add the int at
     * @param i     The int to add
     * @return The new array
     */
    public static int[] add(final int[] array, final int index, final int i) {
        int[] newArray = new int[array.length + 1];
        System.arraycopy(array, 0, newArray, 0, index);
        newArray[index] = i;
        System.arraycopy(array, index, newArray, index + 1, array.length - index);
        return newArray;
    }

    /**
     * Add multiple ints to the end of an array.
     *
     * @param array The array to add the ints to
     * @param ints  The ints to add
     * @return The new array
     */
    public static int[] add(final int[] array, final int... ints) {
        return add(array, array.length, ints);
    }

    /**
     * Add multiple ints to the start of an array.
     *
     * @param array The array to add the ints to
     * @param ints  The ints to add
     * @return The new array
     */
    public static int[] prepend(final int[] array, final int... ints) {
        return add(array, 0, ints);
    }

    /**
     * Add multiple ints to an array at a specific index.
     *
     * @param array The array to add the ints to
     * @param index The index to add the ints at
     * @param ints  The ints to add
     * @return The new array
     */
    public static int[] add(final int[] array, final int index, final int... ints) {
        int[] newArray = new int[array.length + ints.length];
        System.arraycopy(array, 0, newArray, 0, index);
        System.arraycopy(ints, 0, newArray, index, ints.length);
        System.arraycopy(array, index, newArray, index + ints.length, array.length - index);
        return newArray;
    }

    /**
     * Add multiple int arrays to the end of an array.
     *
     * @param array The array to add the other arrays to
     * @param other The arrays to add
     * @return The new array
     */
    public static int[] add(final int[] array, final int[]... other) {
        return add(array, array.length, other);
    }

    /**
     * Add multiple int arrays to the start of an array.
     *
     * @param array The array to add the other arrays to
     * @param other The arrays to add
     * @return The new array
     */
    public static int[] prepend(final int[] array, final int[]... other) {
        return add(array, 0, other);
    }

    /**
     * Add multiple int arrays to an array at a specific index.
     *
     * @param array The array to add the other arrays to
     * @param index The index to add the other arrays at
     * @param other The arrays to add
     * @return The new array
     */
    public static int[] add(final int[] array, final int index, final int[]... other) {
        int length = array.length;
        for (int[] o : other) length += o.length;
        int[] newArray = new int[length];
        System.arraycopy(array, 0, newArray, 0, index);
        int i = index;
        for (int[] o : other) {
            System.arraycopy(o, 0, newArray, i, o.length);
            i += o.length;
        }
        System.arraycopy(array, index, newArray, i, array.length - index);
        return newArray;
    }

    /**
     * Remove a given int from an array.<br>
     * <b>Note:</b> This only removes the first occurrence of the int.<br>
     * <b>Note:</b> If the int is not found, the original array is returned.
     *
     * @param array The array to remove the int from
     * @param i     The int to remove
     * @return The new array
     */
    public static int[] remove(final int[] array, final int i) {
        int index = indexOf(array, i);
        if (index == -1) return array;
        return removeAt(array, index);
    }

    /**
     * Remove the first int from an array.
     *
     * @param array The array to remove the int from
     * @return The new array
     */
    public static int[] removeFirst(final int[] array) {
        return removeAt(array, 0);
    }

    /**
     * Remove the last int from an array.
     *
     * @param array The array to remove the int from
     * @return The new array
     */
    public static int[] removeLast(final int[] array) {
        return removeAt(array, array.length - 1);
    }

    /**
     * Remove an int from an array at a specific index.
     *
     * @param array The array to remove the int from
     * @param index The index to remove the int at
     * @return The new array
     */
    public static int[] removeAt(final int[] array, final int index) {
        return removeAt(array, index, 1);
    }

    /**
     * Remove multiple ints from an array at a specific index.
     *
     * @param array  The array to remove the ints from
     * @param index  The index to remove the ints at
     * @param length The number of ints to remove
     * @return The new array
     */
    public static int[] removeAt(final int[] array, final int index, final int length) {
        int[] newArray = new int[array.length - length];
        System.arraycopy(array, 0, newArray, 0, index);
        System.arraycopy(array, index + length, newArray, index, array.length - index - length);
        return newArray;
    }

    /*
     * |----------------------------------------------------------------|
     * |                             long[]                             |
     * |----------------------------------------------------------------|
     */

    /**
     * Get the index of a long in an array.
     *
     * @param array The array to search in
     * @param l     The long to search for
     * @return The index of the long or -1 if not found
     */
    public static int indexOf(final long[] array, final long l) {
        for (int x = 0; x < array.length; x++) {
            if (array[x] == l) return x;
        }
        return -1;
    }

    /**
     * Get the index of a long in an array starting from the end.
     *
     * @param array The array to search in
     * @param l     The long to search for
     * @return The index of the long or -1 if not found
     */
    public static int indexOfLast(final long[] array, final long l) {
        for (int x = array.length - 1; x >= 0; x--) {
            if (array[x] == l) return x;
        }
        return -1;
    }

    /**
     * Get the index of a long array in an array.
     *
     * @param array The array to search in
     * @param other The long array to search for
     * @return The index of the long array or -1 if not found
     */
    public static int indexOf(final long[] array, final long[] other) {
        for (int x = 0; x < array.length; x++) {
            if (array[x] == other[0]) {
                boolean found = true;
                for (int y = 1; y < other.length; y++) {
                    if (y + x >= array.length) return -1;
                    if (array[x + y] != other[y]) {
                        found = false;
                        break;
                    }
                }
                if (found) return x;
            }
        }
        return -1;
    }

    /**
     * Add a long to the end of an array.
     *
     * @param array The array to add the long to
     * @param l     The long to add
     * @return The new array
     */
    public static long[] add(final long[] array, final long l) {
        return add(array, array.length, l);
    }

    /**
     * Add a long to the start of an array.
     *
     * @param array The array to add the long to
     * @param l     The long to add
     * @return The new array
     */
    public static long[] prepend(final long[] array, final long l) {
        return add(array, 0, l);
    }

    /**
     * Add a long to an array at a specific index.
     *
     * @param array The array to add the long to
     * @param index The index to add the long at
     * @param l     The long to add
     * @return The new array
     */
    public static long[] add(final long[] array, final int index, final long l) {
        long[] newArray = new long[array.length + 1];
        System.arraycopy(array, 0, newArray, 0, index);
        newArray[index] = l;
        System.arraycopy(array, index, newArray, index + 1, array.length - index);
        return newArray;
    }

    /**
     * Add multiple longs to the end of an array.
     *
     * @param array The array to add the longs to
     * @param longs The longs to add
     * @return The new array
     */
    public static long[] add(final long[] array, final long... longs) {
        return add(array, array.length, longs);
    }

    /**
     * Add multiple longs to the start of an array.
     *
     * @param array The array to add the longs to
     * @param longs The longs to add
     * @return The new array
     */
    public static long[] prepend(final long[] array, final long... longs) {
        return add(array, 0, longs);
    }

    /**
     * Add multiple longs to an array at a specific index.
     *
     * @param array The array to add the longs to
     * @param index The index to add the longs at
     * @param longs The longs to add
     * @return The new array
     */
    public static long[] add(final long[] array, final int index, final long... longs) {
        long[] newArray = new long[array.length + longs.length];
        System.arraycopy(array, 0, newArray, 0, index);
        System.arraycopy(longs, 0, newArray, index, longs.length);
        System.arraycopy(array, index, newArray, index + longs.length, array.length - index);
        return newArray;
    }

    /**
     * Add multiple long arrays to the end of an array.
     *
     * @param array The array to add the other arrays to
     * @param other The arrays to add
     * @return The new array
     */
    public static long[] add(final long[] array, final long[]... other) {
        return add(array, array.length, other);
    }

    /**
     * Add multiple long arrays to the start of an array.
     *
     * @param array The array to add the other arrays to
     * @param other The arrays to add
     * @return The new array
     */
    public static long[] prepend(final long[] array, final long[]... other) {
        return add(array, 0, other);
    }

    /**
     * Add multiple long arrays to an array at a specific index.
     *
     * @param array The array to add the other arrays to
     * @param index The index to add the other arrays at
     * @param other The arrays to add
     * @return The new array
     */
    public static long[] add(final long[] array, final int index, final long[]... other) {
        int length = array.length;
        for (long[] o : other) length += o.length;
        long[] newArray = new long[length];
        System.arraycopy(array, 0, newArray, 0, index);
        int i = index;
        for (long[] o : other) {
            System.arraycopy(o, 0, newArray, i, o.length);
            i += o.length;
        }
        System.arraycopy(array, index, newArray, i, array.length - index);
        return newArray;
    }

    /**
     * Remove a given long from an array.<br>
     * <b>Note:</b> This only removes the first occurrence of the long.<br>
     * <b>Note:</b> If the long is not found, the original array is returned.
     *
     * @param array The array to remove the long from
     * @param l     The long to remove
     * @return The new array
     */
    public static long[] remove(final long[] array, final long l) {
        int index = indexOf(array, l);
        if (index == -1) return array;
        return removeAt(array, index);
    }

    /**
     * Remove the first long from an array.
     *
     * @param array The array to remove the long from
     * @return The new array
     */
    public static long[] removeFirst(final long[] array) {
        return removeAt(array, 0);
    }

    /**
     * Remove the last long from an array.
     *
     * @param array The array to remove the long from
     * @return The new array
     */
    public static long[] removeLast(final long[] array) {
        return removeAt(array, array.length - 1);
    }

    /**
     * Remove a long from an array at a specific index.
     *
     * @param array The array to remove the long from
     * @param index The index to remove the long at
     * @return The new array
     */
    public static long[] removeAt(final long[] array, final int index) {
        return removeAt(array, index, 1);
    }

    /**
     * Remove multiple longs from an array at a specific index.
     *
     * @param array  The array to remove the longs from
     * @param index  The index to remove the longs at
     * @param length The number of longs to remove
     * @return The new array
     */
    public static long[] removeAt(final long[] array, final int index, final int length) {
        long[] newArray = new long[array.length - length];
        System.arraycopy(array, 0, newArray, 0, index);
        System.arraycopy(array, index + length, newArray, index, array.length - index - length);
        return newArray;
    }

    /*
     * |----------------------------------------------------------------|
     * |                            float[]                             |
     * |----------------------------------------------------------------|
     */

    /**
     * Get the index of a float in an array.
     *
     * @param array The array to search in
     * @param f     The float to search for
     * @return The index of the float or -1 if not found
     */
    public static int indexOf(final float[] array, final float f) {
        for (int x = 0; x < array.length; x++) {
            if (array[x] == f) return x;
        }
        return -1;
    }

    /**
     * Get the index of a float in an array starting from the end.
     *
     * @param array The array to search in
     * @param f     The float to search for
     * @return The index of the float or -1 if not found
     */
    public static int indexOfLast(final float[] array, final float f) {
        for (int x = array.length - 1; x >= 0; x--) {
            if (array[x] == f) return x;
        }
        return -1;
    }

    /**
     * Get the index of a float array in an array.
     *
     * @param array The array to search in
     * @param other The float array to search for
     * @return The index of the float array or -1 if not found
     */
    public static int indexOf(final float[] array, final float[] other) {
        for (int x = 0; x < array.length; x++) {
            if (array[x] == other[0]) {
                boolean found = true;
                for (int y = 1; y < other.length; y++) {
                    if (y + x >= array.length) return -1;
                    if (array[x + y] != other[y]) {
                        found = false;
                        break;
                    }
                }
                if (found) return x;
            }
        }
        return -1;
    }

    /**
     * Add a float to the end of an array.
     *
     * @param array The array to add the float to
     * @param f     The float to add
     * @return The new array
     */
    public static float[] add(final float[] array, final float f) {
        return add(array, array.length, f);
    }

    /**
     * Add a float to the start of an array.
     *
     * @param array The array to add the float to
     * @param f     The float to add
     * @return The new array
     */
    public static float[] prepend(final float[] array, final float f) {
        return add(array, 0, f);
    }

    /**
     * Add a float to an array at a specific index.
     *
     * @param array The array to add the float to
     * @param index The index to add the float at
     * @param f     The float to add
     * @return The new array
     */
    public static float[] add(final float[] array, final int index, final float f) {
        float[] newArray = new float[array.length + 1];
        System.arraycopy(array, 0, newArray, 0, index);
        newArray[index] = f;
        System.arraycopy(array, index, newArray, index + 1, array.length - index);
        return newArray;
    }

    /**
     * Add multiple floats to the end of an array.
     *
     * @param array  The array to add the floats to
     * @param floats The floats to add
     * @return The new array
     */
    public static float[] add(final float[] array, final float... floats) {
        return add(array, array.length, floats);
    }

    /**
     * Add multiple floats to the start of an array.
     *
     * @param array  The array to add the floats to
     * @param floats The floats to add
     * @return The new array
     */
    public static float[] prepend(final float[] array, final float... floats) {
        return add(array, 0, floats);
    }

    /**
     * Add multiple floats to an array at a specific index.
     *
     * @param array  The array to add the floats to
     * @param index  The index to add the floats at
     * @param floats The floats to add
     * @return The new array
     */
    public static float[] add(final float[] array, final int index, final float... floats) {
        float[] newArray = new float[array.length + floats.length];
        System.arraycopy(array, 0, newArray, 0, index);
        System.arraycopy(floats, 0, newArray, index, floats.length);
        System.arraycopy(array, index, newArray, index + floats.length, array.length - index);
        return newArray;
    }

    /**
     * Add multiple float arrays to the end of an array.
     *
     * @param array The array to add the other arrays to
     * @param other The arrays to add
     * @return The new array
     */
    public static float[] add(final float[] array, final float[]... other) {
        return add(array, array.length, other);
    }

    /**
     * Add multiple float arrays to the start of an array.
     *
     * @param array The array to add the other arrays to
     * @param other The arrays to add
     * @return The new array
     */
    public static float[] prepend(final float[] array, final float[]... other) {
        return add(array, 0, other);
    }

    /**
     * Add multiple float arrays to an array at a specific index.
     *
     * @param array The array to add the other arrays to
     * @param index The index to add the other arrays at
     * @param other The arrays to add
     * @return The new array
     */
    public static float[] add(final float[] array, final int index, final float[]... other) {
        int length = array.length;
        for (float[] o : other) length += o.length;
        float[] newArray = new float[length];
        System.arraycopy(array, 0, newArray, 0, index);
        int i = index;
        for (float[] o : other) {
            System.arraycopy(o, 0, newArray, i, o.length);
            i += o.length;
        }
        System.arraycopy(array, index, newArray, i, array.length - index);
        return newArray;
    }

    /**
     * Remove a given float from an array.<br>
     * <b>Note:</b> This only removes the first occurrence of the float.<br>
     * <b>Note:</b> If the float is not found, the original array is returned.
     *
     * @param array The array to remove the float from
     * @param f     The float to remove
     * @return The new array
     */
    public static float[] remove(final float[] array, final float f) {
        int index = indexOf(array, f);
        if (index == -1) return array;
        return removeAt(array, index);
    }

    /**
     * Remove the first float from an array.
     *
     * @param array The array to remove the float from
     * @return The new array
     */
    public static float[] removeFirst(final float[] array) {
        return removeAt(array, 0);
    }

    /**
     * Remove the last float from an array.
     *
     * @param array The array to remove the float from
     * @return The new array
     */
    public static float[] removeLast(final float[] array) {
        return removeAt(array, array.length - 1);
    }

    /**
     * Remove a float from an array at a specific index.
     *
     * @param array The array to remove the float from
     * @param index The index to remove the float at
     * @return The new array
     */
    public static float[] removeAt(final float[] array, final int index) {
        return removeAt(array, index, 1);
    }

    /**
     * Remove multiple floats from an array at a specific index.
     *
     * @param array  The array to remove the floats from
     * @param index  The index to remove the floats at
     * @param length The number of floats to remove
     * @return The new array
     */
    public static float[] removeAt(final float[] array, final int index, final int length) {
        float[] newArray = new float[array.length - length];
        System.arraycopy(array, 0, newArray, 0, index);
        System.arraycopy(array, index + length, newArray, index, array.length - index - length);
        return newArray;
    }

    /*
     * |----------------------------------------------------------------|
     * |                            double[]                            |
     * |----------------------------------------------------------------|
     */

    /**
     * Get the index of a double in an array.
     *
     * @param array The array to search in
     * @param d     The double to search for
     * @return The index of the double or -1 if not found
     */
    public static int indexOf(final double[] array, final double d) {
        for (int x = 0; x < array.length; x++) {
            if (array[x] == d) return x;
        }
        return -1;
    }

    /**
     * Get the index of a double in an array starting from the end.
     *
     * @param array The array to search in
     * @param d     The double to search for
     * @return The index of the double or -1 if not found
     */
    public static int indexOfLast(final double[] array, final double d) {
        for (int x = array.length - 1; x >= 0; x--) {
            if (array[x] == d) return x;
        }
        return -1;
    }

    /**
     * Get the index of a double array in an array.
     *
     * @param array The array to search in
     * @param other The double array to search for
     * @return The index of the double array or -1 if not found
     */
    public static int indexOf(final double[] array, final double[] other) {
        for (int x = 0; x < array.length; x++) {
            if (array[x] == other[0]) {
                boolean found = true;
                for (int y = 1; y < other.length; y++) {
                    if (y + x >= array.length) return -1;
                    if (array[x + y] != other[y]) {
                        found = false;
                        break;
                    }
                }
                if (found) return x;
            }
        }
        return -1;
    }

    /**
     * Add a double to the end of an array.
     *
     * @param array The array to add the double to
     * @param d     The double to add
     * @return The new array
     */
    public static double[] add(final double[] array, final double d) {
        return add(array, array.length, d);
    }

    /**
     * Add a double to the start of an array.
     *
     * @param array The array to add the double to
     * @param d     The double to add
     * @return The new array
     */
    public static double[] prepend(final double[] array, final double d) {
        return add(array, 0, d);
    }

    /**
     * Add a double to an array at a specific index.
     *
     * @param array The array to add the double to
     * @param index The index to add the double at
     * @param d     The double to add
     * @return The new array
     */
    public static double[] add(final double[] array, final int index, final double d) {
        double[] newArray = new double[array.length + 1];
        System.arraycopy(array, 0, newArray, 0, index);
        newArray[index] = d;
        System.arraycopy(array, index, newArray, index + 1, array.length - index);
        return newArray;
    }

    /**
     * Add multiple doubles to the end of an array.
     *
     * @param array   The array to add the doubles to
     * @param doubles The doubles to add
     * @return The new array
     */
    public static double[] add(final double[] array, final double... doubles) {
        return add(array, array.length, doubles);
    }

    /**
     * Add multiple doubles to the start of an array.
     *
     * @param array   The array to add the doubles to
     * @param doubles The doubles to add
     * @return The new array
     */
    public static double[] prepend(final double[] array, final double... doubles) {
        return add(array, 0, doubles);
    }

    /**
     * Add multiple doubles to an array at a specific index.
     *
     * @param array   The array to add the doubles to
     * @param index   The index to add the doubles at
     * @param doubles The doubles to add
     * @return The new array
     */
    public static double[] add(final double[] array, final int index, final double... doubles) {
        double[] newArray = new double[array.length + doubles.length];
        System.arraycopy(array, 0, newArray, 0, index);
        System.arraycopy(doubles, 0, newArray, index, doubles.length);
        System.arraycopy(array, index, newArray, index + doubles.length, array.length - index);
        return newArray;
    }

    /**
     * Add multiple double arrays to the end of an array.
     *
     * @param array The array to add the other arrays to
     * @param other The arrays to add
     * @return The new array
     */
    public static double[] add(final double[] array, final double[]... other) {
        return add(array, array.length, other);
    }

    /**
     * Add multiple double arrays to the start of an array.
     *
     * @param array The array to add the other arrays to
     * @param other The arrays to add
     * @return The new array
     */
    public static double[] prepend(final double[] array, final double[]... other) {
        return add(array, 0, other);
    }

    /**
     * Add multiple double arrays to an array at a specific index.
     *
     * @param array The array to add the other arrays to
     * @param index The index to add the other arrays at
     * @param other The arrays to add
     * @return The new array
     */
    public static double[] add(final double[] array, final int index, final double[]... other) {
        int length = array.length;
        for (double[] o : other) length += o.length;
        double[] newArray = new double[length];
        System.arraycopy(array, 0, newArray, 0, index);
        int i = index;
        for (double[] o : other) {
            System.arraycopy(o, 0, newArray, i, o.length);
            i += o.length;
        }
        System.arraycopy(array, index, newArray, i, array.length - index);
        return newArray;
    }

    /**
     * Remove a given double from an array.<br>
     * <b>Note:</b> This only removes the first occurrence of the double.<br>
     * <b>Note:</b> If the double is not found, the original array is returned.
     *
     * @param array The array to remove the double from
     * @param d     The double to remove
     * @return The new array
     */
    public static double[] remove(final double[] array, final double d) {
        int index = indexOf(array, d);
        if (index == -1) return array;
        return removeAt(array, index);
    }

    /**
     * Remove the first double from an array.
     *
     * @param array The array to remove the double from
     * @return The new array
     */
    public static double[] removeFirst(final double[] array) {
        return removeAt(array, 0);
    }

    /**
     * Remove the last double from an array.
     *
     * @param array The array to remove the double from
     * @return The new array
     */
    public static double[] removeLast(final double[] array) {
        return removeAt(array, array.length - 1);
    }

    /**
     * Remove a double from an array at a specific index.
     *
     * @param array The array to remove the double from
     * @param index The index to remove the double at
     * @return The new array
     */
    public static double[] removeAt(final double[] array, final int index) {
        return removeAt(array, index, 1);
    }

    /**
     * Remove multiple doubles from an array at a specific index.
     *
     * @param array  The array to remove the doubles from
     * @param index  The index to remove the doubles at
     * @param length The number of doubles to remove
     * @return The new array
     */
    public static double[] removeAt(final double[] array, final int index, final int length) {
        double[] newArray = new double[array.length - length];
        System.arraycopy(array, 0, newArray, 0, index);
        System.arraycopy(array, index + length, newArray, index, array.length - index - length);
        return newArray;
    }

    /*
     * |----------------------------------------------------------------|
     * |                            Object[]                            |
     * |----------------------------------------------------------------|
     */

    /**
     * Get the index of an object in an array.
     *
     * @param array The array to search in
     * @param o     The object to search for
     * @param <O>   The type of the array
     * @return The index of the object or -1 if not found
     */
    public static <O> int indexOf(final O[] array, final O o) {
        for (int x = 0; x < array.length; x++) {
            if (array[x] == o) return x;
        }
        return -1;
    }

    /**
     * Get the index of an object in an array starting from the end.
     *
     * @param array The array to search in
     * @param o     The object to search for
     * @param <O>   The type of the array
     * @return The index of the object or -1 if not found
     */
    public static <O> int indexOfLast(final O[] array, final O o) {
        for (int x = array.length - 1; x >= 0; x--) {
            if (array[x] == o) return x;
        }
        return -1;
    }

    /**
     * Get the index of an object array in an array.
     *
     * @param array The array to search in
     * @param other The object array to search for
     * @param <O>   The type of the array
     * @return The index of the object array or -1 if not found
     */
    public static <O> int indexOf(final O[] array, final O[] other) {
        for (int x = 0; x < array.length; x++) {
            if (array[x] == other[0]) {
                boolean found = true;
                for (int y = 1; y < other.length; y++) {
                    if (y + x >= array.length) return -1;
                    if (array[x + y] != other[y]) {
                        found = false;
                        break;
                    }
                }
                if (found) return x;
            }
        }
        return -1;
    }

    /**
     * Add an object to the end of an array.
     *
     * @param array The array to add the object to
     * @param o     The object to add
     * @param <O>   The type of the array
     * @return The new array
     */
    public static <O> O[] add(final O[] array, final O o) {
        return add(array, array.length, o);
    }

    /**
     * Add an object to the start of an array.
     *
     * @param array The array to add the object to
     * @param o     The object to add
     * @param <O>   The type of the array
     * @return The new array
     */
    public static <O> O[] prepend(final O[] array, final O o) {
        return add(array, 0, o);
    }

    /**
     * Add an object to an array at a specific index.
     *
     * @param array The array to add the object to
     * @param index The index to add the object at
     * @param o     The object to add
     * @param <O>   The type of the array
     * @return The new array
     */
    public static <O> O[] add(final O[] array, final int index, final O o) {
        O[] newArray = (O[]) Array.newInstance(array.getClass().getComponentType(), array.length + 1);
        System.arraycopy(array, 0, newArray, 0, index);
        newArray[index] = o;
        System.arraycopy(array, index, newArray, index + 1, array.length - index);
        return newArray;
    }

    /**
     * Add multiple objects to the end of an array.
     *
     * @param array   The array to add the objects to
     * @param objects The objects to add
     * @param <O>     The type of the array
     * @return The new array
     */
    @SafeVarargs
    public static <O> O[] add(final O[] array, final O... objects) {
        return add(array, array.length, objects);
    }

    /**
     * Add multiple objects to the start of an array.
     *
     * @param array   The array to add the objects to
     * @param objects The objects to add
     * @param <O>     The type of the array
     * @return The new array
     */
    @SafeVarargs
    public static <O> O[] prepend(final O[] array, final O... objects) {
        return add(array, 0, objects);
    }

    /**
     * Add multiple objects to an array at a specific index.
     *
     * @param array   The array to add the objects to
     * @param index   The index to add the objects at
     * @param objects The objects to add
     * @param <O>     The type of the array
     * @return The new array
     */
    @SafeVarargs
    public static <O> O[] add(final O[] array, final int index, final O... objects) {
        O[] newArray = (O[]) Array.newInstance(array.getClass().getComponentType(), array.length + objects.length);
        System.arraycopy(array, 0, newArray, 0, index);
        System.arraycopy(objects, 0, newArray, index, objects.length);
        System.arraycopy(array, index, newArray, index + objects.length, array.length - index);
        return newArray;
    }

    /**
     * Add multiple object arrays to the end of an array.
     *
     * @param array The array to add the other arrays to
     * @param other The arrays to add
     * @param <O>   The type of the array
     * @return The new array
     */
    @SafeVarargs
    public static <O> O[] add(final O[] array, final O[]... other) {
        return add(array, array.length, other);
    }

    /**
     * Add multiple object arrays to the start of an array.
     *
     * @param array The array to add the other arrays to
     * @param other The arrays to add
     * @param <O>   The type of the array
     * @return The new array
     */
    @SafeVarargs
    public static <O> O[] prepend(final O[] array, final O[]... other) {
        return add(array, 0, other);
    }

    /**
     * Add multiple object arrays to an array at a specific index.
     *
     * @param array The array to add the other arrays to
     * @param index The index to add the other arrays at
     * @param other The arrays to add
     * @param <O>   The type of the array
     * @return The new array
     */
    @SafeVarargs
    public static <O> O[] add(final O[] array, final int index, final O[]... other) {
        int length = array.length;
        for (O[] o : other) length += o.length;
        O[] newArray = (O[]) Array.newInstance(array.getClass().getComponentType(), length);
        System.arraycopy(array, 0, newArray, 0, index);
        int i = index;
        for (O[] o : other) {
            System.arraycopy(o, 0, newArray, i, o.length);
            i += o.length;
        }
        System.arraycopy(array, index, newArray, i, array.length - index);
        return newArray;
    }

    /**
     * Remove a given object from an array.<br>
     * <b>Note:</b> This only removes the first occurrence of the object.<br>
     * <b>Note:</b> If the object is not found, the original array is returned.
     *
     * @param array The array to remove the object from
     * @param o     The object to remove
     * @param <O>   The type of the array
     * @return The new array
     */
    public static <O> O[] remove(final O[] array, final O o) {
        int index = indexOf(array, o);
        if (index == -1) return array;
        return removeAt(array, index);
    }

    /**
     * Remove the first object from an array.
     *
     * @param array The array to remove the object from
     * @param <O>   The type of the array
     * @return The new array
     */
    public static <O> O[] removeFirst(final O[] array) {
        return removeAt(array, 0);
    }

    /**
     * Remove the last object from an array.
     *
     * @param array The array to remove the object from
     * @param <O>   The type of the array
     * @return The new array
     */
    public static <O> O[] removeLast(final O[] array) {
        return removeAt(array, array.length - 1);
    }

    /**
     * Remove an object from an array at a specific index.
     *
     * @param array The array to remove the object from
     * @param index The index to remove the object at
     * @param <O>   The type of the array
     * @return The new array
     */
    public static <O> O[] removeAt(final O[] array, final int index) {
        return removeAt(array, index, 1);
    }

    /**
     * Remove multiple objects from an array at a specific index.
     *
     * @param array  The array to remove the objects from
     * @param index  The index to remove the objects at
     * @param length The number of objects to remove
     * @param <O>    The type of the array
     * @return The new array
     */
    public static <O> O[] removeAt(final O[] array, final int index, final int length) {
        O[] newArray = (O[]) Array.newInstance(array.getClass().getComponentType(), array.length - length);
        System.arraycopy(array, 0, newArray, 0, index);
        System.arraycopy(array, index + length, newArray, index, array.length - index - length);
        return newArray;
    }

}
