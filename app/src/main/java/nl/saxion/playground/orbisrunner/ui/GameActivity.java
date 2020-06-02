package nl.saxion.playground.orbisrunner.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import nl.saxion.playground.orbisrunner.R;
import nl.saxion.playground.orbisrunner.game.OrbisRunnerModel;
import nl.saxion.playground.orbisrunner.lib.GameView;
import nl.saxion.playground.orbisrunner.singleton.GameProvider;

public class GameActivity extends AppCompatActivity {

    private OrbisRunnerModel or;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        or = new OrbisRunnerModel(GameProvider.getPlayer(), GameProvider.getCurrentLevel());

        GameView gv = findViewById(R.id.game);
        gv.setGame(or);
        gv.setBackgroundColor(Color.WHITE);

    }
}
