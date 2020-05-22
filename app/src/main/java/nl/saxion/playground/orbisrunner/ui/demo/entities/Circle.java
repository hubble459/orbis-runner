package nl.saxion.playground.orbisrunner.ui.demo.entities;

import android.graphics.Color;
import android.graphics.Paint;

import nl.saxion.playground.orbisrunner.lib.Entity;
import nl.saxion.playground.orbisrunner.lib.GameView;

public class Circle extends Entity {
    private static final float STROKE_WIDTH = 100f;
    private float size;
    private int colour;

    public Circle(boolean black) {
        this.colour = black ? Color.BLACK : Color.WHITE;
    }

    @Override
    public void draw(GameView gv) {
        size = Math.min(gv.getWidth(), gv.getHeight()) - STROKE_WIDTH;
        Paint p = new Paint();
        p.setColor(colour);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(STROKE_WIDTH);
        float xMiddle = gv.getWidth() / 2f;
        float yMiddle = gv.getHeight() / 2f;
        gv.getCanvas().drawCircle(xMiddle, yMiddle, getRadiusOutside(), p);
    }

    public float getRadiusOutside() {
        return size / 2;
    }

    public float getRadiusInside() {
        return (size + STROKE_WIDTH / 2) / 2 - STROKE_WIDTH;
    }
}
