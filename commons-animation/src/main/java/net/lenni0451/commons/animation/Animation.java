package net.lenni0451.commons.animation;

import net.lenni0451.commons.animation.easing.EasingFunction;
import net.lenni0451.commons.animation.easing.EasingMode;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * This class is used to create animations with multiple frames and easing functions.
 */
public class Animation {

    private final List<AnimationFrame> frames;
    private AnimationMode mode;

    private State state;
    private long startTime;
    private int currentFrame;
    private float[] stoppedValue;

    public Animation() {
        this(AnimationMode.DEFAULT);
    }

    public Animation(final AnimationMode mode) {
        this.frames = new ArrayList<>();
        this.mode = mode;
        this.state = State.STOPPED;
    }

    /**
     * Add a new frame to the animation.<br>
     * Fields that are not set will be copied from the last frame.<br>
     * The start value will be copied from the end value of the last frame.
     *
     * @param frameBuilder The builder for the frame
     * @return The current animation instance
     */
    public Animation frame(final Consumer<AnimationFrameBuilder> frameBuilder) {
        AnimationFrameBuilder builder = new AnimationFrameBuilder();
        frameBuilder.accept(builder);
        this.frames.add(builder.build(() -> {
            if (this.frames.isEmpty()) throw new IllegalStateException("Can't copy values from an animation without frames");
            return this.frames.get(this.frames.size() - 1);
        }));
        return this;
    }

    /**
     * Add a new frame to the animation.<br>
     * Fields that are set to {@code null} will be copied from the last frame.<br>
     * The start value will be copied from the end value of the last frame.
     *
     * @param easingFunction The easing function for the frame
     * @param easingMode     The easing mode for the frame
     * @param startValue     The start value for the frame
     * @param endValue       The end value for the frame
     * @param duration       The duration for the frame
     * @return The current animation instance
     */
    public Animation frame(@Nullable final EasingFunction easingFunction, @Nullable final EasingMode easingMode, @Nullable final float[] startValue, @Nullable final float[] endValue, @Nullable final Integer duration) {
        return this.frame(f -> f.easingFunction(easingFunction).easingMode(easingMode).start(startValue).end(endValue).duration(duration));
    }

    /**
     * Set the mode of the animation.
     *
     * @param mode The mode of the animation
     * @return The current animation instance
     */
    public Animation setMode(final AnimationMode mode) {
        this.mode = mode;
        return this;
    }

    /**
     * @return If the animation is running
     */
    public boolean isRunning() {
        return this.state.equals(State.RUNNING) || this.state.equals(State.REVERSED);
    }

    /**
     * @return If the animation is reversed
     */
    public boolean isReversed() {
        return this.state.equals(State.REVERSED);
    }

    /**
     * @return If the animation is finished
     */
    public boolean isFinished() {
        return this.state.equals(State.FINISHED);
    }

    /**
     * Reverse the current animation state.
     *
     * @return The current animation instance
     */
    public Animation reverse() {
        if (this.state.equals(State.RUNNING)) this.state = State.REVERSED;
        else if (this.state.equals(State.REVERSED)) this.state = State.RUNNING;
        else throw new IllegalStateException("Can't reverse an animation that is not running");
        return this;
    }

    /**
     * Start the animation.<br>
     * If the animation is already running, this method will do nothing.
     *
     * @return The current animation instance
     */
    public Animation start() {
        if (!this.state.equals(State.STOPPED)) return this;
        if (this.frames.isEmpty()) throw new IllegalStateException("Can't start an animation without frames");

        this.state = State.RUNNING;
        this.startTime = System.currentTimeMillis();
        this.currentFrame = 0;
        this.stoppedValue = null;
        return this;
    }

    /**
     * Stop the animation.<br>
     * If the animation is already stopped, this method will do nothing.<br>
     * After stopping the animation, the current value will be saved and returned by {@link #getValues()}.
     *
     * @return The current animation instance
     */
    public Animation stop() {
        if (!this.isRunning()) return this;
        this.stoppedValue = this.getValues();
        this.state = State.STOPPED;
        return this;
    }

    /**
     * Reset the animation to the start.<br>
     * This will stop the animation if it is running.
     *
     * @return The current animation instance
     */
    public Animation reset() {
        this.state = State.STOPPED;
        this.startTime = 0;
        this.currentFrame = 0;
        this.stoppedValue = null;
        return this;
    }

    /**
     * @return The current value of the animation
     */
    public float getValue() {
        return this.getValues()[0];
    }

    /**
     * @return The current values of the animation
     */
    public float[] getValues() {
        if (this.frames.isEmpty()) throw new IllegalStateException("Can't get values from an animation without frames");
        if (this.stoppedValue != null) return this.stoppedValue;
        if (this.state.equals(State.STOPPED)) return this.frames.get(0).getStartValue();
        if (this.state.equals(State.FINISHED)) return this.frames.get(this.frames.size() - 1).getEndValue();

        while (true) {
            AnimationFrame frame = this.frames.get(this.currentFrame);
            long timeLeft = frame.getTimeLeft(this.startTime);
            if (timeLeft > 0) {
                if (this.state.equals(State.RUNNING)) return frame.getValue(this.startTime);
                else if (this.state.equals(State.REVERSED)) return frame.getInvertedValue(this.startTime);
                else throw new IllegalStateException("Unknown Animation State: " + this.state);
            }

            int nextFrame = this.currentFrame + (this.state.equals(State.REVERSED) ? -1 : 1);
            if (nextFrame < 0) {
                switch (this.mode) {
                    case DEFAULT:
                        this.state = State.FINISHED;
                        this.stoppedValue = this.frames.get(0).getStartValue();
                        return this.stoppedValue;
                    case LOOP:
                        this.currentFrame = this.frames.size() - 1;
                        break;
                    case PING_PONG:
                        this.state = State.RUNNING;
                        this.currentFrame = 0;
                        break;
                    default:
                        throw new IllegalStateException("Unknown AnimationMode: " + this.mode);
                }
            } else if (nextFrame >= this.frames.size()) {
                switch (this.mode) {
                    case DEFAULT:
                        this.state = State.FINISHED;
                        this.stoppedValue = this.frames.get(this.frames.size() - 1).getEndValue();
                        return this.stoppedValue;
                    case LOOP:
                        this.currentFrame = 0;
                        break;
                    case PING_PONG:
                        this.state = State.REVERSED;
                        this.currentFrame = this.frames.size() - 1;
                        break;
                    default:
                        throw new IllegalStateException("Unknown AnimationMode: " + this.mode);
                }
            } else {
                this.currentFrame = nextFrame;
            }
            this.startTime = System.currentTimeMillis() + timeLeft;
        }
    }


    private enum State {
        STOPPED, RUNNING, REVERSED, FINISHED
    }

}
