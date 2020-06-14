package nl.saxion.playground.orbisrunner.ui.demo.entities;

import android.graphics.Color;
import android.graphics.Paint;

import nl.saxion.playground.orbisrunner.R;
import nl.saxion.playground.orbisrunner.lib.GameView;
import nl.saxion.playground.orbisrunner.sprite.Sprite;

public class DemoEnemy extends Sprite {
    private Paint paint;

    public DemoEnemy() {
        paint = new Paint();
        paint.setColor(Color.RED);

        width = 50f * scale;
        height = 50f * scale;

        if (scale == 1f) {
            setMargin(-90f);
        } else if (scale == .5f) {
            setMargin(-20f);

        }
    }

    @Override
    public void draw(GameView gv) {
        width = 50f * scale;
        height = 50f * scale;

        draw(gv, false);
        gv.getCanvas().save();
        gv.getCanvas().rotate(angle, xVal + width / 2, yVal + height / 2);
        gv.getCanvas().drawRect(xVal + width, yVal + height, xVal, yVal, paint);
        gv.getCanvas().restore();
    }

    @Override
    public int getBitmapRes() {
        return R.drawable.demo_entity;
    }

    @Override
    public String getName() {
        return "Demo Enemy";
    }
}
