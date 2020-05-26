package nl.saxion.playground.orbisrunner.ui.demo.entities;

import android.graphics.Bitmap;
import android.graphics.Color;

import nl.saxion.playground.orbisrunner.R;
import nl.saxion.playground.orbisrunner.lib.Entity;
import nl.saxion.playground.orbisrunner.lib.GameView;

public class Enemy extends Entity {
    private float xVal, yVal;
    private float width, height;
    private float angle;
    private Bitmap bitmap;

    public Enemy(float[] xy) {
        xVal = xy[0];
        yVal = xy[1];
        angle = xy[2];
    }

    @Override
    public void draw(GameView gv) {
        if (bitmap == null) {
            bitmap = gv.getBitmap(R.drawable.demo_entity, Color.RED);
            width = bitmap.getWidth();
            height = bitmap.getHeight();
        }
        gv.drawBitmap(bitmap, xVal - width / 2, yVal - height / 2, width, height, angle);
    }

    boolean inHitbox(float x, float y, float width, float height) {
        float centerX = xVal + this.width / 2;
        float centerY = yVal + this.height / 2;

        if (centerY >= y && centerY <= y + height) {
            return centerX >= x && centerX <= x + width;
        }
        return false;
    }
}
