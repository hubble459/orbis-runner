package nl.saxion.playground.orbisrunner.ui.demo.entities;

import android.graphics.Color;
import android.graphics.Paint;

import nl.saxion.playground.orbisrunner.lib.Entity;
import nl.saxion.playground.orbisrunner.lib.GameView;

public class DemoCircle extends Entity {
    private static final float STROKE_WIDTH = 50f;
    private float size;
    private float xMiddle, yMiddle;

    private Paint paint;

    public DemoCircle(boolean black) {
        int colour = black ? Color.BLACK : Color.WHITE;
        paint = new Paint();
        paint.setColor(colour);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(STROKE_WIDTH);
    }

    @Override
    public void draw(GameView gv) {
        if (size == 0) {
            size = Math.min(gv.getWidth(), gv.getHeight()) - STROKE_WIDTH;
            xMiddle = gv.getWidth() / 2f;
            yMiddle = gv.getHeight() / 2f;
        }
        gv.getCanvas().drawCircle(xMiddle, yMiddle, getRadiusOutside(), paint);
    }

    public float getRadiusOutside() {
        return size / 2;
    }

    public float getRadiusInside() {
        return (size + STROKE_WIDTH / 2) / 2 - STROKE_WIDTH;
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
