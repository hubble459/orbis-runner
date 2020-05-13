package nl.saxion.playground.template;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import nl.saxion.playground.orbisrunner.R;
import nl.saxion.playground.template.lib.GameModel;
import nl.saxion.playground.template.lib.GameView;
import nl.saxion.playground.template.spaceshooter.Activity;
import nl.saxion.playground.template.spaceshooter.Game;

public class TemplateActivity extends AppCompatActivity {

    GameView spaceShooterCanvas, platformerCanvas;
    GameModel spaceShooter, platformer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_activity_main);

        spaceShooterCanvas = findViewById(R.id.spaceShooter);
        spaceShooter = new Game();

        findViewById(R.id.spaceShooterText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TemplateActivity.this, Activity.class));
            }
        });

        platformerCanvas = findViewById(R.id.platformer);
        platformer = new nl.saxion.playground.template.platformer.Game();

        findViewById(R.id.platformerText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TemplateActivity.this, nl.saxion.playground.template.platformer.Activity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        spaceShooterCanvas.setGame(spaceShooter);
        platformerCanvas.setGame(platformer);
    }

    @Override
    protected void onPause() {
        super.onPause();
        spaceShooterCanvas.setGame(null);
        platformerCanvas.setGame(null);
    }
}
