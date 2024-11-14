package net.lenni0451.commons.math.shapes.rect;

import java.util.Objects;

public class Rect2d {

    public static final Rect2d ZERO = new Rect2d(0, 0, 0, 0);

    private final double x;
    private final double y;
    private final double width;
    private final double height;

    public Rect2d(final double width, final double height) {
        this(0, 0, width, height);
    }

    public Rect2d(final double x, final double y, final double width, final double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * @return The x coordinate of the top left corner
     */
    public double getX() {
        return this.x;
    }

    /**
     * @return The y coordinate of the top left corner
     */
    public double getY() {
        return this.y;
    }

    /**
     * @return The width of the rectangle
     */
    public double getWidth() {
        return this.width;
    }

    /**
     * @return The height of the rectangle
     */
    public double getHeight() {
        return this.height;
    }

    /**
     * @return The area of the rectangle
     */
    public double getArea() {
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
    public Rect2d scale(final double scale) {
        return this.scale(scale, scale);
    }

    /**
     * Scale the rectangle by different factors in both directions.
     *
     * @param scaleX The scale factor for the x direction
     * @param scaleY The scale factor for the y direction
     * @return The scaled rectangle
     */
    public Rect2d scale(final double scaleX, final double scaleY) {
        return new Rect2d(this.width * scaleX, this.height * scaleY);
    }

    /**
     * Check if this rectangle intersects with another rectangle.
     *
     * @param rect The other rectangle
     * @return If the rectangles intersect
     */
    public boolean intersects(final Rect2d rect) {
        return this.x < rect.x + rect.width && this.x + this.width > rect.x && this.y < rect.y + rect.height && this.y + this.height > rect.y;
    }

    /**
     * Check if this rectangle contains another rectangle.
     *
     * @param rect The other rectangle
     * @return If this rectangle contains the other rectangle
     */
    public boolean contains(final Rect2d rect) {
        return this.x <= rect.x && this.x + this.width >= rect.x + rect.width && this.y <= rect.y && this.y + this.height >= rect.y + rect.height;
    }

    /**
     * Get the intersection of this rectangle with another rectangle.
     *
     * @param rect The other rectangle
     * @return The intersection rectangle
     */
    public Rect2d intersection(final Rect2d rect) {
        if (!this.intersects(rect)) return ZERO;
        double x = Math.max(this.x, rect.x);
        double y = Math.max(this.y, rect.y);
        double width = Math.min(this.x + this.width, rect.x + rect.width) - x;
        double height = Math.min(this.y + this.height, rect.y + rect.height) - y;
        return new Rect2d(x, y, width, height);
    }

    @Override
    public String toString() {
        return "Rect2d{" +
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
        Rect2d rect2d = (Rect2d) o;
        return Double.compare(this.x, rect2d.x) == 0 && Double.compare(this.y, rect2d.y) == 0 && Double.compare(this.width, rect2d.width) == 0 && Double.compare(this.height, rect2d.height) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y, this.width, this.height);
    }

}
