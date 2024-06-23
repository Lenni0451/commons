package net.lenni0451.commons.animation.easing;

public enum EasingMode {

    EASE_IN,
    EASE_OUT,
    EASE_IN_OUT;

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
