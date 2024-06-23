package net.lenni0451.commons.animation;

import java.util.ArrayList;
import java.util.List;

public class Animation {

    private final List<AnimationFrame> frames;
    private AnimationMode mode;

    private State state;
    private long startTime;
    private int currentFrame;
    private Float stoppedValue;

    public Animation() {
        this(AnimationMode.DEFAULT);
    }

    public Animation(final AnimationMode mode) {
        this.frames = new ArrayList<>();
        this.mode = mode;
        this.state = State.STOPPED;
    }

    public Animation addFrame(final AnimationFrame frame) {
        this.frames.add(frame);
        return this;
    }

    public Animation addFrames(final AnimationFrame... frames) {
        for (AnimationFrame frame : frames) this.addFrame(frame);
        return this;
    }

    public void setMode(final AnimationMode mode) {
        this.mode = mode;
    }

    public boolean isRunning() {
        return this.state.equals(State.RUNNING) || this.state.equals(State.REVERSED);
    }

    public boolean isReversed() {
        return this.state.equals(State.REVERSED);
    }

    public boolean isFinished() {
        return this.state.equals(State.FINISHED);
    }

    public void reverse() {
        if (this.state.equals(State.RUNNING)) this.state = State.REVERSED;
        else if (this.state.equals(State.REVERSED)) this.state = State.RUNNING;
        else throw new IllegalStateException("Can't reverse an animation that is not running");
    }

    public void start() {
        if (!this.state.equals(State.STOPPED)) return;
        this.state = State.RUNNING;
        this.startTime = System.currentTimeMillis();
        this.currentFrame = 0;
        this.stoppedValue = null;
    }

    public void stop() {
        if (!this.isRunning()) return;
        this.stoppedValue = this.getValue();
        this.state = State.STOPPED;
    }

    public void reset() {
        this.state = State.STOPPED;
        this.startTime = 0;
        this.currentFrame = 0;
        this.stoppedValue = null;
    }

    public float getValue() {
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
