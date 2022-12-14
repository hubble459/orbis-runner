package nl.saxion.playground.orbisrunner.lib;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

/**
 * Animation utility
 *
 * @author Mostly Quentin & Joost
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
    }

    /**
     * Make a textView fade out in the span of 1000 ms
     *
     * @param fadeCoin TextView
     */
    public static void fadeOut(final TextView fadeCoin) {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(1000);
        valueAnimator.setFloatValues(1, 0);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                fadeCoin.setAlpha(value);
            }
        });
        valueAnimator.start();
    }

    /**
     * Make a textView count down and disappear when its zero
     *
     * @param coolDown TextView
     * @param time     time in MS
     */
    public static void countDown(final TextView coolDown, long time) {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(time);
        valueAnimator.setFloatValues(time, 0);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) (float) valueAnimator.getAnimatedValue();
                value = (int) (Math.round(value / 100.) * 100);
                coolDown.setText(String.valueOf(value));
                if (value == 0) {
                    coolDown.setVisibility(View.GONE);
                }
            }
        });
        valueAnimator.start();
    }
}
