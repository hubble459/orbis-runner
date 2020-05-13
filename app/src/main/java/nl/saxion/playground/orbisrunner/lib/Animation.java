package nl.saxion.playground.orbisrunner.lib;

import android.animation.ValueAnimator;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

public class Animation {
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

    public static void stop(View view) {
        if (view instanceof ImageView) {
            Drawable drawable = ((ImageView) view).getDrawable();
            if (drawable instanceof AnimationDrawable) {
                ((AnimationDrawable) drawable).stop();
            }
        }
        if (view.getTag() instanceof ValueAnimator) {
            ((ValueAnimator) view.getTag()).cancel();
        }
    }
}
