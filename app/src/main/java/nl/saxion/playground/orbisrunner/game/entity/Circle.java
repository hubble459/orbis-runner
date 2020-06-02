package nl.saxion.playground.orbisrunner.game.entity;

import android.graphics.Color;
import android.graphics.Paint;

import nl.saxion.playground.orbisrunner.lib.Entity;
import nl.saxion.playground.orbisrunner.lib.GameView;

public class Circle extends Entity {
    private static final float STROKE_WIDTH = 100f;
    private float size;
    private int colour;
    private Paint paint;

    public Circle(boolean black) {
        this.colour = black ? Color.BLACK : Color.WHITE;
        paint = new Paint();
        paint.setColor(colour);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(STROKE_WIDTH);
    }

    @Override
    public void draw(GameView gv) {
        float xMiddle = 0;
        float yMiddle = 0;
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
}
