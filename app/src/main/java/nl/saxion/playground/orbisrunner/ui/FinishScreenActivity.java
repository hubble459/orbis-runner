package nl.saxion.playground.orbisrunner.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import nl.saxion.playground.orbisrunner.R;
import nl.saxion.playground.orbisrunner.game.Level;
import nl.saxion.playground.orbisrunner.singleton.GameProvider;

public class FinishScreenActivity extends AppCompatActivity {

    private Level level;
    private int collectibleCoins;
    private int collectedCoins;
    private int totalCoins;
    private int deathCounter;
    private boolean objectiveClaimed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_screen);

        level = GameProvider.getCurrentLevel();
        collectibleCoins = level.getCollectibleCoins();
        deathCounter = level.getDeathCounter();
        objectiveClaimed = level.getObjectiveClaimed();
        collectedCoins = level.getCollectedCoins();
        totalCoins = GameProvider.getCoins();

        init();

        checkObjective();

        level.setCollectedCoins(0);
        GameProvider.saveData(this);
    }

    private void init() {
        Button nextLevelButton = findViewById(R.id.nextLevelButton);
        nextLevelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameProvider.nextLevel();

                Intent intent = new Intent(FinishScreenActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });

        Button mainMenuButton = findViewById(R.id.mainMenuButton);
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FinishScreenActivity.this, StartScreenActivity.class);
                startActivity(intent);
            }
        });

        Button retryButton = findViewById(R.id.retryLevelButton);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FinishScreenActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });

        TextView totalCoinsText = findViewById(R.id.totalCoins);
        totalCoinsText.setText(String.valueOf(collectedCoins));

        TextView totalDeathsText = findViewById(R.id.totalDeaths);
        totalDeathsText.setText(String.valueOf(deathCounter));

    }

    private void checkObjective() {
        if (collectedCoins == collectibleCoins) {
            if (!objectiveClaimed) {
                GameProvider.setCoins(totalCoins + 5);
                level.setObjectiveClaimed(true);
            }
            objectiveCleared();
        }
    }

    private void objectiveCleared() {
        ImageView imageView = findViewById(R.id.completed);
        imageView.setImageResource(R.drawable.green_tick);
    }

}
