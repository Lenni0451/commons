package net.lenni0451.commons.animation.easing.impl;

import net.lenni0451.commons.animation.easing.EasingFunction;

public class QuadraticEasingFunction implements EasingFunction {

    @Override
    public float easeIn(float x) {
        return x * x;
    }

    @Override
    public float easeOut(float x) {
        return 1 - (1 - x) * (1 - x);
    }

    @Override
    public float easeInOut(float x) {
        if (x < 0.5) return 2 * x * x;
        return (float) (1 - Math.pow(-2 * x + 2, 2) / 2);
    }

}
