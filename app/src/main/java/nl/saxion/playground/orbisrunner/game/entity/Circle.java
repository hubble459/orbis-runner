package nl.saxion.playground.orbisrunner.game.entity;

import android.graphics.Color;
import android.graphics.Paint;

import nl.saxion.playground.orbisrunner.lib.Entity;
import nl.saxion.playground.orbisrunner.lib.GameView;

public class Circle extends Entity {
    public static final float SIZE_DOUBLE = -1;

    public static final float STROKE_WIDTH = 100f;

    private float size, margin, strokeWidth;
    private float xMiddle, yMiddle;

    private boolean onlyBottom;

    private Paint paint;

    public Circle(boolean black, boolean onlyBottom) {
        this.onlyBottom = onlyBottom;

        paint = new Paint();
        paint.setColor(black ? Color.BLACK : Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(STROKE_WIDTH);

        strokeWidth = STROKE_WIDTH;
    }

    public void setMargin(float margin) {
        this.margin = margin;
    }

    @Override
    public void draw(GameView gv) {
        if (size == 0 || size == SIZE_DOUBLE) {
            float sizeTMP = size;
            size = Math.min(gv.getWidth(), gv.getHeight()) - strokeWidth;
            if (sizeTMP == SIZE_DOUBLE) {
                size *= 2;
            }
        }

        if (xMiddle == 0 || yMiddle == 0) {
            xMiddle = gv.getWidth() / 2f;
            yMiddle = gv.getHeight() / 2f;
            if (onlyBottom)
                yMiddle -= (size / 2 - strokeWidth - margin) / 2;
        }

        gv.getCanvas().drawCircle(xMiddle, yMiddle, getRadiusOutside() + margin, paint);
    }

    private float getRadiusOutside() {
        return size / 2;
    }

    private float getRadiusInside() {
        return getRadiusOutside() - (strokeWidth);
    }

    public void setSize(float size) {
        this.size = size;
    }

    public float[] getXYFromDegrees(float degrees, float margin, Entity e) {
        margin += 10f;

        if (Float.isNaN(degrees)) {
            degrees = 0;
        }

        float width = 0;
        float height = 0;
        float jump = 0;
        if (e != null) {
            width = e.getWidth() / 2;
            height = e.getHeight() / 2;
            jump = e.getJump();
            margin += e.getMargin();
        }

        double radians = Math.toRadians(degrees);
        float[] xy = new float[3];
        xy[0] = (float) ((getRadiusInside() - margin - jump) * Math.cos(radians) + xMiddle - width);
        xy[1] = (float) ((getRadiusInside() - margin - jump) * Math.sin(radians) + yMiddle - height);
        xy[2] = degrees;
        return xy;
    }

    public void setStrokeWidth(float width) {
        strokeWidth = width;
        paint.setStrokeWidth(strokeWidth);
    }
}
