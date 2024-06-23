package net.lenni0451.commons.animation;

import net.lenni0451.commons.animation.easing.EasingFunction;
import net.lenni0451.commons.animation.easing.EasingMode;

class AnimationFrame {

    private final EasingFunction easingFunction;
    private final EasingMode easingMode;
    private final float[] startValue;
    private final float[] endValue;
    private final int duration;

    public AnimationFrame(final EasingFunction easingFunction, final EasingMode easingMode, final float[] startValue, final float[] endValue, final int duration) {
        this.easingFunction = easingFunction;
        this.easingMode = easingMode;
        this.startValue = startValue;
        this.endValue = endValue;
        this.duration = duration;

        if (startValue.length != endValue.length) throw new IllegalArgumentException("The start and end value arrays must have the same length!");
        if (startValue.length == 0) throw new IllegalArgumentException("The start and end value arrays must have at least one element!");
    }

    public EasingFunction getEasingFunction() {
        return this.easingFunction;
    }

    public EasingMode getEasingMode() {
        return this.easingMode;
    }

    public float[] getStartValue() {
        return this.startValue;
    }

    public float[] getEndValue() {
        return this.endValue;
    }

    public int getDuration() {
        return this.duration;
    }

    public long getTimeLeft(final long startTime) {
        return this.duration - (System.currentTimeMillis() - startTime);
    }

    public float[] getValue(final long startTime) {
        float progress = this.getProgress(startTime, this.easingMode);
        float[] result = new float[this.startValue.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = this.startValue[i] + (this.endValue[i] - this.startValue[i]) * progress;
        }
        return result;
    }

    public float[] getInvertedValue(long startTime) {
        float progress = this.getProgress(startTime, this.easingMode.invert());
        float[] result = new float[this.startValue.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = this.endValue[i] + (this.startValue[i] - this.endValue[i]) * progress;
        }
        return result;
    }

    private float getProgress(final long startTime, final EasingMode easingMode) {
        float progress = (float) (System.currentTimeMillis() - startTime) / this.duration;
        if (progress > 1) return 0;
        switch (easingMode) {
            case EASE_IN:
                return this.easingFunction.easeIn(progress);
            case EASE_OUT:
                return this.easingFunction.easeOut(progress);
            case EASE_IN_OUT:
                return this.easingFunction.easeInOut(progress);
            default:
                return progress;
        }
    }

}
