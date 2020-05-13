package nl.saxion.playground.orbisrunner.ui;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import nl.saxion.playground.orbisrunner.R;
import nl.saxion.playground.orbisrunner.lib.Animation;

public class StartScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        init();
    }

    private void init() {
        // Animate Playing W A L K I N G
        ImageView player = findViewById(R.id.player);
        ((AnimationDrawable) player.getDrawable()).start();
        Animation.walkInCircle(player, 4000);

        // Set level title
        TextView lvl = findViewById(R.id.level);
        lvl.setText("LVL 3" /* get level from save instance */);

        // Assign Play Button
        Button playButton = findViewById(R.id.play_button);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the game matching the corresponding level
                toast("Start Clicked");
            }
        });

        // Assign Customize Button
        TextView customizeButton = findViewById(R.id.customize_button);
        customizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the customization intent
                toast("Customize Clicked");
            }
        });

        // Assign New Game Button
        Button newGameButton = findViewById(R.id.new_game_button);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Clear saved level progress and start game on level 0
                toast("New Game Clicked");
            }
        });

        // Assign Settings Button
        Button settingsButton = findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start settings activity
                toast("Settings Clicked");
            }
        });
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
