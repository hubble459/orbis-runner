package nl.saxion.playground.orbisrunner.model.game.sprite;

import android.graphics.Color;
import android.graphics.Paint;

import nl.saxion.playground.orbisrunner.lib.Entity;
import nl.saxion.playground.orbisrunner.lib.GameView;

/**
 * Circle sprite
 */
public class Circle extends Entity {
    public static final float SIZE_DOUBLE = -1;

    public static final float STROKE_WIDTH = 100f;

    private float superSize;
    private float size, strokeWidth;
    private float xMiddle, yMiddle;
    private float scale;

    private boolean onlyBottom;

    private Paint paint;

    /**
     * Set-up paint, scale and strokeWidth
     *
     * @param black      circle color is blacl
     * @param onlyBottom show only the bottom half of the circle
     */
    public Circle(boolean black, boolean onlyBottom) {
        this.onlyBottom = onlyBottom;
        scale = 1;

        paint = new Paint();
        paint.setColor(black ? Color.BLACK : Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(STROKE_WIDTH);

        strokeWidth = STROKE_WIDTH;
    }

    /**
     * Draw the circle
     *
     * @param gv The `GameView` to draw to.
     */
    @Override
    public void draw(GameView gv) {
        if (size == 0 || size == SIZE_DOUBLE) {
            size = Math.min(gv.getWidth() - strokeWidth, gv.getHeight() - strokeWidth);
            size /= scale;
            if (superSize == SIZE_DOUBLE) {
                size *= 2;
            }
        }

        if (xMiddle == 0 || yMiddle == 0) {
            xMiddle = gv.getWidth() / 2f;
            yMiddle = gv.getHeight() / 2f;
            if (onlyBottom) {
                yMiddle += (yMiddle / 2) - size / 2;
            }
        }

        gv.getCanvas().drawCircle(xMiddle, yMiddle, getRadiusOutside(), paint);
    }

    /**
     * Get radius including the strokeWidth
     *
     * @return radius
     */
    private float getRadiusOutside() {
        return size / 2;
    }

    /**
     * Get radius excluding the strokeWidth
     *
     * @return radius
     */
    private float getRadiusInside() {
        return getRadiusOutside() - strokeWidth;
    }

    public void setSize(float size, float scale) {
        this.scale = scale;
        this.size = size;
        this.superSize = size;
    }

    /**
     * Calculate the X and Y points in the circle from the degrees
     *
     * @param degrees degrees
     * @param margin  margin
     * @param e       Entity needed for dimensions
     * @return XY[]
     */
    public float[] getXYFromDegrees(float degrees, float margin, Entity e) {
        margin = Math.max(25f, this.margin + margin);

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

    public float getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(float width) {
        strokeWidth = width;
        paint.setStrokeWidth(strokeWidth);
        size = superSize;
    }
}
