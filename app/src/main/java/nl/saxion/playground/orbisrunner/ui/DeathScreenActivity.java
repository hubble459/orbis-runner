package nl.saxion.playground.orbisrunner.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import nl.saxion.playground.orbisrunner.R;

public class DeathScreenActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_death_screen);

        init();
    }

    private void init(){
        // Display level player died on
        TextView lvl = findViewById(R.id.died_on);
        lvl.setText("You Died On Level " /* get level player died on*/ );


        // Assign New Game Button
        Button newGameButton = findViewById(R.id.new_game_button);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Clear saved level progress and start game on level 0
                Toast.makeText(DeathScreenActivity.this, "New Game Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        // Assign Main Menu Button
        Button mainMenuButton = findViewById(R.id.main_menu_button);
        mainMenuButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // Go to the starting screen
                Intent intent = new Intent(DeathScreenActivity.this, StartScreenActivity.class);
                startActivity(intent);
                Toast.makeText(DeathScreenActivity.this,"Main Menu Clicked",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
