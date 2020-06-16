package nl.saxion.playground.orbisrunner.lib;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Animation utility
 */
public class Animation {
    /**
     * Will rotate the view given 360 degrees in a set amount of time
     * Can also be rotated in reverse
     *
     * @param view      the view to rotate
     * @param duration  the time it takes for one rotation in milliseconds
     * @param inReverse a true or false to rotate in reverse or not
     */
    public static void walkInCircle(final View view, int duration, boolean inReverse) {
        if (inReverse)
            view.setScaleX(-1);

        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setFloatValues(inReverse ? -360f : 360f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                view.setRotation(value);
            }
        });
        valueAnimator.start();
        view.setTag(valueAnimator);
    }

    /**
     * Same as the other one but reverse is in reverse
     */
    public static void walkInCircleSplash(final View view, int duration, boolean inReverse) {
        if (!inReverse)
            view.setScaleX(-1);

        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setFloatValues(inReverse ? -360f : 360f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                view.setRotation(value);
            }
        });
        valueAnimator.start();
        view.setTag(valueAnimator);
    }
}
