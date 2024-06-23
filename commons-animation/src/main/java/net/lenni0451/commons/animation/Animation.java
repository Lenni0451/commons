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
    private boolean frameByFrame;

    private State state;
    private AnimationDirection direction;
    private long startTime;
    private int currentFrame;
    private float[] stoppedValue;

    public Animation() {
        this(AnimationMode.DEFAULT);
    }

    public Animation(final AnimationMode mode) {
        this.frames = new ArrayList<>();
        this.mode = mode;

        this.state = State.PAUSED;
        this.direction = AnimationDirection.FORWARDS;
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
        AnimationFrame frame = builder.build(() -> {
            if (this.frames.isEmpty()) throw new IllegalStateException("Can't copy values from an animation without frames");
            return this.frames.get(this.frames.size() - 1);
        });
        this.frames.add(frame);
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
     * @param easingBehavior The reverse behavior for the frame
     * @return The current animation instance
     */
    public Animation frame(@Nullable final EasingFunction easingFunction, @Nullable final EasingMode easingMode, final float startValue, final float endValue, @Nullable final Integer duration, @Nullable final EasingBehavior easingBehavior) {
        return this.frame(easingFunction, easingMode, new float[]{startValue}, new float[]{endValue}, duration, easingBehavior);
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
     * @param easingBehavior The reverse behavior for the frame
     * @return The current animation instance
     */
    public Animation frame(@Nullable final EasingFunction easingFunction, @Nullable final EasingMode easingMode, @Nullable final float[] startValue, @Nullable final float[] endValue, @Nullable final Integer duration, @Nullable final EasingBehavior easingBehavior) {
        return this.frame(f -> f.easingFunction(easingFunction).easingMode(easingMode).start(startValue).end(endValue).duration(duration).easingBehavior(easingBehavior));
    }

    /**
     * Set the mode of the animation.<br>
     * This will reset the animation.
     *
     * @param mode The mode of the animation
     * @return The current animation instance
     */
    public Animation setMode(final AnimationMode mode) {
        this.mode = mode;
        this.reset();
        return this;
    }

    /**
     * Set if the animation should be played frame by frame or not.<br>
     * If {@code true}, manually calling the {@link #start()} method is required to continue the animation.
     *
     * @param frameByFrame If the animation should be played frame by frame
     * @return The current animation instance
     */
    public Animation setFrameByFrame(boolean frameByFrame) {
        this.frameByFrame = frameByFrame;
        return this;
    }

    /**
     * @return If the animation is running
     */
    public boolean isRunning() {
        return this.state.equals(State.RUNNING);
    }

    /**
     * @return If the animation is finished
     */
    public boolean isFinished() {
        return this.state.equals(State.FINISHED);
    }

    /**
     * @return The direction of the animation
     */
    public AnimationDirection getDirection() {
        return this.direction;
    }

    /**
     * Set the direction of the animation.
     *
     * @param direction The direction of the animation
     */
    public Animation setDirection(final AnimationDirection direction) {
        if (!this.direction.equals(direction)) this.reverse();
        return this;
    }

    /**
     * @return The current frame of the animation
     */
    public int getCurrentFrame() {
        return this.currentFrame;
    }

    /**
     * @return The number of frames in the animation
     */
    public int getFrameCount() {
        return this.frames.size();
    }

    /**
     * Get the start value of the given frame.
     *
     * @param frame The frame to get the start value from
     * @return The start value of the frame
     */
    public float[] getStart(final int frame) {
        return this.frames.get(frame).getStartValue();
    }

    /**
     * Get the end value of the given frame.
     *
     * @param frame The frame to get the end value from
     * @return The end value of the frame
     */
    public float[] getEnd(final int frame) {
        return this.frames.get(frame).getEndValue();
    }

    /**
     * Reverse the current animation direction.
     *
     * @return The current animation instance
     */
    public Animation reverse() {
        this.direction = this.direction.getOpposite();
        if (this.isRunning()) {
            AnimationFrame frame = this.frames.get(this.currentFrame);
            this.startTime = System.currentTimeMillis() - frame.getTimeLeft(this.startTime);
        }
        return this;
    }

    /**
     * Start the animation in the given direction.<br>
     * If the given direction is already set, this method will do nothing.
     *
     * @param direction The direction to start the animation in
     * @return The current animation instance
     */
    public Animation start(final AnimationDirection direction) {
        if (this.isRunning()) return this;
        if (this.direction.equals(direction)) return this;
        this.state = State.PAUSED; //Set the state in case it was FINISHED
        this.reverse();
        return this.start();
    }

    /**
     * Start the animation.<br>
     * If the animation is already running, this method will do nothing.
     *
     * @return The current animation instance
     */
    public Animation start() {
        if (this.isRunning()) return this;
        if (this.state.equals(State.FINISHED)) return this;
        if (this.frames.isEmpty()) throw new IllegalStateException("Can't start an animation without frames");

        this.state = State.RUNNING;
        this.startTime = System.currentTimeMillis() - this.startTime;
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
        this.state = State.PAUSED;
        AnimationFrame frame = this.frames.get(this.currentFrame);
        this.startTime = frame.getDuration() - frame.getTimeLeft(this.startTime);
        return this;
    }

    /**
     * Reset the animation to the start.<br>
     * This will stop the animation if it is running.
     *
     * @return The current animation instance
     */
    public Animation reset() {
        this.state = State.PAUSED;
        this.direction = AnimationDirection.FORWARDS;
        this.startTime = 0;
        this.currentFrame = 0;
        this.stoppedValue = null;
        return this;
    }

    /**
     * Finish the animation immediately.<br>
     * This will stop the animation if it is running.
     *
     * @return The current animation instance
     */
    public Animation finish() {
        this.state = State.FINISHED;
        this.startTime = 0;
        if (this.direction.equals(AnimationDirection.FORWARDS)) {
            this.currentFrame = this.frames.size() - 1;
            this.stoppedValue = this.frames.get(this.currentFrame).getEndValue();
        } else {
            this.currentFrame = 0;
            this.stoppedValue = this.frames.get(0).getStartValue();
        }
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
        if (!this.isRunning()) {
            if (this.direction.equals(AnimationDirection.FORWARDS)) return this.frames.get(0).getStartValue();
            else if (this.direction.equals(AnimationDirection.BACKWARDS)) return this.frames.get(this.frames.size() - 1).getEndValue();
            else throw new IllegalStateException("Unknown Animation Direction: " + this.direction);
        }

        while (true) {
            AnimationFrame frame = this.frames.get(this.currentFrame);
            long timeLeft = frame.getTimeLeft(this.startTime);
            if (timeLeft > 0) {
                if (this.direction.equals(AnimationDirection.FORWARDS)) return frame.getValue(this.startTime);
                else if (this.direction.equals(AnimationDirection.BACKWARDS)) return frame.getInvertedValue(this.startTime);
                else throw new IllegalStateException("Unknown Animation Direction: " + this.direction);
            }

            int nextFrame = this.currentFrame + this.direction.getDirection();
            if (nextFrame < 0) {
                switch (this.mode) {
                    case DEFAULT:
                        this.state = State.FINISHED;
                        this.startTime = 0;
                        this.stoppedValue = this.frames.get(0).getStartValue();
                        return this.stoppedValue;
                    case LOOP:
                        this.currentFrame = this.frames.size() - 1;
                        break;
                    case PING_PONG:
                        this.direction = AnimationDirection.FORWARDS;
                        this.currentFrame = 0;
                        break;
                    default:
                        throw new IllegalStateException("Unknown AnimationMode: " + this.mode);
                }
            } else if (nextFrame >= this.frames.size()) {
                switch (this.mode) {
                    case DEFAULT:
                        this.state = State.FINISHED;
                        this.startTime = 0;
                        this.stoppedValue = this.frames.get(this.frames.size() - 1).getEndValue();
                        return this.stoppedValue;
                    case LOOP:
                        this.currentFrame = 0;
                        break;
                    case PING_PONG:
                        this.direction = AnimationDirection.BACKWARDS;
                        this.currentFrame = this.frames.size() - 1;
                        break;
                    default:
                        throw new IllegalStateException("Unknown AnimationMode: " + this.mode);
                }
            } else {
                this.currentFrame = nextFrame;
            }
            if (this.frameByFrame) {
                this.state = State.PAUSED;
                this.startTime = 0;
                this.stoppedValue = this.frames.get(this.currentFrame).getStartValue();
                return this.stoppedValue;
            } else {
                this.startTime = System.currentTimeMillis() + timeLeft;
            }
        }
    }


    private enum State {
        PAUSED, RUNNING, FINISHED
    }

}
