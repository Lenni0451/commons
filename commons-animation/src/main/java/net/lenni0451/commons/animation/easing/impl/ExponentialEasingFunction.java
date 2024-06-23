package net.lenni0451.commons.animation.easing.impl;

import net.lenni0451.commons.animation.easing.EasingFunction;

public class ExponentialEasingFunction implements EasingFunction {

    @Override
    public float easeIn(float x) {
        if (x == 0) return 0;
        return (float) Math.pow(2, 10 * x - 10);
    }

    @Override
    public float easeOut(float x) {
        if (x == 1) return 1;
        return (float) (1 - Math.pow(2, -10 * x));
    }

    @Override
    public float easeInOut(float x) {
        if (x == 0) return 0;
        if (x == 1) return 1;
        if (x < 0.5) return (float) (Math.pow(2, 20 * x - 10) / 2);
        return (float) ((2 - Math.pow(2, -20 * x + 10)) / 2);
    }

}
