package nl.saxion.playground.orbisrunner.game;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;

import nl.saxion.playground.orbisrunner.game.entity.Circle;
import nl.saxion.playground.orbisrunner.game.entity.Player;
import nl.saxion.playground.orbisrunner.lib.Entity;
import nl.saxion.playground.orbisrunner.lib.GameModel;
import nl.saxion.playground.orbisrunner.singleton.GameProvider;
import nl.saxion.playground.orbisrunner.ui.DeathScreenActivity;

public class OrbisRunnerModel extends GameModel {
    private final Activity activity;
    private final Circle circle;
    private final Player player;
    private final Level level;

    public OrbisRunnerModel(Activity activity) {
        this.level = GameProvider.getCurrentLevel();

        this.circle = new Circle(true, true);
        this.circle.setSize(Circle.SIZE_DOUBLE);
        this.circle.setMargin(Circle.STROKE_WIDTH);

        this.player = GameProvider.getPlayer();
        this.player.setGame(this);

        this.activity = activity;
    }

    @Override
    public void start(Canvas canvas) {
        addEntities();
    }

    private void addEntities() {
        addEntity(player);
        addEntity(circle);

        for (Entity entity : level.getEntities()) {
            entity.setGame(this);
            addEntity(entity);
        }
    }

    @Override
    public void dead() {
        Intent intent = new Intent(activity, DeathScreenActivity.class);
        intent.putExtra(DeathScreenActivity.LEVEL, level.getNumber());
        activity.finish();
        activity.startActivity(intent);
        GameProvider.getPlayer().setDead(false);
    }

    @Override
    public float[] getXYFromDegrees(float degrees, float margin, Entity e) {
        return circle.getXYFromDegrees(degrees, margin, e);
    }
}
