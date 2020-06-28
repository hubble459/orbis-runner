package nl.saxion.playground.orbisrunner.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import java.util.ArrayList;

import nl.saxion.playground.orbisrunner.R;
import nl.saxion.playground.orbisrunner.adapter.LevelGridAdapter;
import nl.saxion.playground.orbisrunner.model.Level;
import nl.saxion.playground.orbisrunner.singleton.GameProvider;

/**
 * Activity to select a level to play
 *
 * @author Joost Winkelman
 * @since 6/15/2020
 */
public class LevelSelectorActivity extends AppCompatActivity {
    private LevelGridAdapter levelGridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_selector);

        init();
    }

    /**
     * Fill GridView with levels using a custom adapter
     * And init buttons
     */
    private void init() {
        GridView levels = findViewById(R.id.levelGrid);

        levelGridAdapter = new LevelGridAdapter(this, GameProvider.getLevels());

        levels.setAdapter(levelGridAdapter);

        levels.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                final Level level = GameProvider.getLevels().get(position);
                // TODO: 6/15/2020 invert level is custom!!!!
                if (!level.isCustom()) {
                    new AlertDialog.Builder(LevelSelectorActivity.this)
                            .setTitle(R.string.delete)
                            .setNegativeButton(R.string.no, null)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    deleteLevel(level);
                                }
                            })
                            .show();
                    return true;
                }
                return false;
            }
        });

        levels.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (position <= GameProvider.getMaxLevel()) {
                    gotoLevel(position);
                }
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

        Button levelMakerButton = findViewById(R.id.levelMakerButton);
        levelMakerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LevelSelectorActivity.this, LevelMaker.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Delete a level and shift levels to fill up empty space
     * 1 2 3 : remove 2
     * 1   3 : shift
     * 1 2 : done
     *
     * @param level Level to delete
     */
    private void deleteLevel(Level level) {
        ArrayList<Level> levels = GameProvider.getLevels();
        int number = level.getNumber();
        int max = levels.size();
        if (number != max) {
            for (int i = number; i < max; i++) {
                Level l = levels.get(i);
                l.setNumber(i);
            }
        }
        levels.remove(level);
        levelGridAdapter.notifyDataSetChanged();
        GameProvider.saveData(this);
    }

    /**
     * Refresh list on resume, so when a level gets added the list will update
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (levelGridAdapter != null) {
            levelGridAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Start a level
     *
     * @param position position of level to start
     */
    private void gotoLevel(int position) {
        GameProvider.setCurrentLevel(position);
        Intent game = new Intent(LevelSelectorActivity.this, GameActivity.class);
        startActivity(game);
        finish();
    }

    // TODO: 22/06/2020 REMOVE
    public void export(View view) {
        Intent intent = new Intent(this, LevelExporterActivity.class);
        startActivity(intent);
    }
}