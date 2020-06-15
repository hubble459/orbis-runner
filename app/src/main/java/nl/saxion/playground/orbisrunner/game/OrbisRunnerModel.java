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

public class OrbisRunnerModel extends GameModel {
    private final Activity activity;
    private final Circle circle;
    private final Player player;
    private final Level level;
    //  private final MediaPlayer mediaPlayer;

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


//        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mediaPlayer) {
//                mediaPlayer.release();
//            }
//        });
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
                entity.reset();
                if (entity instanceof Sprite) {
                    ((Sprite) entity).setSpeedScale(level.getScale() / 2);
                }
                addEntity(entity);
            }
        }
    }

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

    public void finish() {
        player.setEnabled(false);
        for (Entity entity : getEntities()) {
            entity.setPaused(true);
        }

        Intent intent = new Intent(activity, FinishScreenActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    private void deadSound() {
        MediaPlayer mediaPlayer = MediaPlayer.create(activity, R.raw.oof);
        mediaPlayer.start();
    }

    @Override
    public float[] getXYFromDegrees(float degrees, float margin, Entity e) {
        return circle.getXYFromDegrees(degrees, margin, e);
    }
}
