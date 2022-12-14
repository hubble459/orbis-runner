package nl.saxion.playground.orbisrunner.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.io.File;

import nl.saxion.playground.orbisrunner.R;
import nl.saxion.playground.orbisrunner.singleton.GameProvider;

/**
 * Settings screen in which you can turn sound or music off and on
 *
 * @author Joost Winkelman
 */
public class SettingScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_screen);

        init();
    }

    /**
     * Set buttons
     * Save when toggled
     */
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

        Switch cheat = findViewById(R.id.cheatSwitch);
        cheat.setChecked(GameProvider.isCheatOn());
        cheat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                //toggle cheat off or on
                GameProvider.setCheatOn(checked);
                GameProvider.saveData(SettingScreenActivity.this);
            }
        });
    }

    /**
     * Delete save and restart
     *
     * @param view ButtonView
     */
    public void reset(View view) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.confirm_reset)
                .setPositiveButton(R.string.reset, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteSave();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void deleteSave() {
        String fileName = "savedData.json";
        File path = new File(getFilesDir(), fileName);
        boolean deleted;
        if (path.exists()) {
            deleted = deleteFile(fileName);
        } else {
            deleted = false;
            Toast.makeText(this, "Nothing to reset...", Toast.LENGTH_SHORT).show();
        }
        if (deleted) {
            Toast.makeText(this, "Save deleted!", Toast.LENGTH_SHORT).show();
            GameProvider.newInstance();
            GameProvider.getSave(this);
            finish();
        }
    }

    /**
     * Go to the starting screen
     *
     * @param view ButtonView
     */
    public void mainMenu(View view) {
        finish();
    }

    public void about(View view) {
        Intent intent = new Intent(this, AboutScreenActivity.class);
        startActivity(intent);
    }
}
