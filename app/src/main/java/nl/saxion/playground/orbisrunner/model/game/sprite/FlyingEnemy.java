package nl.saxion.playground.orbisrunner.model.game.sprite;

import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;

import nl.saxion.playground.orbisrunner.R;
import nl.saxion.playground.orbisrunner.lib.GameView;

/**
 * Bird Sprite
 */
public class FlyingEnemy extends Sprite {
    private AnimationDrawable bird;
    private int frames;
    private int frame;
    private long last;

    @Override
    public int getBitmapRes() {
        return R.drawable.sprite_bird;
    }

    @Override
    public String getName() {
        return "Bird";
    }

    /**
     * Draw frames of birb
     *
     * @param gv GameView
     */
    @Override
    public void draw(GameView gv) {
        if (bird == null) {
            bird = (AnimationDrawable) gv.getContext().getDrawable(R.drawable.sprite_bird);
            if (bird != null) {
                if (game != null) {
                    setXYValues(game.getXYFromDegrees(startAngle, startJump, this));
                } else if (levelMaker != null) {
                    setXYValues(levelMaker.getXYFromDegrees(startAngle, startJump, this));
                }
                width = bird.getIntrinsicWidth() * scale;
                height = bird.getIntrinsicHeight() * scale;
                frames = bird.getNumberOfFrames();
                last = System.currentTimeMillis();
            }
        }

        if (bird != null) {
            // Reset scale if needed
            if (width != bird.getIntrinsicWidth() * scale) {
                width = bird.getIntrinsicWidth() * scale;
                height = bird.getIntrinsicHeight() * scale;
            }

            long now = System.currentTimeMillis();
            if (last < now - bird.getDuration(frame)) {
                ++frame;
                if (frame >= frames) frame = 0;
                last = now;
            }

            Canvas c = gv.getCanvas();
            c.save();
            c.rotate(angle - 90, xVal + width / 2, yVal + height / 2);
            bird.getFrame(frame).setBounds(
                    (int) (xVal),
                    (int) (yVal),
                    (int) (xVal + width),
                    (int) (yVal + height));
            bird.getFrame(frame).draw(c);
            c.restore();
        }

        super.draw(gv, false);
    }
}
