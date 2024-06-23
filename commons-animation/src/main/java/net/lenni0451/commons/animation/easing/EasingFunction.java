package net.lenni0451.commons.animation.easing;

import net.lenni0451.commons.animation.easing.impl.*;

public interface EasingFunction {

    EasingFunction LINEAR = new LinearEasingFunction();
    EasingFunction SINE = new SineEasingFunction();
    EasingFunction QUAD = new QuadraticEasingFunction();
    EasingFunction CUBIC = new CubicEasingFunction();
    EasingFunction QUART = new QuartEasingFunction();
    EasingFunction QUINT = new QuintEasingFunction();
    EasingFunction EXPO = new ExponentialEasingFunction();
    EasingFunction CIRC = new CircularEasingFunction();
    EasingFunction BACK = new BackEasingFunction();
    EasingFunction ELASTIC = new ElasticEasingFunction();
    EasingFunction BOUNCE = new BounceEasingFunction();

    float easeIn(final float x);

    float easeOut(final float x);

    float easeInOut(final float x);

}
