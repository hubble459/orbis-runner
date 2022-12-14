package nl.saxion.playground.orbisrunner.ui;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;
import java.util.Random;

import nl.saxion.playground.orbisrunner.R;
import nl.saxion.playground.orbisrunner.lib.Animation;
import nl.saxion.playground.orbisrunner.singleton.GameProvider;

/**
 * Starting activity
 *
 * @author Quentin Correia
 */
public class StartScreenActivity extends AppCompatActivity {
    private TextView lvl;

    /**
     * Call the init on create
     *
     * @param savedInstanceState unused
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        init();
    }

    /**
     * Set all values and start animations
     */
    private void init() {
        // Animate Player W A L K I N G
        ImageView player = findViewById(R.id.player);
        ((AnimationDrawable) player.getBackground()).start();
        Animation.walkInCircle(findViewById(R.id.rotateView), 7000, new Random().nextBoolean());

        // Set level title
        lvl = findViewById(R.id.level);
        updateLevelNumber();

        // Assign Play Button
        Button playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the game
                Intent game = new Intent(StartScreenActivity.this, GameActivity.class);
                startActivity(game);
            }
        });

        // Assign Customize Button
        TextView customizeButton = findViewById(R.id.customizeButton);
        customizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the customization intent
                Intent intent = new Intent(StartScreenActivity.this, CustomizationActivity.class);
                startActivity(intent);
            }
        });

        // Assign Customize Button
        TextView shopButton = findViewById(R.id.shopButton);
        shopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the customization intent
                Intent shop = new Intent(StartScreenActivity.this, ShopActivity.class);
                startActivity(shop);
            }
        });

        // Assign New Game Button
        Button newGameButton = findViewById(R.id.levelSelectButton);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Clear saved level progress and start game on level 0
                Intent game = new Intent(StartScreenActivity.this, LevelSelectorActivity.class);
                startActivity(game);
            }
        });

        // Assign Settings Button
        Button settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start settings activity
                Intent settings = new Intent(StartScreenActivity.this, SettingScreenActivity.class);
                startActivity(settings);
            }
        });
    }

    private void updateLevelNumber() {
        lvl.setText(String.format(Locale.ENGLISH, "LVL %d", GameProvider.getCurrentLevel().getNumber()) /* get level from save instance */);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (lvl != null) {
            updateLevelNumber();
        }
    }
}
