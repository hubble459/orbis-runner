package nl.saxion.playground.orbisrunner.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import nl.saxion.playground.orbisrunner.R;
import nl.saxion.playground.orbisrunner.singleton.GameProvider;

public class SettingScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_screen);

        init();
    }

    public void init() {
        Switch sound = findViewById(R.id.soundSwitch);
        sound.setChecked(GameProvider.isSoundOn());
        sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                //toggle sound off or on
                GameProvider.setSoundOn(checked);
                GameProvider.saveData(SettingScreenActivity.this);
            }
        });


        Switch music = findViewById(R.id.musicSwitch);
        music.setChecked(GameProvider.isMusicOn());
        music.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                //toggle music off or on
                GameProvider.setMusicOn(checked);
                GameProvider.saveData(SettingScreenActivity.this);
            }
        });

        Button mainMenuButton = findViewById(R.id.mainMenuButton);
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to the starting screen
                finish();

            }
        });
    }
}
