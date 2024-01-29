package net.lenni0451.commons.math.shapes.rect;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Rect2iTest {

    @Test
    void areaCalculationIsCorrect() {
        Rect2i rect = new Rect2i(4, 5);
        assertEquals(20, rect.getArea());
    }

    @Test
    void squareIdentificationIsCorrect() {
        Rect2i square = new Rect2i(5, 5);
        assertTrue(square.isSquare());
    }

    @Test
    void nonSquareIdentificationIsCorrect() {
        Rect2i rect = new Rect2i(4, 5);
        assertFalse(rect.isSquare());
    }

    @Test
    void scalingChangesDimensionsCorrectly() {
        Rect2i rect = new Rect2i(4, 5);
        Rect2i scaled = rect.scale(2);
        assertEquals(8, scaled.getWidth());
        assertEquals(10, scaled.getHeight());
    }

    @Test
    void intersectionIsCorrect() {
        Rect2i rect1 = new Rect2i(0, 0, 4, 5);
        Rect2i rect2 = new Rect2i(2, 2, 4, 4);
        Rect2i intersection = rect1.intersection(rect2);
        assertEquals(2, intersection.getX());
        assertEquals(2, intersection.getY());
        assertEquals(2, intersection.getWidth());
        assertEquals(3, intersection.getHeight());
    }

    @Test
    void intersectionWithNoOverlapReturnsZeroArea() {
        Rect2i rect1 = new Rect2i(0, 0, 4, 4);
        Rect2i rect2 = new Rect2i(5, 5, 4, 4);
        Rect2i intersection = rect1.intersection(rect2);
        assertEquals(0, intersection.getArea());
    }

    @Test
    void containsReturnsTrueWhenFullyContained() {
        Rect2i rect1 = new Rect2i(0, 0, 4, 4);
        Rect2i rect2 = new Rect2i(1, 1, 2, 2);
        assertTrue(rect1.contains(rect2));
    }

    @Test
    void containsReturnsFalseWhenNotFullyContained() {
        Rect2i rect1 = new Rect2i(0, 0, 4, 4);
        Rect2i rect2 = new Rect2i(3, 3, 2, 2);
        assertFalse(rect1.contains(rect2));
    }

    @Test
    void intersectsReturnsTrueWhenIntersecting() {
        Rect2i rect1 = new Rect2i(0, 0, 4, 4);
        Rect2i rect2 = new Rect2i(3, 3, 2, 2);
        assertTrue(rect1.intersects(rect2));
    }

    @Test
    void intersectsReturnsFalseWhenNotIntersecting() {
        Rect2i rect1 = new Rect2i(0, 0, 4, 4);
        Rect2i rect2 = new Rect2i(5, 5, 2, 2);
        assertFalse(rect1.intersects(rect2));
    }

}
