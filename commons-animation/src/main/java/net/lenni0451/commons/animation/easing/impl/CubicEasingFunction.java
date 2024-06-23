package net.lenni0451.commons.animation.easing.impl;

import net.lenni0451.commons.animation.easing.EasingFunction;

public class CubicEasingFunction implements EasingFunction {

    @Override
    public float easeIn(float x) {
        return x * x * x;
    }

    @Override
    public float easeOut(float x) {
        return (float) (1 - Math.pow(1 - x, 3));
    }

    @Override
    public float easeInOut(float x) {
        if (x < 0.5) return 4 * x * x * x;
        return (float) (1 - Math.pow(-2 * x + 2, 3) / 2);
    }

}
