package net.lenni0451.commons.animation;

import net.lenni0451.commons.animation.clock.TickClock;
import net.lenni0451.commons.animation.easing.EasingFunction;
import net.lenni0451.commons.animation.easing.EasingMode;

import java.util.Arrays;

/**
 * A dynamic animation with dynamically changing target values.<br>
 * The animation will automatically handle the easing and duration of the animation.
 */
public class DynamicAnimation {

    private final EasingFunction easingFunction;
    private final EasingMode easingMode;
    private final long durationPerUnit;
    private final float durationUnit;
    private TickClock clock;

    private float[] start;
    private float[] target;
    private long startTime;
    private long duration;

    public DynamicAnimation(final EasingFunction easingFunction, final EasingMode easingMode, final long duration, final float target) {
        this(easingFunction, easingMode, duration, 0, new float[]{target});
    }

    public DynamicAnimation(final EasingFunction easingFunction, final EasingMode easingMode, final long duration, final float[] target) {
        this(easingFunction, easingMode, duration, 0, target);
    }

    public DynamicAnimation(final EasingFunction easingFunction, final EasingMode easingMode, final long durationPerUnit, final float durationUnit, final float target) {
        this(easingFunction, easingMode, durationPerUnit, durationUnit, new float[]{target});
    }

    public DynamicAnimation(final EasingFunction easingFunction, final EasingMode easingMode, final long durationPerUnit, final float durationUnit, final float[] target) {
        this.easingFunction = easingFunction;
        this.easingMode = easingMode;
        this.durationPerUnit = durationPerUnit;
        this.durationUnit = durationUnit;
        this.clock = TickClock.SYSTEM;

        this.start = target;
        this.target = target;
    }

    /**
     * @return The clock used for the animation
     */
    public TickClock getClock() {
        return this.clock;
    }

    /**
     * Set the clock used for the animation.
     *
     * @param clock The clock
     * @return The current instance
     */
    public DynamicAnimation setClock(final TickClock clock) {
        this.clock = clock;
        return this;
    }

    /**
     * @return The start value of the animation
     */
    public float getStart() {
        return this.getStarts()[0];
    }

    /**
     * @return The start values of the animation
     */
    public float[] getStarts() {
        return this.start;
    }

    /**
     * @return The target value of the animation
     */
    public float getTarget() {
        return this.getTargets()[0];
    }

    /**
     * @return The target values of the animation
     */
    public float[] getTargets() {
        return this.target;
    }

    /**
     * @return If the animation is currently running
     */
    public boolean isRunning() {
        return this.startTime != 0 && this.clock.getTime() - this.startTime < this.duration;
    }

    /**
     * Stop the animation and keep the current value.<br>
     * It can be resumed by calling {@link #setTarget(float...)}.
     *
     * @return The current instance
     */
    public DynamicAnimation stop() {
        this.target = this.getValues();
        this.startTime = 0;
        return this;
    }

    /**
     * Immediately stop the animation and jump to the target value.<br>
     * It can be resumed by calling {@link #setTarget(float...)}.
     *
     * @return The current instance
     */
    public DynamicAnimation finish() {
        this.startTime = 0;
        return this;
    }

    /**
     * Set the target value of the animation.<br>
     * If the target value is the same as the current target value, nothing will happen.
     *
     * @param target The new target value
     * @return The current instance
     */
    public DynamicAnimation setTarget(final float... target) {
        if (Arrays.equals(this.target, target)) return this;
        this.start = this.getValues();
        this.target = target;
        this.startTime = this.clock.getTime();
        if (this.durationUnit == 0) {
            this.duration = this.durationPerUnit;
        } else {
            float maxDiff = 0;
            for (int i = 0; i < this.target.length; i++) {
                maxDiff = Math.max(maxDiff, Math.abs(this.target[i] - this.start[i]));
            }
            this.duration = (long) (maxDiff / this.durationUnit * this.durationPerUnit);
        }
        return this;
    }

    /**
     * Get the current value of the animation.
     *
     * @return The current value
     */
    public float getValue() {
        return this.getValues()[0];
    }

    /**
     * Get the current values of the animation.
     *
     * @return The current values
     */
    public float[] getValues() {
        if (this.startTime == 0) return this.target;
        float progress = (float) (this.clock.getTime() - this.startTime) / this.duration;
        if (progress > 1) return this.target;
        float position = this.easingMode.call(this.easingFunction, progress);
        float[] result = new float[this.target.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = this.start[i] + (this.target[i] - this.start[i]) * position;
        }
        return result;
    }

}
