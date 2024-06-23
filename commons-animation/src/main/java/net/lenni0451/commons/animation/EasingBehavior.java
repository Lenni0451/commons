package net.lenni0451.commons.animation;

import net.lenni0451.commons.animation.easing.EasingMode;

/**
 * Defines the behavior of the easing mode when reversing the animation.
 */
public enum EasingBehavior {

    /**
     * Keep the easing mode when reversing the animation.<br>
     * This can be used to <u>ease in</u> on the way forward and <u>ease in</u> again on the way back.<br>
     * <b>This only works correctly if the animation is guaranteed to be played entirely or the easing mode is set to {@link EasingMode#EASE_IN_OUT}!<br>
     * If the animation is reversed mid-way, the animation will jump to a different position!</b>
     */
    KEEP,
    /**
     * Keep the easing function when reversing the animation.<br>
     * This can be used to <u>ease in</u> on the way forward and <u>ease out</u> on the way back.<br>
     * This is compatible with animations that are reversed mid-way.
     */
    REVERSE,

}
