package net.lenni0451.commons.math.shapes.rect;

import java.util.Objects;

public class Rect2i {

    public static final Rect2i ZERO = new Rect2i(0, 0, 0, 0);

    private final int x;
    private final int y;
    private final int width;
    private final int height;

    public Rect2i(final int width, final int height) {
        this(0, 0, width, height);
    }

    public Rect2i(final int x, final int y, final int width, final int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * @return The x coordinate of the top left corner
     */
    public int getX() {
        return this.x;
    }

    /**
     * @return The y coordinate of the top left corner
     */
    public int getY() {
        return this.y;
    }

    /**
     * @return The width of the rectangle
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * @return The height of the rectangle
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * @return The area of the rectangle
     */
    public int getArea() {
        return this.width * this.height;
    }

    /**
     * @return If the rectangle is a square
     */
    public boolean isSquare() {
        return this.width == this.height;
    }

    /**
     * Scale the rectangle by the same factor in both directions.
     *
     * @param scale The scale factor
     * @return The scaled rectangle
     */
    public Rect2i scale(final int scale) {
        return this.scale(scale, scale);
    }

    /**
     * Scale the rectangle by different factors in both directions.
     *
     * @param scaleX The scale factor for the x direction
     * @param scaleY The scale factor for the y direction
     * @return The scaled rectangle
     */
    public Rect2i scale(final int scaleX, final int scaleY) {
        return new Rect2i(this.width * scaleX, this.height * scaleY);
    }

    /**
     * Check if this rectangle intersects with another rectangle.
     *
     * @param rect The other rectangle
     * @return If the rectangles intersect
     */
    public boolean intersects(final Rect2i rect) {
        return this.x < rect.x + rect.width && this.x + this.width > rect.x && this.y < rect.y + rect.height && this.y + this.height > rect.y;
    }

    /**
     * Check if this rectangle contains another rectangle.
     *
     * @param rect The other rectangle
     * @return If this rectangle contains the other rectangle
     */
    public boolean contains(final Rect2i rect) {
        return this.x <= rect.x && this.x + this.width >= rect.x + rect.width && this.y <= rect.y && this.y + this.height >= rect.y + rect.height;
    }

    /**
     * Get the intersection of this rectangle with another rectangle.
     *
     * @param rect The other rectangle
     * @return The intersection rectangle
     */
    public Rect2i intersection(final Rect2i rect) {
        if (!this.intersects(rect)) return ZERO;
        int x = Math.max(this.x, rect.x);
        int y = Math.max(this.y, rect.y);
        int width = Math.min(this.x + this.width, rect.x + rect.width) - x;
        int height = Math.min(this.y + this.height, rect.y + rect.height) - y;
        return new Rect2i(x, y, width, height);
    }

    @Override
    public String toString() {
        return "Rect2i{" +
                "x=" + this.x +
                ", y=" + this.y +
                ", width=" + this.width +
                ", height=" + this.height +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rect2i rect2i = (Rect2i) o;
        return this.x == rect2i.x && this.y == rect2i.y && this.width == rect2i.width && this.height == rect2i.height;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y, this.width, this.height);
    }

}
