package net.lenni0451.commons.animation.easing.impl;

import net.lenni0451.commons.animation.easing.EasingFunction;

public class CircularEasingFunction implements EasingFunction {

    @Override
    public float easeIn(float x) {
        return (float) (1 - Math.sqrt(1 - Math.pow(x, 2)));
    }

    @Override
    public float easeOut(float x) {
        return (float) Math.sqrt(1 - Math.pow(x - 1, 2));
    }

    @Override
    public float easeInOut(float x) {
        if (x < 0.5) return (float) ((1 - Math.sqrt(1 - Math.pow(2 * x, 2))) / 2);
        return (float) ((Math.sqrt(1 - Math.pow(-2 * x + 2, 2)) + 1) / 2);
    }

}
