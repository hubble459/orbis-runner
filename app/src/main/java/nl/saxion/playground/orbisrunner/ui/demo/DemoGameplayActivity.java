package nl.saxion.playground.orbisrunner.ui.demo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import nl.saxion.playground.orbisrunner.R;
import nl.saxion.playground.orbisrunner.lib.GameView;
import nl.saxion.playground.orbisrunner.singleton.GameProvider;
import nl.saxion.playground.orbisrunner.ui.DeathScreenActivity;

public class DemoGameplayActivity extends AppCompatActivity {
    private static final String TAG = "GameplayDemoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay_demo);

        TextView speedometer = findViewById(R.id.speedometer);

        DemoOrbisGame og = new DemoOrbisGame(this, new DemoOrbisGame.OnDeathListener() {
            @Override
            public void dead() {
                Intent intent = new Intent(DemoGameplayActivity.this, DeathScreenActivity.class);
                intent.putExtra(DeathScreenActivity.LEVEL, GameProvider.getCurrentLevel().getNumber() /*get level from data*/);
                startActivity(intent);
                finish();
            }
        });
        og.setSpeedometer(speedometer);
        og.setLevel(GameProvider.getCurrentLevel().getNumber() /*get level from data*/);
        GameView gv = findViewById(R.id.demoGame);
        gv.setBackgroundColor(Color.WHITE);
        gv.setGame(og);
    }
}
