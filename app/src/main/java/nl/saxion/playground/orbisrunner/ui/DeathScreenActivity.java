package nl.saxion.playground.orbisrunner.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

import nl.saxion.playground.orbisrunner.R;
import nl.saxion.playground.orbisrunner.model.Level;
import nl.saxion.playground.orbisrunner.singleton.GameProvider;

/**
 * Death screen activity
 *
 * @author Joost Winkelman
 */
public class DeathScreenActivity extends AppCompatActivity {
    private int number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_death_screen);

        Level level = GameProvider.getCurrentLevel();

        if (!GameProvider.isCheatOn()) {
            // After death, remove coins you collected in the level
            int collected = GameProvider.getCoins() - level.getCollectedCoins();
            GameProvider.setCoins(collected);
            level.setCollectedCoins(0);
        }

        number = level.getNumber();

        init();
    }

    /**
     * Init buttons and show the title with the level you died on
     */
    private void init() {
        // Display level player died on
        TextView lvl = findViewById(R.id.diedOn);
        lvl.setText(String.format(Locale.ENGLISH, getString(R.string.you_died_on_level_), number));

        // Assign Retry Button
        Button retryButton = findViewById(R.id.retryButton);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retry Level
                Intent intent = new Intent(DeathScreenActivity.this, GameActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Assign Main Menu Button
        Button mainMenuButton = findViewById(R.id.mainMenuButton);
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to the starting screen
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        // openMainMenu();
        finish();
    }
}
