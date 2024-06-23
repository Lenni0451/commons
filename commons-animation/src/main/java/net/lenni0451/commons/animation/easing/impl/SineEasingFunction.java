package net.lenni0451.commons.animation.easing.impl;

import net.lenni0451.commons.animation.easing.EasingFunction;

public class SineEasingFunction implements EasingFunction {

    @Override
    public float easeIn(float x) {
        return (float) (1 - Math.cos((x * Math.PI) / 2));
    }

    @Override
    public float easeOut(float x) {
        return (float) Math.sin((x * Math.PI) / 2);
    }

    @Override
    public float easeInOut(float x) {
        return (float) (-(Math.cos(Math.PI * x) - 1) / 2);
    }

}
