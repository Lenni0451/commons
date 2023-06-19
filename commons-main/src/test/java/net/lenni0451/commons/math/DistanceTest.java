package net.lenni0451.commons.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Thank ChatGPT for these tests
class DistanceTest {

    @Test
    void between2DFloat() {
        assertEquals(5F, Distance.between(0F, 0F, 3F, 4F), 2);
        assertEquals(5F, Distance.between(0F, 0F, -3F, -4F), 2);
        assertEquals(5F, Distance.between(3F, 4F, 0F, 0F), 2);
        assertEquals(5F, Distance.between(-3F, -4F, 0F, 0F), 2);

        assertEquals(2.83F, MathUtils.round(Distance.between(1F, 2F, 3F, 4F), 2));
        assertEquals(2.83F, MathUtils.round(Distance.between(-1F, -2F, -3F, -4F), 2));
        assertEquals(2.83F, MathUtils.round(Distance.between(4F, 3F, 2F, 1F), 2));
        assertEquals(2.83F, MathUtils.round(Distance.between(-4F, -3F, -2F, -1F), 2));

        assertEquals(0F, Distance.between(0F, 0F, 0F, 0F), 2);
        assertEquals(0F, Distance.between(1F, 1F, 1F, 1F), 2);
        assertEquals(0F, Distance.between(-2F, -2F, -2F, -2F), 2);
        assertEquals(0F, Distance.between(0F, 0F, 0F, 0F), 2);

    }

    @Test
    void between2DDouble() {
        assertEquals(5D, Distance.between(0D, 0D, 3D, 4D), 2);
        assertEquals(5D, Distance.between(0D, 0D, -3D, -4D), 2);
        assertEquals(5D, Distance.between(3D, 4D, 0D, 0D), 2);
        assertEquals(5D, Distance.between(-3D, -4D, 0D, 0D), 2);

        assertEquals(2.83D, MathUtils.round(Distance.between(1D, 2D, 3D, 4D), 2));
        assertEquals(2.83D, MathUtils.round(Distance.between(-1D, -2D, -3D, -4D), 2));
        assertEquals(2.83D, MathUtils.round(Distance.between(4D, 3D, 2D, 1D), 2));
        assertEquals(2.83D, MathUtils.round(Distance.between(-4D, -3D, -2D, -1D), 2));

        assertEquals(0D, Distance.between(0D, 0D, 0D, 0D), 2);
        assertEquals(0D, Distance.between(1D, 1D, 1D, 1D), 2);
        assertEquals(0D, Distance.between(-2D, -2D, -2D, -2D), 2);
        assertEquals(0D, Distance.between(0D, 0D, 0D, 0D), 2);
    }

    @Test
    void between3DFloat() {
        assertEquals(5F, Distance.between(0F, 0F, 0F, 3F, 4F, 0F));
        assertEquals(5F, Distance.between(0F, 0F, 0F, -3F, -4F, 0F));
        assertEquals(5F, Distance.between(3F, 4F, 0F, 0F, 0F, 0F));
        assertEquals(5F, Distance.between(-3F, -4F, 0F, 0F, 0F, 0F));

        assertEquals(7.07F, MathUtils.round(Distance.between(0F, 0F, 0F, 3F, 4F, 5F), 2));
        assertEquals(7.07F, MathUtils.round(Distance.between(0F, 0F, 0F, -3F, -4F, -5F), 2));
        assertEquals(7.07F, MathUtils.round(Distance.between(3F, 4F, 5F, 0F, 0F, 0F), 2));
        assertEquals(7.07F, MathUtils.round(Distance.between(-3F, -4F, -5F, 0F, 0F, 0F), 2));

        assertEquals(12.45F, MathUtils.round(Distance.between(1F, 2F, 3F, -4F, -5F, -6F), 2));
        assertEquals(12.45F, MathUtils.round(Distance.between(-4F, -5F, -6F, 1F, 2F, 3F), 2));

        assertEquals(0F, Distance.between(0F, 0F, 0F, 0F, 0F, 0F));
        assertEquals(0F, Distance.between(1F, 2F, 3F, 1F, 2F, 3F));
    }

    @Test
    void between3DDouble() {
        assertEquals(5D, Distance.between(0D, 0D, 0D, 3D, 4D, 0D));
        assertEquals(5D, Distance.between(0D, 0D, 0D, -3D, -4D, 0D));
        assertEquals(5D, Distance.between(3D, 4D, 0D, 0D, 0D, 0D));
        assertEquals(5D, Distance.between(-3D, -4D, 0D, 0D, 0D, 0D));

        assertEquals(7.071D, MathUtils.round(Distance.between(0D, 0D, 0D, 3D, 4D, 5D), 3));
        assertEquals(7.071D, MathUtils.round(Distance.between(0D, 0D, 0D, -3D, -4D, -5D), 3));
        assertEquals(7.071D, MathUtils.round(Distance.between(3D, 4D, 5D, 0D, 0D, 0D), 3));
        assertEquals(7.071D, MathUtils.round(Distance.between(-3D, -4D, -5D, 0D, 0D, 0D), 3));

        assertEquals(12.45D, MathUtils.round(Distance.between(1D, 2D, 3D, -4D, -5D, -6D), 3));
        assertEquals(12.45D, MathUtils.round(Distance.between(-4D, -5D, -6D, 1D, 2D, 3D), 3));

        assertEquals(0D, Distance.between(0D, 0D, 0D, 0D, 0D, 0D));
        assertEquals(0D, Distance.between(1D, 2D, 3D, 1D, 2D, 3D));
    }

}
