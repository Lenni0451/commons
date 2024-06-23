package net.lenni0451.commons.animation;

import net.lenni0451.commons.animation.easing.EasingFunction;
import net.lenni0451.commons.animation.easing.EasingMode;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class AnimationFrameBuilder {

    private EasingFunction easingFunction;
    private EasingMode easingMode;
    private float[] startValue;
    private float[] endValue;
    private Integer duration;
    private EasingBehavior easingBehavior;

    AnimationFrameBuilder() {
    }

    /**
     * Set the easing function for the frame.
     *
     * @param easingFunction The easing function
     * @return The current builder instance
     */
    public AnimationFrameBuilder easingFunction(@Nullable final EasingFunction easingFunction) {
        this.easingFunction = easingFunction;
        return this;
    }

    /**
     * Set the easing mode for the frame.
     *
     * @param easingMode The easing mode
     * @return The current builder instance
     */
    public AnimationFrameBuilder easingMode(@Nullable final EasingMode easingMode) {
        this.easingMode = easingMode;
        return this;
    }

    /**
     * Set the start value for the frame.
     *
     * @param startValue The start value
     * @return The current builder instance
     */
    public AnimationFrameBuilder start(@Nullable final float... startValue) {
        this.startValue = startValue;
        return this;
    }

    /**
     * Set the end value for the frame.
     *
     * @param endValue The end value
     * @return The current builder instance
     */
    public AnimationFrameBuilder end(@Nullable final float... endValue) {
        this.endValue = endValue;
        return this;
    }

    /**
     * Set the duration for the frame.
     *
     * @param duration The duration
     * @return The current builder instance
     */
    public AnimationFrameBuilder duration(@Nullable final Integer duration) {
        this.duration = duration;
        return this;
    }

    /**
     * Set the reverse behavior for the frame.
     *
     * @param easingBehavior The reverse behavior
     * @return The current builder instance
     */
    public AnimationFrameBuilder reverseBehavior(@Nullable final EasingBehavior easingBehavior) {
        this.easingBehavior = easingBehavior;
        return this;
    }

    AnimationFrame build(final Supplier<AnimationFrame> parent) {
        if (this.easingFunction == null) this.easingFunction = parent.get().getEasingFunction();
        if (this.easingMode == null) this.easingMode = parent.get().getEasingMode();
        if (this.startValue == null) this.startValue = parent.get().getEndValue();
        if (this.endValue == null) throw new IllegalArgumentException("The end value must be set!");
        if (this.duration == null) this.duration = parent.get().getDuration();
        if (this.easingBehavior == null) this.easingBehavior = parent.get().getEasingBehavior();

        return new AnimationFrame(this.easingFunction, this.easingMode, this.startValue, this.endValue, this.duration, this.easingBehavior);
    }

}
