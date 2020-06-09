package nl.saxion.playground.orbisrunner.game;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.media.MediaPlayer;

import nl.saxion.playground.orbisrunner.R;
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
    private final MediaPlayer mediaPlayer;

    public OrbisRunnerModel(Activity activity) {
        this.level = GameProvider.getCurrentLevel();

        this.circle = new Circle(true, true);
        this.circle.setSize(Circle.SIZE_DOUBLE);
        this.circle.setMargin(Circle.STROKE_WIDTH);

        this.player = GameProvider.getPlayer();
        this.player.setGame(this);

        this.activity = activity;

        this.mediaPlayer = MediaPlayer.create(activity, R.raw.oof);
        this.mediaPlayer.setVolume(1.0f, 1.0f);
    }

    @Override
    public void start(Canvas canvas) {
        addEntities();
    }

    private void addEntities() {
        addEntity(player);
        addEntity(circle);

        for (Entity entity : level.getEntities()) {
            if (!getEntities().contains(entity)) {
                entity.setGame(this);
                entity.setPaused(false);
                entity.reset();
                addEntity(entity);
            }
        }
    }

    @Override
    public void dead() {
        deadSound();

        for (Entity entity : getEntities()) {
            entity.setPaused(true);
            entity.reset();
        }

        Intent intent = new Intent(activity, DeathScreenActivity.class);
        intent.putExtra(DeathScreenActivity.LEVEL, level.getNumber());
        activity.startActivity(intent);
        activity.finish();
    }

    private void deadSound() {
        mediaPlayer.start();
    }

    @Override
    public float[] getXYFromDegrees(float degrees, float margin, Entity e) {
        return circle.getXYFromDegrees(degrees, margin, e);
    }
}
