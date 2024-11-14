package net.lenni0451.commons.math.shapes.rect;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Rect2dTest {

    @Test
    void areaCalculationIsCorrect() {
        Rect2d rect = new Rect2d(4, 5);
        assertEquals(20, rect.getArea());
    }

    @Test
    void squareIdentificationIsCorrect() {
        Rect2d square = new Rect2d(5, 5);
        assertTrue(square.isSquare());
    }

    @Test
    void nonSquareIdentificationIsCorrect() {
        Rect2d rect = new Rect2d(4, 5);
        assertFalse(rect.isSquare());
    }

    @Test
    void scalingChangesDimensionsCorrectly() {
        Rect2d rect = new Rect2d(4, 5);
        Rect2d scaled = rect.scale(2);
        assertEquals(8, scaled.getWidth());
        assertEquals(10, scaled.getHeight());
    }

    @Test
    void intersectionIsCorrect() {
        Rect2d rect1 = new Rect2d(0, 0, 4, 5);
        Rect2d rect2 = new Rect2d(2, 2, 4, 4);
        Rect2d intersection = rect1.intersection(rect2);
        assertEquals(2, intersection.getX());
        assertEquals(2, intersection.getY());
        assertEquals(2, intersection.getWidth());
        assertEquals(3, intersection.getHeight());
    }

    @Test
    void intersectionWithNoOverlapReturnsZeroArea() {
        Rect2d rect1 = new Rect2d(0, 0, 4, 4);
        Rect2d rect2 = new Rect2d(5, 5, 4, 4);
        Rect2d intersection = rect1.intersection(rect2);
        assertEquals(0, intersection.getArea());
    }

    @Test
    void containsReturnsTrueWhenFullyContained() {
        Rect2d rect1 = new Rect2d(0, 0, 4, 4);
        Rect2d rect2 = new Rect2d(1, 1, 2, 2);
        assertTrue(rect1.contains(rect2));
    }

    @Test
    void containsReturnsFalseWhenNotFullyContained() {
        Rect2d rect1 = new Rect2d(0, 0, 4, 4);
        Rect2d rect2 = new Rect2d(3, 3, 2, 2);
        assertFalse(rect1.contains(rect2));
    }

    @Test
    void intersectsReturnsTrueWhenIntersecting() {
        Rect2d rect1 = new Rect2d(0, 0, 4, 4);
        Rect2d rect2 = new Rect2d(3, 3, 2, 2);
        assertTrue(rect1.intersects(rect2));
    }

    @Test
    void intersectsReturnsFalseWhenNotIntersecting() {
        Rect2d rect1 = new Rect2d(0, 0, 4, 4);
        Rect2d rect2 = new Rect2d(5, 5, 2, 2);
        assertFalse(rect1.intersects(rect2));
    }

}
