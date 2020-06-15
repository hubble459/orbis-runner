package nl.saxion.playground.orbisrunner.game;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.media.SoundPool;

import nl.saxion.playground.orbisrunner.R;
import nl.saxion.playground.orbisrunner.lib.Entity;
import nl.saxion.playground.orbisrunner.lib.GameModel;
import nl.saxion.playground.orbisrunner.singleton.GameProvider;
import nl.saxion.playground.orbisrunner.sprite.Circle;
import nl.saxion.playground.orbisrunner.sprite.Player;
import nl.saxion.playground.orbisrunner.sprite.Sprite;
import nl.saxion.playground.orbisrunner.ui.DeathScreenActivity;
import nl.saxion.playground.orbisrunner.ui.FinishScreenActivity;

public class OrbisRunnerModel extends GameModel {
    private final Activity activity;
    private final Circle circle;
    private final Player player;
    private final Level level;
    private final SoundPool sound;
    private final int DEATH_SOUND;

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

        sound = new SoundPool.Builder().build();
        DEATH_SOUND = sound.load(activity, R.raw.oof, 1);
    }

    /**
     * When game starts this gets run
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

        deathSound();

        Intent intent = new Intent(activity, DeathScreenActivity.class);
        activity.startActivity(intent);
        activity.finish();
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
    private void deathSound() {
        sound.play(DEATH_SOUND, 1, 1, 0, 0, 1);
    }

    @Override
    public float[] getXYFromDegrees(float degrees, float margin, Entity e) {
        return circle.getXYFromDegrees(degrees, margin, e);
    }
}
