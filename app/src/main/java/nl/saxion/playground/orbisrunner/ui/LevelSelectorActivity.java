package nl.saxion.playground.orbisrunner.ui;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

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

    private static final String TAG = "LevelSelectorActivity";

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
                if (level.isCustom()) {
                    new AlertDialog.Builder(LevelSelectorActivity.this)
                            .setTitle(R.string.delete)
                            .setNegativeButton(R.string.no, null)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    deleteLevel(level);
                                }
                            })
                            .setNeutralButton("Share", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    shareLevel(level);
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

    /**
     * Button to go to the export level activity
     *
     * @param view button
     */
    public void export(View view) {
        Intent intent = new Intent(this, LevelExporterActivity.class);
        startActivity(intent);
    }

    private void shareLevel(Level level) {
        try {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_EMAIL, new String[]{"orbisrunner@gmail.com"});
            share.putExtra(Intent.EXTRA_SUBJECT, "[Orbis Runner] Custom Level");
            JSONObject json = level.toSimpleJSON();
            share.putExtra(Intent.EXTRA_TEXT, json.toString(3));
            startActivity(Intent.createChooser(share, "Share Level:"));
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Level conversion failed...", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Import a level from json in the clipboard
     *
     * @param view button
     */
    public void importClip(View view) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        JSONObject jsonObject = getJSON(clipboard);
        if (jsonObject != null) {
            Level level = Level.fromJSON(jsonObject);
            level.setCustom(true);
            level.setNumber(GameProvider.getLastNumber());
            GameProvider.getLevels().add(level);
            GameProvider.saveData(this);
            levelGridAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Level imported!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Your clipboard doesn't contain a level!", Toast.LENGTH_SHORT).show();
        }
    }

    private JSONObject getJSON(ClipboardManager clipboard) {
        if (clipboard != null && clipboard.hasPrimaryClip() && clipboard.getPrimaryClip() != null && clipboard.getPrimaryClip().getItemCount() > 0) {
            String jsonString = clipboard.getPrimaryClip().getItemAt(0).getText().toString();
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                if (jsonObject.has("scale") && jsonObject.has("entities")) {
                    return jsonObject;
                } else {
                    return null;
                }
            } catch (JSONException e) {
                return null;
            }
        }
        return null;
    }
}