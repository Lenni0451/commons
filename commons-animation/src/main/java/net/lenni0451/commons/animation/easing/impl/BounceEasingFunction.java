package net.lenni0451.commons.animation.easing.impl;

import net.lenni0451.commons.animation.easing.EasingFunction;

public class BounceEasingFunction implements EasingFunction {

    private static final float N1 = 7.5625F;
    private static final float D1 = 2.75F;

    @Override
    public float easeIn(float x) {
        return 1 - this.easeOut(1 - x);
    }

    @Override
    public float easeOut(float x) {
        if (x < 1 / D1) {
            return N1 * x * x;
        } else if (x < 2 / D1) {
            return (float) (N1 * (x -= 1.5 / D1) * x + 0.75);
        } else if (x < 2.5 / D1) {
            return (float) (N1 * (x -= 2.25 / D1) * x + 0.9375);
        } else {
            return (float) (N1 * (x -= 2.625 / D1) * x + 0.984375);
        }
    }

    @Override
    public float easeInOut(float x) {
        if (x < 0.5) return (1 - this.easeOut(1 - 2 * x)) / 2;
        return (1 + this.easeOut(2 * x - 1)) / 2;
    }

}
