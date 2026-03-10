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

    /**
     * Approximate the time progress for the given wanted easing output.
     *
     * @param easingMode   The easing mode
     * @param wantedOutput The wanted output
     * @return The approximated time progress
     */
    default float getInverse(final EasingMode easingMode, final float wantedOutput) {
        float low = 0;
        float high = 1;
        float mid;
        while (high - low > 0.00001F) {
            mid = (low + high) / 2;
            float output = easingMode.call(this, mid);
            if (output < wantedOutput) {
                low = mid;
            } else {
                high = mid;
            }
        }
        return (low + high) / 2;
    }

}
