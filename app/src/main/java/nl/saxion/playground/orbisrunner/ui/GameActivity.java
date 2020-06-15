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

public class GameActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

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

    @Override
    protected void onStart() {
        startMusic();
        super.onStart();
    }

    private void startMusic() {
        if (GameProvider.isMusicOn()) {
            mediaPlayer = MediaPlayer.create(this, randomMusic());
            mediaPlayer.start();
        }
    }

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

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onPause() {
        mediaPlayer.stop();
        mediaPlayer.release();
        super.onPause();
    }
}
