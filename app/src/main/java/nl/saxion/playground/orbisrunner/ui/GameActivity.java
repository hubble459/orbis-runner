package nl.saxion.playground.orbisrunner.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import nl.saxion.playground.orbisrunner.R;
import nl.saxion.playground.orbisrunner.game.OrbisRunnerModel;
import nl.saxion.playground.orbisrunner.lib.GameView;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        OrbisRunnerModel or = new OrbisRunnerModel(this);
        GameView gv = findViewById(R.id.game);
        gv.setGame(or);
        gv.setBackgroundColor(Color.WHITE);

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, StartScreenActivity.class));
        finish();
    }
}
