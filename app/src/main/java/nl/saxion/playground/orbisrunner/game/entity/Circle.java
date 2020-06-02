package nl.saxion.playground.orbisrunner.game.entity;

import android.graphics.Color;
import android.graphics.Paint;

import nl.saxion.playground.orbisrunner.lib.Entity;
import nl.saxion.playground.orbisrunner.lib.GameView;

public class Circle extends Entity {
    public static final float SIZE_DOUBLE = -1;

    public static final float STROKE_WIDTH = 100f;

    private float size, margin;
    private float xMiddle, yMiddle;

    private Paint paint;

    public Circle(boolean black) {
        paint = new Paint();
        paint.setColor(black ? Color.BLACK : Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(STROKE_WIDTH);
    }

    public void setMargin(float margin) {
        this.margin = margin;
    }

    @Override
    public void draw(GameView gv) {
        if (size == 0 || size == SIZE_DOUBLE) {
            float sizeTMP = size;
            size = Math.min(gv.getWidth(), gv.getHeight()) - STROKE_WIDTH;
            if (sizeTMP == SIZE_DOUBLE) {
                size *= 2;
            }
        }

        if (xMiddle == 0) {
            xMiddle = gv.getWidth() / 2f;
            yMiddle = gv.getHeight() / 2f;
        }

        gv.getCanvas().drawCircle(xMiddle, yMiddle, getRadiusOutside() + margin, paint);
    }

    public float getRadiusOutside() {
        return size / 2;
    }

    public float getRadiusInside() {
        return (size + STROKE_WIDTH / 2) / 2 - STROKE_WIDTH;
    }

    public void setSize(float size) {
        this.size = size;
    }
}
