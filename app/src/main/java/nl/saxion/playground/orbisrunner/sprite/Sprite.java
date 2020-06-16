package nl.saxion.playground.orbisrunner.sprite;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.DrawableRes;

import nl.saxion.playground.orbisrunner.lib.Entity;
import nl.saxion.playground.orbisrunner.lib.GameView;

public abstract class Sprite extends Entity {
    private Bitmap bitmap;
    private Paint paint;
    private float speedScale;

    @Override
    public void draw(GameView gv) {
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(gv.getContext().getResources(), getBitmapRes());
            width = bitmap.getWidth() * scale;
            height = bitmap.getHeight() * scale;
            if (levelMaker != null) {
                setXYValues(levelMaker.getXYFromDegrees(startAngle, jump, this));
            } else if (game != null) {
                setXYValues(game.getXYFromDegrees(startAngle, jump, this));
            }
        }

        if (bitmap != null) {
            // Reset scale if needed
            if (width != bitmap.getWidth() * scale) {
                width = bitmap.getWidth() * scale;
                height = bitmap.getHeight() * scale;
            }
            gv.drawBitmap(bitmap, xVal, yVal, width, height, angle - 90);
        }

        drawOutline(gv.getCanvas());
    }

    public void draw(GameView gv, boolean drawBitmap) {
        if (drawBitmap) {
            draw(gv);
        } else {
            drawOutline(gv.getCanvas());
        }
    }

    private void drawOutline(Canvas canvas) {
        if (levelMaker != null && isSelected()) {
            if (paint == null) {
                paint = new Paint();
                paint.setColor(Color.CYAN);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(10f);

                setXYValues(levelMaker.getXYFromDegrees(angle, 0, this));
            }

            canvas.save();
            canvas.drawCircle(xVal + width / 2, yVal + height / 2, width, paint);
            canvas.restore();
        }
    }

    @Override
    public void tick() {
        if (game != null && !pause) {
            if (reset) {
                angle = startAngle - .3f * getSpeedScale();
                reset = false;
            }
            angle += .3f * getSpeedScale();
            if (angle > 360) angle = 0;
            setXYValues(game.getXYFromDegrees(angle, jump, this));
        }
    }

    @DrawableRes
    public abstract int getBitmapRes();

    public abstract String getName();

    public Sprite newInstance() throws IllegalAccessException, InstantiationException {
        return getClass().newInstance();
    }

    public float getSpeedScale() {
        return Float.isNaN(speedScale) ? 1 : (speedScale * 2);
    }

    public void setSpeedScale(float speedScale) {
        this.speedScale = speedScale;
    }
}
