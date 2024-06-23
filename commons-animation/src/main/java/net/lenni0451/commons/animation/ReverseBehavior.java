package net.lenni0451.commons.animation;

import net.lenni0451.commons.animation.easing.EasingMode;

import java.util.function.Function;

public enum ReverseBehavior {

    /**
     * Keep the easing mode as it is.<br>
     * When reversing the animation the easing function will be used in the opposite direction.
     */
    KEEP(Function.identity()),
    /**
     * Invert the easing mode.<br>
     * When reversing the animation the easing function will play reversed.<br>
     * This is the recommended behavior for most animations.
     */
    REVERSE(EasingMode::invert);

    private final Function<EasingMode, EasingMode> function;

    ReverseBehavior(final Function<EasingMode, EasingMode> function) {
        this.function = function;
    }

    public EasingMode apply(final EasingMode mode) {
        return this.function.apply(mode);
    }

}
