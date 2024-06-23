package net.lenni0451.commons.animation;

import net.lenni0451.commons.animation.easing.EasingFunction;
import net.lenni0451.commons.animation.easing.EasingMode;

public class AnimationFrame {

    private final EasingFunction easingFunction;
    private final EasingMode easingMode;
    private final float startValue;
    private final float endValue;
    private final int duration;

    public AnimationFrame(final EasingFunction easingFunction, final EasingMode easingMode, final float startValue, final float endValue, final int duration) {
        this.easingFunction = easingFunction;
        this.easingMode = easingMode;
        this.startValue = startValue;
        this.endValue = endValue;
        this.duration = duration;
    }

    public EasingFunction getEasingFunction() {
        return this.easingFunction;
    }

    public EasingMode getEasingMode() {
        return this.easingMode;
    }

    public float getStartValue() {
        return this.startValue;
    }

    public float getEndValue() {
        return this.endValue;
    }

    public float getDuration() {
        return this.duration;
    }

    public long getTimeLeft(final long startTime) {
        return this.duration - (System.currentTimeMillis() - startTime);
    }

    public float getValue(final long startTime) {
        return this.startValue + (this.endValue - this.startValue) * this.getProgress(startTime, this.easingMode);
    }

    public float getInvertedValue(long startTime) {
        return this.endValue + (this.startValue - this.endValue) * this.getProgress(startTime, this.easingMode.invert());
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
