package nl.saxion.playground.orbisrunner.model.game;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.TextView;

import nl.saxion.playground.orbisrunner.R;
import nl.saxion.playground.orbisrunner.lib.Animation;
import nl.saxion.playground.orbisrunner.lib.Entity;
import nl.saxion.playground.orbisrunner.lib.GameModel;
import nl.saxion.playground.orbisrunner.model.Level;
import nl.saxion.playground.orbisrunner.model.game.sprite.Circle;
import nl.saxion.playground.orbisrunner.model.game.sprite.Player;
import nl.saxion.playground.orbisrunner.model.game.sprite.Sprite;
import nl.saxion.playground.orbisrunner.model.game.sprite.Tutorial;
import nl.saxion.playground.orbisrunner.singleton.GameProvider;
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
    private TextView coinCounter;
    private TextView fadeCoin;
    private TextView coolDown;

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

    @Override
    public void started(Canvas canvas) {
        player.setXY();
    }

    /**
     * Add player and circle to game
     * Add all entities found in level entity list
     */
    private void addEntities() {
        addEntity(player);
        addEntity(circle);
        if (GameProvider.isFirstPlay()) {
            addEntity(new Tutorial(this));
        }

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

    /**
     * TextView to visualize coins
     *
     * @param coins TextView Counter
     */
    public void setCoinCounter(TextView coins) {
        this.coinCounter = coins;
    }

    /**
     * Set the coin counter thats at the top of the screen
     *
     * @param amount coin amount
     */
    public void setCoinCount(int amount) {
        if (coinCounter != null) {
            this.coinCounter.setText(String.valueOf(amount));
        }
    }

    /**
     * Set what text view should fade a '+1' when a coin gets collected
     *
     * @param fadeCoin TextView
     */
    public void setFadeCoin(TextView fadeCoin) {
        this.fadeCoin = fadeCoin;
    }

    /**
     * Called when a coin gets collected
     * Make the '+1' visible in fade it out in the span of 1000 MS
     */
    public void collectedCoin() {
        if (fadeCoin != null) {
            this.fadeCoin.setVisibility(View.VISIBLE);
            Animation.fadeOut(fadeCoin);
        }
    }

    /**
     * Set the coolDown TextView
     * Shows how much you have to wait to duck again in the bottom-left corner
     *
     * @param cool TextView
     */
    public void setCoolDown(TextView cool) {
        this.coolDown = cool;
    }

    /**
     * Make the textView countDown a given amount of time and disappear when done
     *
     * @param time in MS
     */
    public void coolDown(long time) {
        coolDown.setVisibility(View.VISIBLE);
        Animation.countDown(coolDown, time);
    }
}
