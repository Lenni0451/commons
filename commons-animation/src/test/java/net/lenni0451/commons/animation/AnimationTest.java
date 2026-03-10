package net.lenni0451.commons.animation;

import net.lenni0451.commons.animation.clock.TickClock;
import net.lenni0451.commons.animation.easing.EasingFunction;
import net.lenni0451.commons.animation.easing.EasingMode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnimationTest {

    @Test
    void testClock() {
        ManualClock clock = new ManualClock();
        Animation anim = new Animation()
                .setClock(clock)
                .frame(f -> f.duration(100L).start(0).end(100).easingFunction(EasingFunction.LINEAR).easingMode(EasingMode.EASE_IN).easingBehavior(EasingBehavior.KEEP))
                .frame(f -> f.duration(100L).end(200));

        anim.start();
        assertEquals(0, anim.getValue(), 0.1);

        clock.time += 50;
        assertEquals(50, anim.getValue(), 0.1);

        clock.time += 60; // 110 total, exceeds first frame (100)
        float value = anim.getValue();
        assertEquals(110, value, 0.1);
        assertEquals(1, anim.getCurrentFrame());

        clock.time += 100; // 210 total, exceeds second frame (200)
        assertEquals(200, anim.getValue(), 0.1);
        assertTrue(anim.isFinished());
    }

    @Test
    void testDynamicAnimationMultiValue() {
        ManualClock clock = new ManualClock();
        DynamicAnimation dynamic = new DynamicAnimation(EasingFunction.LINEAR, EasingMode.EASE_IN, 100L, new float[]{0, 0})
                .setClock(clock);

        dynamic.setTarget(100, 200);
        clock.time += 50;
        float[] values = dynamic.getValues();
        assertEquals(50, values[0], 0.1);
        assertEquals(100, values[1], 0.1);

        clock.time += 50;
        values = dynamic.getValues();
        assertEquals(100, values[0], 0.1);
        assertEquals(200, values[1], 0.1);
        assertFalse(dynamic.isRunning());
    }

    @Test
    void testLinearInverse() {
        assertEquals(0.5f, EasingFunction.LINEAR.getInverse(EasingMode.EASE_IN, 0.5f));
    }


    private static class ManualClock implements TickClock {
        private long time = 1000;

        @Override
        public long getTime() {
            return this.time;
        }
    }

}
