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
            yMiddle = gv.getHeight() / 2f - (size / 2 - STROKE_WIDTH - margin) / 2;
        }

        gv.getCanvas().drawCircle(xMiddle, yMiddle, getRadiusOutside() + margin, paint);
    }

    private float getRadiusOutside() {
        return size / 2;
    }

    private float getRadiusInside() {
        return (size + STROKE_WIDTH / 2) / 2 - STROKE_WIDTH;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public float[] getXYFromDegrees(float degrees, float margin) {
        double radians = Math.toRadians(degrees);
        float[] xy = new float[3];
        xy[0] = (float) ((getRadiusInside() - margin) * Math.cos(radians) + xMiddle);
        xy[1] = (float) ((getRadiusInside() - margin) * Math.sin(radians) + yMiddle);
        xy[2] = degrees;
        return xy;
    }
}
