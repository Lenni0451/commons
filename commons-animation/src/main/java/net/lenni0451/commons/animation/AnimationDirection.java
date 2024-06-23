package net.lenni0451.commons.animation;

public enum AnimationDirection {

    FORWARDS(1),
    BACKWARDS(-1);

    private final int direction;

    AnimationDirection(final int direction) {
        this.direction = direction;
    }

    public int getDirection() {
        return this.direction;
    }

    public AnimationDirection getOpposite() {
        return this == FORWARDS ? BACKWARDS : FORWARDS;
    }

}
