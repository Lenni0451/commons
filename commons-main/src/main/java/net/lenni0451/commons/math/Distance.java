package net.lenni0451.commons.math;

public class Distance {

    /**
     * Calculate the distance between two points (2D).
     *
     * @param x1 The x coordinate of the first point
     * @param y1 The y coordinate of the first point
     * @param x2 The x coordinate of the second point
     * @param y2 The y coordinate of the second point
     * @return The distance between the two points
     */
    public static float between(final float x1, final float y1, final float x2, final float y2) {
        return (float) Math.sqrt(squared(x1, y1, x2, y2));
    }

    /**
     * Calculate the distance between two points (2D).
     *
     * @param x1 The x coordinate of the first point
     * @param y1 The y coordinate of the first point
     * @param x2 The x coordinate of the second point
     * @param y2 The y coordinate of the second point
     * @return The distance between the two points
     */
    public static double between(final double x1, final double y1, final double x2, final double y2) {
        return Math.sqrt(squared(x1, y1, x2, y2));
    }

    /**
     * Calculate the squared distance between two points (2D).
     *
     * @param x1 The x coordinate of the first point
     * @param y1 The y coordinate of the first point
     * @param x2 The x coordinate of the second point
     * @param y2 The y coordinate of the second point
     * @return The squared distance between the two points
     */
    public static float squared(final float x1, final float y1, final float x2, final float y2) {
        return (float) (Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    /**
     * Calculate the squared distance between two points (2D).
     *
     * @param x1 The x coordinate of the first point
     * @param y1 The y coordinate of the first point
     * @param x2 The x coordinate of the second point
     * @param y2 The y coordinate of the second point
     * @return The squared distance between the two points
     */
    public static double squared(final double x1, final double y1, final double x2, final double y2) {
        return Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2);
    }

    /**
     * Calculate the distance between two points (3D).
     *
     * @param x1 The x coordinate of the first point
     * @param y1 The y coordinate of the first point
     * @param z1 The z coordinate of the first point
     * @param x2 The x coordinate of the second point
     * @param y2 The y coordinate of the second point
     * @param z2 The z coordinate of the second point
     * @return The distance between the two points
     */
    public static float between(final float x1, final float y1, final float z1, final float x2, final float y2, final float z2) {
        return (float) Math.sqrt(squared(x1, y1, z1, x2, y2, z2));
    }

    /**
     * Calculate the distance between two points (3D).
     *
     * @param x1 The x coordinate of the first point
     * @param y1 The y coordinate of the first point
     * @param z1 The z coordinate of the first point
     * @param x2 The x coordinate of the second point
     * @param y2 The y coordinate of the second point
     * @param z2 The z coordinate of the second point
     * @return The distance between the two points
     */
    public static double between(final double x1, final double y1, final double z1, final double x2, final double y2, final double z2) {
        return Math.sqrt(squared(x1, y1, z1, x2, y2, z2));
    }

    /**
     * Calculate the squared distance between two points (3D).
     *
     * @param x1 The x coordinate of the first point
     * @param y1 The y coordinate of the first point
     * @param z1 The z coordinate of the first point
     * @param x2 The x coordinate of the second point
     * @param y2 The y coordinate of the second point
     * @param z2 The z coordinate of the second point
     * @return The squared distance between the two points
     */
    public static float squared(final float x1, final float y1, final float z1, final float x2, final float y2, final float z2) {
        return (float) (Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2) + Math.pow(z2 - z1, 2));
    }

    /**
     * Calculate the squared distance between two points (3D).
     *
     * @param x1 The x coordinate of the first point
     * @param y1 The y coordinate of the first point
     * @param z1 The z coordinate of the first point
     * @param x2 The x coordinate of the second point
     * @param y2 The y coordinate of the second point
     * @param z2 The z coordinate of the second point
     * @return The squared distance between the two points
     */
    public static double squared(final double x1, final double y1, final double z1, final double x2, final double y2, final double z2) {
        return Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2) + Math.pow(z2 - z1, 2);
    }

}
