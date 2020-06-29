package nl.saxion.playground.orbisrunner.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import nl.saxion.playground.orbisrunner.R;
import nl.saxion.playground.orbisrunner.model.Level;
import nl.saxion.playground.orbisrunner.singleton.GameProvider;

/**
 * Finish screen activity
 *
 * @author Serwet Uz
 */
public class FinishScreenActivity extends AppCompatActivity {

    private Level level;
    private int collectibleCoins;
    private int collectedCoins;
    private int totalCoins;
    private int deathCounter;
    private boolean objectiveClaimed;

    /**
     * Get all values
     *
     * @param savedInstanceState unused
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_screen);

        level = GameProvider.getCurrentLevel();
        collectibleCoins = level.getCollectibleCoins();
        collectedCoins = level.getCollectedCoins();
        deathCounter = level.getDeathCounter();
        objectiveClaimed = level.getObjectiveClaimed();
        totalCoins = GameProvider.getCoins();

        init();

        checkObjective();

        level.setCollectedCoins(0);
        GameProvider.saveData(this);
    }

    /**
     * Initialize
     */
    private void init() {
        int max = Math.max(GameProvider.getCurrentLevel().getNumber(), GameProvider.getMaxLevel());
        GameProvider.setMaxLevel(max);

        Button nextLevelButton = findViewById(R.id.nextLevelButton);
        nextLevelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (GameProvider.nextLevel()) {
                    // Start game on new level and save
                    GameProvider.saveData(FinishScreenActivity.this);
                    intent = new Intent(FinishScreenActivity.this, GameActivity.class);
                } else {
                    // Open level selector
                    intent = new Intent(FinishScreenActivity.this, LevelSelectorActivity.class);
                }
                startActivity(intent);
                finish();
            }
        });

        Button mainMenuButton = findViewById(R.id.mainMenuButton);
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button retryButton = findViewById(R.id.retryLevelButton);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FinishScreenActivity.this, GameActivity.class);
                startActivity(intent);
                finish();
            }
        });

        TextView totalDeathsText = findViewById(R.id.totalDeaths);
        totalDeathsText.setText(String.valueOf(deathCounter));

        TextView totalCoinsText = findViewById(R.id.totalCoins);
        totalCoinsText.setText(String.format(Locale.ENGLISH, "%d / %d", collectedCoins, collectibleCoins));
    }

    /**
     * Check if you've cleared the objective (collected all coins)
     */
    private void checkObjective() {
        if (collectedCoins >= collectibleCoins) {
            if (!objectiveClaimed) {
                GameProvider.setCoins(totalCoins + 5);
                findViewById(R.id.bonusCoins).setVisibility(View.VISIBLE);
                level.setObjectiveClaimed(true);
            }
            objectiveCleared();
        }
    }

    /**
     * If cleared, show a green check instead of a red cross next to the objective
     */
    private void objectiveCleared() {
        ImageView imageView = findViewById(R.id.completed);
        imageView.setImageResource(R.drawable.green_tick);
    }
}
