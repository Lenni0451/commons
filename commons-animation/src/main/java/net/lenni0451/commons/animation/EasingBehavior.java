package net.lenni0451.commons.animation;

/**
 * Defines the behavior of the easing mode when reversing the animation.
 */
public enum EasingBehavior {

    /**
     * Keep the easing mode when reversing the animation.<br>
     * This can be used to <u>ease in</u> on the way forward and <u>ease in</u> again on the way back.
     */
    KEEP,
    /**
     * Keep the easing function when reversing the animation.<br>
     * This can be used to <u>ease in</u> on the way forward and <u>ease out</u> on the way back.
     */
    REVERSE,

}
