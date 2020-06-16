package nl.saxion.playground.orbisrunner.ui;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Random;

import nl.saxion.playground.orbisrunner.R;
import nl.saxion.playground.orbisrunner.game.OrbisRunnerModel;
import nl.saxion.playground.orbisrunner.lib.Entity;
import nl.saxion.playground.orbisrunner.lib.GameView;
import nl.saxion.playground.orbisrunner.singleton.GameProvider;

/**
 * Game activity
 */
public class GameActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;

    /**
     * Init stuff on create
     *
     * @param savedInstanceState unused
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        OrbisRunnerModel or = new OrbisRunnerModel(this);
        GameView gv = findViewById(R.id.game);
        gv.setGame(or);
        gv.setBackgroundColor(Color.WHITE);

        for (Entity entity : or.getEntities()) {
            entity.reset();
            entity.setGame(or);
        }
    }

    /**
     * Start music on start
     * <p>
     * When you rotate the screen, music will restart
     */
    @Override
    protected void onStart() {
        startMusic();
        super.onStart();
    }

    /**
     * Start music if turned on
     */
    private void startMusic() {
        if (GameProvider.isMusicOn()) {
            mediaPlayer = MediaPlayer.create(this, randomMusic());
            mediaPlayer.start();
        }
    }

    /**
     * Get a random song
     *
     * @return random song resource
     */
    private int randomMusic() {
        switch (new Random().nextInt(3)) {
            case 0:
                return R.raw.ddk2_stickerbush_symphony;
            case 1:
                return R.raw.eternal_champions_character_bios;
            default:
                return R.raw.smk_koopa_beach;
        }
    }

    /**
     * Finish the activity when the back button is pressed
     * <p>
     * For some reason super.onBackPressed() doesn't work
     */
    @Override
    public void onBackPressed() {
        finish();
    }

    /**
     * When the activity is paused, stop the music
     */
    @Override
    protected void onPause() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        super.onPause();
    }
}
