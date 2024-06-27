package net.lenni0451.commons.animation.easing;

public enum EasingMode {

    EASE_IN,
    EASE_OUT,
    EASE_IN_OUT;

    public float call(final EasingFunction easingFunction, final float x) {
        switch (this) {
            case EASE_IN:
                return easingFunction.easeIn(x);
            case EASE_OUT:
                return easingFunction.easeOut(x);
            case EASE_IN_OUT:
                return easingFunction.easeInOut(x);
            default:
                throw new IllegalStateException("Unexpected value: " + this);
        }
    }

    public EasingMode invert() {
        switch (this) {
            case EASE_IN:
                return EASE_OUT;
            case EASE_OUT:
                return EASE_IN;
            case EASE_IN_OUT:
                return EASE_IN_OUT;
            default:
                throw new IllegalStateException("Unexpected value: " + this);
        }
    }

}
