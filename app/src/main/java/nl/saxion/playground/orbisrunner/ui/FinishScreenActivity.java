package nl.saxion.playground.orbisrunner.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import nl.saxion.playground.orbisrunner.R;
import nl.saxion.playground.orbisrunner.game.Level;
import nl.saxion.playground.orbisrunner.singleton.GameProvider;

public class FinishScreenActivity extends AppCompatActivity {

    private Level level;
    private int collectableCoins;
    private int collectedCoins;
    private int totalCoins;
    private int deathCounter;
    private boolean objectiveClaimed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_screen);

        checkObjective();

        level = GameProvider.getCurrentLevel();
        collectableCoins = level.getCollectableCoins();
        deathCounter = level.getDeathCounter();

        init();

    }

    private void init() {

        Button mainMenuButton = findViewById(R.id.mainMenuButton);
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FinishScreenActivity.this, StartScreenActivity.class);
                startActivity(intent);
            }
        });

    }

    private void checkObjective() {
        if (deathCounter == 0) {
            if (collectedCoins == collectableCoins) {
                objectiveCleared();
            }
        }
    }

    private void objectiveCleared() {
        ImageView imageView = findViewById(R.id.completed);
        imageView.setImageResource(R.drawable.green_tick);

        totalCoins = GameProvider.getCoins();
        totalCoins = totalCoins + 5;

        //level.setDeathCounter(0);

    }


}
