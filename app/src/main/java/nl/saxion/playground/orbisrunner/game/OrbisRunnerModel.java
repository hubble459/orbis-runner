package nl.saxion.playground.orbisrunner.game;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.media.SoundPool;

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
    private final SoundPool sound;
    private final int DEATH_SOUND;

    public OrbisRunnerModel(Activity activity) {
        this.level = GameProvider.getCurrentLevel();

        this.circle = new Circle(true, true);
        this.circle.setSize(Circle.SIZE_DOUBLE);
        this.circle.setMargin(Circle.STROKE_WIDTH / 2);

        this.player = GameProvider.getPlayer();
        this.player.setGame(this);

        this.activity = activity;

        //this.sound = MediaPlayer.create(activity, R.raw.oof);
        sound = new SoundPool.Builder().build();
        DEATH_SOUND = sound.load(activity, R.raw.oof, 1);
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

        Intent intent = new Intent(activity, DeathScreenActivity.class);
        intent.putExtra(DeathScreenActivity.LEVEL, level.getNumber());
        activity.startActivity(intent);
        activity.finish();

        for (Entity entity : getEntities()) {
            entity.setPaused(true);
            entity.reset();
        }
    }

    private void deadSound() {
        sound.play(DEATH_SOUND, 1, 1, 0, 0, 1);
    }

    @Override
    public float[] getXYFromDegrees(float degrees, float margin, Entity e) {
        return circle.getXYFromDegrees(degrees, margin, e);
    }
}
