package nl.saxion.playground.orbisrunner.game.entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;

import nl.saxion.playground.orbisrunner.lib.Entity;
import nl.saxion.playground.orbisrunner.lib.GameView;

public abstract class Sprite extends Entity {
    private Bitmap bitmap;

    @Override
    public void draw(GameView gv) {
        super.draw(gv);

        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(gv.getContext().getResources(), getBitmapRes());
            width = bitmap.getWidth() * scale;
            height = bitmap.getHeight() * scale;
            if (levelMaker != null) {
                setXYValues(levelMaker.getXYFromDegrees(angle, jump, this));
            } else if (game != null) {
                setXYValues(game.getXYFromDegrees(angle, jump, this));
            }
        }

        if (bitmap != null) {
            gv.drawBitmap(bitmap, xVal, yVal, width, height, angle - 90);
        }
    }

    public void draw(GameView gv, boolean drawBitmap) {
        if (drawBitmap) {
            draw(gv);
        } else {
            super.draw(gv);
        }
    }

    @DrawableRes
    public abstract int getBitmapRes();

    public abstract String getName();

    public Sprite newInstance() throws IllegalAccessException, InstantiationException {
        return getClass().newInstance();
    }
}
