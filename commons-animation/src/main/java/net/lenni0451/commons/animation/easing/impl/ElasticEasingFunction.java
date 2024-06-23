package net.lenni0451.commons.animation.easing.impl;

import net.lenni0451.commons.animation.easing.EasingFunction;

public class ElasticEasingFunction implements EasingFunction {

    private static final float C4 = (float) ((2 * Math.PI) / 3);
    private static final float C5 = (float) ((2 * Math.PI) / 4.5);

    @Override
    public float easeIn(float x) {
        if (x == 0) return 0;
        if (x == 1) return 1;
        return (float) (-Math.pow(2, 10 * x - 10) * Math.sin((x * 10 - 10.75) * C4));
    }

    @Override
    public float easeOut(float x) {
        if (x == 0) return 0;
        if (x == 1) return 1;
        return (float) (Math.pow(2, -10 * x) * Math.sin((x * 10 - 0.75) * C4) + 1);
    }

    @Override
    public float easeInOut(float x) {
        if (x == 0) return 0;
        if (x == 1) return 1;

        double sin = Math.sin((20 * x - 11.125) * C5);
        if (x < 0.5) return (float) (-(Math.pow(2, 20 * x - 10) * sin) / 2);
        return (float) ((Math.pow(2, -20 * x + 10) * sin) / 2 + 1);
    }

}
