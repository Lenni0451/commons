package net.lenni0451.commons.strings;

public class StringReader {

    private final String s;
    private int index;

    public StringReader(final String s) {
        this.s = s;
    }

    /**
     * @return The current index of the reader
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * Set the index of the reader.
     *
     * @param index The new index
     * @throws IndexOutOfBoundsException If the index is out of bounds
     */
    public void setIndex(final int index) {
        if (index < 0 || index > this.s.length()) throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + this.s.length());
        this.index = index;
    }

    /**
     * @return If there are more characters to read
     */
    public boolean hasNext() {
        return this.index < this.s.length();
    }

    /**
     * Read the next character and increase the index by 1.
     *
     * @return The next character
     * @throws IndexOutOfBoundsException If there are no more characters to read
     */
    public char next() {
        this.check(1);
        return this.s.charAt(this.index++);
    }

    /**
     * Peek the next character without increasing the index.
     *
     * @return The next character
     * @throws IndexOutOfBoundsException If there are no more characters to peek
     */
    public char peek() {
        this.check(1);
        return this.s.charAt(this.index);
    }

    /**
     * Peek the next characters without increasing the index.
     *
     * @param amount The amount of characters to peek
     * @return The next characters
     * @throws IndexOutOfBoundsException If there are not enough characters to peek
     */
    public String peek(final int amount) {
        this.check(amount);
        return this.s.substring(this.index, this.index + amount);
    }

    /**
     * Peek the remaining characters without increasing the index.
     *
     * @return The remaining characters
     */
    public String peekRemaining() {
        return this.s.substring(this.index);
    }

    /**
     * Skip the next character and increase the index by 1.
     *
     * @return The reader itself
     * @throws IndexOutOfBoundsException If there are no more characters to skip
     */
    public StringReader skip() {
        this.skip(1);
        return this;
    }

    /**
     * Skip the next characters and increase the index by the given amount.
     *
     * @param amount The amount of characters to skip
     * @return The reader itself
     * @throws IndexOutOfBoundsException If there are not enough characters to skip
     */
    public StringReader skip(final int amount) {
        this.check(amount);
        this.index += amount;
        return this;
    }

    /**
     * Skip all upcoming whitespace characters.
     *
     * @return The reader itself
     */
    public StringReader skipWhitespace() {
        while (this.hasNext() && Character.isWhitespace(this.peek())) {
            this.skip();
        }
        return this;
    }

    /**
     * Skip all characters until the given character is found.<br>
     * The character itself will not be skipped.
     *
     * @param c The character to skip until
     * @return The reader itself
     */
    public StringReader skipUntil(final char c) {
        while (this.hasNext() && this.peek() != c) {
            this.skip();
        }
        return this;
    }

    /**
     * Read all characters until the given character is found.<br>
     * The character itself will not be read.
     *
     * @param c The character to read until
     * @return The read characters
     */
    public String readUntil(final char c) {
        StringBuilder sb = new StringBuilder();
        while (this.hasNext() && this.peek() != c) {
            sb.append(this.next());
        }
        return sb.toString();
    }

    /**
     * Read the next word (characters until the next whitespace character) and skip all upcoming whitespace characters.
     *
     * @return The read word
     * @throws IllegalStateException If there are no more characters to read
     */
    public String readWord() {
        if (!this.hasNext()) throw new IllegalStateException("No more characters to read");
        StringBuilder sb = new StringBuilder();
        while (this.hasNext() && !Character.isWhitespace(this.peek())) {
            sb.append(this.next());
        }
        this.skipWhitespace();
        return sb.toString();
    }

    /**
     * Read the next quoted string and skip all upcoming whitespace characters.<br>
     * Quotes are {@code "} and {@code '}.
     *
     * @return The read quoted string
     * @throws IllegalStateException If the next character is not a quote character
     */
    public String readQuoted() {
        if (this.peek() != '"' && this.peek() != '\'') throw new IllegalStateException("Expected a quote character at index " + this.index);

        StringBuilder sb = new StringBuilder();
        char quote = this.next();
        while (this.hasNext() && this.peek() != quote) {
            sb.append(this.next());
        }
        if (this.hasNext()) {
            this.skip();
            this.skipWhitespace();
        }
        return sb.toString();
    }


    private void check(final int count) {
        if (this.index + count > this.s.length()) throw new IndexOutOfBoundsException("Index: " + this.index + ", Length: " + this.s.length());
    }

}
