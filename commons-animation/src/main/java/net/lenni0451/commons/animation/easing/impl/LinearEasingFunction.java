package net.lenni0451.commons.animation.easing.impl;

import net.lenni0451.commons.animation.easing.EasingFunction;
import net.lenni0451.commons.animation.easing.EasingMode;

public class LinearEasingFunction implements EasingFunction {

    @Override
    public float easeIn(float x) {
        return x;
    }

    @Override
    public float easeOut(float x) {
        return x;
    }

    @Override
    public float easeInOut(float x) {
        return x;
    }

    @Override
    public float getInverse(EasingMode easingMode, float wantedOutput) {
        return wantedOutput;
    }

}
