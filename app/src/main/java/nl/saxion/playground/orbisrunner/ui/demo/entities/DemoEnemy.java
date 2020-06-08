package nl.saxion.playground.orbisrunner.ui.demo.entities;

import android.graphics.Color;
import android.graphics.Paint;

import nl.saxion.playground.orbisrunner.lib.Entity;
import nl.saxion.playground.orbisrunner.lib.GameView;

public class DemoEnemy extends Entity {
    private Paint paint;

    public DemoEnemy() {
        paint = new Paint();
        paint.setColor(Color.RED);
        width = 50f;
        height = 50f;
    }

    @Override
    public void draw(GameView gv) {
        super.draw(gv);
        gv.getCanvas().save();
        gv.getCanvas().rotate(angle, xVal + width / 2, yVal + height / 2);
        gv.getCanvas().drawRect(xVal + width, yVal + height, xVal, yVal, paint);
        gv.getCanvas().restore();
    }

    @Override
    public int getLayer() {
        return 1;
    }
}
