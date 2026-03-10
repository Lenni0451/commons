package net.lenni0451.commons.animation;

/**
 * A listener for animation events.
 */
public interface AnimationListener {

    /**
     * Called when the animation starts.
     *
     * @param animation The animation
     */
    default void onStart(final Animation animation) {
    }

    /**
     * Called when the animation changes its frame.
     *
     * @param animation The animation
     * @param oldFrame  The old frame index
     * @param newFrame  The new frame index
     */
    default void onFrameChange(final Animation animation, final int oldFrame, final int newFrame) {
    }

    /**
     * Called when the animation finishes.
     *
     * @param animation The animation
     */
    default void onFinish(final Animation animation) {
    }

    /**
     * Called when the animation loops.
     *
     * @param animation The animation
     */
    default void onLoop(final Animation animation) {
    }

    /**
     * Called when the animation ping-pongs.
     *
     * @param animation    The animation
     * @param newDirection The new direction
     */
    default void onPingPong(final Animation animation, final AnimationDirection newDirection) {
    }

}
