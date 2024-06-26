package net.lenni0451.commons.animation;

import lombok.Getter;
import net.lenni0451.commons.animation.easing.EasingFunction;
import net.lenni0451.commons.animation.easing.EasingMode;

@Getter
class AnimationFrame {

    private final EasingFunction easingFunction;
    private final EasingMode easingMode;
    private final float[] startValue;
    private final float[] endValue;
    private final int duration;
    private final EasingBehavior easingBehavior;

    public AnimationFrame(final EasingFunction easingFunction, final EasingMode easingMode, final float[] startValue, final float[] endValue, final int duration, final EasingBehavior easingBehavior) {
        this.easingFunction = easingFunction;
        this.easingMode = easingMode;
        this.startValue = startValue;
        this.endValue = endValue;
        this.duration = duration;
        this.easingBehavior = easingBehavior;

        if (startValue.length != endValue.length) throw new IllegalArgumentException("The start and end value arrays must have the same length!");
        if (startValue.length == 0) throw new IllegalArgumentException("The start and end value arrays must have at least one element!");
    }

    public long getTimeLeft(final long startTime) {
        return this.duration - (System.currentTimeMillis() - startTime);
    }

    public float[] getValue(final long startTime) {
        float progress = this.getProgress(startTime);
        float[] result = new float[this.startValue.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = this.startValue[i] + (this.endValue[i] - this.startValue[i]) * progress;
        }
        return result;
    }

    public float[] getInvertedValue(long startTime) {
        if (this.easingBehavior.equals(EasingBehavior.REVERSE)) {
            startTime = System.currentTimeMillis() - (this.duration - (System.currentTimeMillis() - startTime));
            return this.getValue(startTime);
        } else {
            float progress = this.getProgress(startTime);
            float[] result = new float[this.startValue.length];
            for (int i = 0; i < result.length; i++) {
                result[i] = this.endValue[i] + (this.startValue[i] - this.endValue[i]) * progress;
            }
            return result;
        }
    }

    public float getProgress(final long startTime) {
        float progress = (float) (System.currentTimeMillis() - startTime) / this.duration;
        if (progress > 1) return 1;
        return this.easingMode.call(this.easingFunction, progress);
    }

}
