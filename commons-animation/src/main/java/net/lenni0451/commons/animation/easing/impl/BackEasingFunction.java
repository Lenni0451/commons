package net.lenni0451.commons.animation.easing.impl;

import net.lenni0451.commons.animation.easing.EasingFunction;

public class BackEasingFunction implements EasingFunction {

    private static final float C1 = 1.70158F;
    private static final float C2 = C1 * 1.525F;
    private static final float C3 = C1 + 1;

    @Override
    public float easeIn(float x) {
        return C3 * x * x * x - C1 * x * x;
    }

    @Override
    public float easeOut(float x) {
        return (float) (1 + C3 * Math.pow(x - 1, 3) + C1 * Math.pow(x - 1, 2));
    }

    @Override
    public float easeInOut(float x) {
        if (x < 0.5) return (float) ((Math.pow(2 * x, 2) * ((C2 + 1) * 2 * x - C2)) / 2);
        return (float) ((Math.pow(2 * x - 2, 2) * ((C2 + 1) * (x * 2 - 2) + C2) + 2) / 2);
    }

}
