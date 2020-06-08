package nl.saxion.playground.orbisrunner.ui.demo;

import android.content.Context;
import android.graphics.Canvas;
import android.widget.TextView;

import nl.saxion.playground.orbisrunner.lib.GameModel;
import nl.saxion.playground.orbisrunner.ui.demo.entities.DemoCircle;
import nl.saxion.playground.orbisrunner.ui.demo.entities.DemoEnemy;
import nl.saxion.playground.orbisrunner.ui.demo.entities.DemoPlayer;

public class DemoOrbisGame extends GameModel {
    public TextView speedometer;
    private float[] centerXY;
    private int level;
    private DemoCircle demoCircle;
    private Context context;
    private OnDeathListener listener;

    DemoOrbisGame(Context context, OnDeathListener listener) {
        this.context = context;
        this.listener = listener;
    }

    void setLevel(int level) {
        this.level = level;
    }

    @Override
    public void start(Canvas canvas) {
        centerXY = new float[]{canvas.getWidth() / 2f, canvas.getHeight() / 2f};
        demoCircle = new DemoCircle(true);
        addEntity(demoCircle);
    }

    @Override
    public void started(Canvas canvas) {
        // Set player
        DemoPlayer demoPlayer = new DemoPlayer(this, 0);
        addEntity(demoPlayer);

        // Set enemies from level data
        setDummyEnemies();
    }

    private void setDummyEnemies() {
        for (int i = 0; i < 5; i++) {
            float rand = (float) (Math.random() * 360);
            while (rand >= 0 && rand < 10f) {
                rand = (float) (Math.random() * 360);
            }
            DemoEnemy e = new DemoEnemy();
            e.setXYValues(getXYFromDegrees(rand, 0));
            addEntity(e);
        }
    }

    public float[] getXYFromDegrees(float degrees, float margin) {
        double radians = Math.toRadians(degrees);

        float[] xy = new float[3];
        float radius = demoCircle.getRadiusInside() - margin;

        xy[0] = (float) (radius * Math.cos(radians) + centerXY[0]);
        xy[1] = (float) (radius * Math.sin(radians) + centerXY[1]);
        xy[2] = degrees;

        return xy;
    }

    void setSpeedometer(TextView speedometer) {
        this.speedometer = speedometer;
    }

    public Context getContext() {
        return context;
    }

    public void dead() {
        listener.dead();
    }

    public interface OnDeathListener {
        void dead();
    }
}