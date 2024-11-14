package net.lenni0451.commons.math.shapes.rect;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Rect2fTest {

    @Test
    void areaCalculationIsCorrect() {
        Rect2f rect = new Rect2f(4, 5);
        assertEquals(20, rect.getArea());
    }

    @Test
    void squareIdentificationIsCorrect() {
        Rect2f square = new Rect2f(5, 5);
        assertTrue(square.isSquare());
    }

    @Test
    void nonSquareIdentificationIsCorrect() {
        Rect2f rect = new Rect2f(4, 5);
        assertFalse(rect.isSquare());
    }

    @Test
    void scalingChangesDimensionsCorrectly() {
        Rect2f rect = new Rect2f(4, 5);
        Rect2f scaled = rect.scale(2);
        assertEquals(8, scaled.getWidth());
        assertEquals(10, scaled.getHeight());
    }

    @Test
    void intersectionIsCorrect() {
        Rect2f rect1 = new Rect2f(0, 0, 4, 5);
        Rect2f rect2 = new Rect2f(2, 2, 4, 4);
        Rect2f intersection = rect1.intersection(rect2);
        assertEquals(2, intersection.getX());
        assertEquals(2, intersection.getY());
        assertEquals(2, intersection.getWidth());
        assertEquals(3, intersection.getHeight());
    }

    @Test
    void intersectionWithNoOverlapReturnsZeroArea() {
        Rect2f rect1 = new Rect2f(0, 0, 4, 4);
        Rect2f rect2 = new Rect2f(5, 5, 4, 4);
        Rect2f intersection = rect1.intersection(rect2);
        assertEquals(0, intersection.getArea());
    }

    @Test
    void containsReturnsTrueWhenFullyContained() {
        Rect2f rect1 = new Rect2f(0, 0, 4, 4);
        Rect2f rect2 = new Rect2f(1, 1, 2, 2);
        assertTrue(rect1.contains(rect2));
    }

    @Test
    void containsReturnsFalseWhenNotFullyContained() {
        Rect2f rect1 = new Rect2f(0, 0, 4, 4);
        Rect2f rect2 = new Rect2f(3, 3, 2, 2);
        assertFalse(rect1.contains(rect2));
    }

    @Test
    void intersectsReturnsTrueWhenIntersecting() {
        Rect2f rect1 = new Rect2f(0, 0, 4, 4);
        Rect2f rect2 = new Rect2f(3, 3, 2, 2);
        assertTrue(rect1.intersects(rect2));
    }

    @Test
    void intersectsReturnsFalseWhenNotIntersecting() {
        Rect2f rect1 = new Rect2f(0, 0, 4, 4);
        Rect2f rect2 = new Rect2f(5, 5, 2, 2);
        assertFalse(rect1.intersects(rect2));
    }

}
