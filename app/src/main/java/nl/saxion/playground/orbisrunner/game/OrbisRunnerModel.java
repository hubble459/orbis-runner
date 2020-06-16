package nl.saxion.playground.orbisrunner.game;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.media.MediaPlayer;

import nl.saxion.playground.orbisrunner.R;
import nl.saxion.playground.orbisrunner.lib.Entity;
import nl.saxion.playground.orbisrunner.lib.GameModel;
import nl.saxion.playground.orbisrunner.singleton.GameProvider;
import nl.saxion.playground.orbisrunner.sprite.Circle;
import nl.saxion.playground.orbisrunner.sprite.Player;
import nl.saxion.playground.orbisrunner.sprite.Sprite;
import nl.saxion.playground.orbisrunner.ui.DeathScreenActivity;
import nl.saxion.playground.orbisrunner.ui.FinishScreenActivity;

/**
 * Model used by the GameView to run the game
 */
public class OrbisRunnerModel extends GameModel {
    private final Activity activity;
    private final Circle circle;
    private final Player player;
    private final Level level;
    private final MediaPlayer mediaPlayer;

    /**
     * When a new game gets created game will reset all values
     * Scale = 1
     * new Circle
     * Player#reset()
     *
     * @param activity used for context needed for sounds
     */
    public OrbisRunnerModel(Activity activity) {
        Entity.setScale(1);

        this.level = GameProvider.getCurrentLevel();

        this.circle = new Circle(true, true);
        this.circle.setSize(Circle.SIZE_DOUBLE, level.getScale());
        this.circle.setMargin(circle.getStrokeWidth() / 2);

        this.player = GameProvider.getPlayer();
        this.player.reset();
        this.player.setGame(this);

        this.activity = activity;

        this.mediaPlayer = MediaPlayer.create(activity, R.raw.oof);
    }

    /**
     * When game starts this gets called
     *
     * @param canvas canvas
     */
    @Override
    public void start(Canvas canvas) {
        addEntities();
    }

    /**
     * Add player and circle to game
     * Add all entities found in level entity list
     */
    private void addEntities() {
        addEntity(player);
        addEntity(circle);

        for (Entity entity : level.getEntities()) {
            if (!getEntities().contains(entity)) {
                entity.setGame(this);
                entity.reset();
                if (entity instanceof Sprite) {
                    ((Sprite) entity).setSpeedScale(level.getScale() / 2);
                }
                addEntity(entity);
            }
        }
    }

    /**
     * If player dies it will call this method
     * Plays a death sound
     * Starts the death screen activity
     */
    public void dead() {
        for (Entity entity : getEntities()) {
            entity.setPaused(true);
        }

        deadSound();

        Intent intent = new Intent(activity, DeathScreenActivity.class);
        activity.startActivity(intent);
        activity.finish();

        level.setCollectedCoins(0);
        level.death();
        GameProvider.saveData(activity);
    }

    /**
     * When player reaches the finish, this gets called
     */
    public void finish() {
        player.setEnabled(false);
        for (Entity entity : getEntities()) {
            entity.setPaused(true);
        }

        Intent intent = new Intent(activity, FinishScreenActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    /**
     * Play sound
     */
    private void deadSound() {
        if (GameProvider.isSoundOn()) {
            mediaPlayer.start();
        } else {
            mediaPlayer.release();
        }

    }

    /**
     * Get XY from degrees calculated with circle width and height
     *
     * @param degrees angle in circle
     * @param margin  margin
     * @param e       entity used for dimensions
     * @return float[3] 0 being x; 1 being y; 2 being the angle
     */
    @Override
    public float[] getXYFromDegrees(float degrees, float margin, Entity e) {
        return circle.getXYFromDegrees(degrees, margin, e);
    }
}
