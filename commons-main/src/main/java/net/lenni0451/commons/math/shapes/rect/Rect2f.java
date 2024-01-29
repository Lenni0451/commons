package net.lenni0451.commons.math.shapes.rect;

import java.util.Objects;

public class Rect2f {

    public static final Rect2f ZERO = new Rect2f(0, 0, 0, 0);

    private final float x;
    private final float y;
    private final float width;
    private final float height;

    public Rect2f(final float width, final float height) {
        this(0, 0, width, height);
    }

    public Rect2f(final float x, final float y, final float width, final float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * @return The x coordinate of the top left corner
     */
    public float getX() {
        return this.x;
    }

    /**
     * @return The y coordinate of the top left corner
     */
    public float getY() {
        return this.y;
    }

    /**
     * @return The width of the rectangle
     */
    public float getWidth() {
        return this.width;
    }

    /**
     * @return The height of the rectangle
     */
    public float getHeight() {
        return this.height;
    }

    /**
     * @return The area of the rectangle
     */
    public float getArea() {
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
    public Rect2f scale(final float scale) {
        return this.scale(scale, scale);
    }

    /**
     * Scale the rectangle by different factors in both directions.
     *
     * @param scaleX The scale factor for the x direction
     * @param scaleY The scale factor for the y direction
     * @return The scaled rectangle
     */
    public Rect2f scale(final float scaleX, final float scaleY) {
        return new Rect2f(this.width * scaleX, this.height * scaleY);
    }

    /**
     * Check if this rectangle intersects with another rectangle.
     *
     * @param rect The other rectangle
     * @return If the rectangles intersect
     */
    public boolean intersects(final Rect2f rect) {
        return this.x < rect.x + rect.width && this.x + this.width > rect.x && this.y < rect.y + rect.height && this.y + this.height > rect.y;
    }

    /**
     * Check if this rectangle contains another rectangle.
     *
     * @param rect The other rectangle
     * @return If this rectangle contains the other rectangle
     */
    public boolean contains(final Rect2f rect) {
        return this.x <= rect.x && this.x + this.width >= rect.x + rect.width && this.y <= rect.y && this.y + this.height >= rect.y + rect.height;
    }

    /**
     * Get the intersection of this rectangle with another rectangle.
     *
     * @param rect The other rectangle
     * @return The intersection rectangle
     */
    public Rect2f intersection(final Rect2f rect) {
        if (!this.intersects(rect)) return ZERO;
        float x = Math.max(this.x, rect.x);
        float y = Math.max(this.y, rect.y);
        float width = Math.min(this.x + this.width, rect.x + rect.width) - x;
        float height = Math.min(this.y + this.height, rect.y + rect.height) - y;
        return new Rect2f(x, y, width, height);
    }

    @Override
    public String toString() {
        return "Rect2f{" +
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
        Rect2f rect2f = (Rect2f) o;
        return Float.compare(this.x, rect2f.x) == 0 && Float.compare(this.y, rect2f.y) == 0 && Float.compare(this.width, rect2f.width) == 0 && Float.compare(this.height, rect2f.height) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y, this.width, this.height);
    }

}
