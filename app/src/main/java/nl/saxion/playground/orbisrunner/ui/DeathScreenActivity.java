package nl.saxion.playground.orbisrunner.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

import nl.saxion.playground.orbisrunner.R;
import nl.saxion.playground.orbisrunner.game.Level;
import nl.saxion.playground.orbisrunner.singleton.GameProvider;

/**
 * Starting activity
 */
public class DeathScreenActivity extends AppCompatActivity {
    public static final String LEVEL = "level_key";
    private int number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_death_screen);


        Level level = GameProvider.getCurrentLevel();
        number = level.getNumber();

        init();
    }

    private void init() {
        // Display level player died on
        TextView lvl = findViewById(R.id.diedOn);
        lvl.setText(String.format(Locale.ENGLISH, "You Died On Level %d", number));

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
                // openMainMenu();
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        // openMainMenu();
        finish();
    }

    private void openMainMenu() {
        Intent intent = new Intent(DeathScreenActivity.this, StartScreenActivity.class);
        startActivity(intent);
        finish();
    }
}
