package nl.saxion.playground.orbisrunner.ui;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import nl.saxion.playground.orbisrunner.R;
import nl.saxion.playground.orbisrunner.adapter.EntityGridAdapter;
import nl.saxion.playground.orbisrunner.game.Level;
import nl.saxion.playground.orbisrunner.levelmaker.EntityItem;
import nl.saxion.playground.orbisrunner.levelmaker.MakerModel;
import nl.saxion.playground.orbisrunner.lib.Entity;
import nl.saxion.playground.orbisrunner.lib.GameView;
import nl.saxion.playground.orbisrunner.singleton.GameProvider;
import nl.saxion.playground.orbisrunner.ui.demo.entities.DemoEnemy;

public class LevelMaker extends AppCompatActivity {
    private SeekBar seekBar;
    private GameView gameView;
    private GridView entityList;
    private Level level;
    private MakerModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_maker);

        seekBar = findViewById(R.id.seekBar);
        gameView = findViewById(R.id.gameView);
        entityList = findViewById(R.id.entityList);

        startingPopup();
    }

    private void startingPopup() {
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        level = new Level(GameProvider.getLevels().size() + 1);
                        init();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        showEditPopup();
                        break;
                    case DialogInterface.BUTTON_NEUTRAL:
                        finish();
                }
            }
        };

        new AlertDialog.Builder(this)
                .setTitle("Level Maker")
                .setMessage("Do you want to make a new level or edit a level?")
                .setPositiveButton("New", listener)
                .setNegativeButton("Edit", listener)
                .setNeutralButton("Cancel", listener)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finish();
                    }
                })
                .show();
    }

    private void init() {
        model = new MakerModel(this, level);
        gameView.setGame(model);
        gameView.setBackgroundColor(Color.WHITE);

        final ArrayList<EntityItem> entityItems = new ArrayList<>();
        entityItems.add(new EntityItem(EntityItem.DEMO_ENEMY));

        EntityGridAdapter adapter = new EntityGridAdapter(this, entityItems);

        entityList.setAdapter(adapter);
        entityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Entity e = (Entity) entityItems.get(position).getEntity().newInstance();
                    e.setLevelMaker(LevelMaker.this);
                    e.setXYValues(model.getXYFromDegrees(0, 15, e));
                    select(e);
                    model.addEntity(e);
                    level.addEntity(e);
                } catch (InstantiationException | IllegalAccessException ex) {
                    ex.printStackTrace();
                }
            }
        });

        seekBar.setMax(360);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Entity e = getSelected();
                if (e == null) return;
                e.setXYValues(model.getXYFromDegrees(360 - progress, 15, e));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public void select(Entity e) {
        deselectAll();
        e.setSelected(true);
        int angle = 360 - (int) e.getAngle();
        seekBar.setProgress(angle);
    }

    private Entity getSelected() {
        for (DemoEnemy entity : model.getEntities(DemoEnemy.class)) {
            if (entity.isSelected()) {
                return entity;
            }
        }
        return null;
    }

    private void showEditPopup() {
        ArrayAdapter<Level> adapter = new ArrayAdapter<Level>(this, android.R.layout.simple_list_item_1, GameProvider.getLevels()) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setText(String.format(Locale.ENGLISH, "Level %d", Objects.requireNonNull(getItem(position)).getNumber()));
                return view;
            }
        };

        new AlertDialog.Builder(this)
                .setTitle("Edit Level")
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        level = GameProvider.getLevels().get(which);
                        init();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }

    public void save(View view) {
        int max = GameProvider.getLevels().size() + 1;

        final NumberPicker np = new NumberPicker(this);
        np.setMinValue(1);
        np.setMaxValue(max);
        np.setValue(level.getNumber());
        np.setPaddingRelative(8, 8, 8, 8);

        new AlertDialog.Builder(this)
                .setTitle("Level")
                .setView(np)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        GameProvider.getLevels().add(np.getValue() - 1, level);
                        GameProvider.saveData(LevelMaker.this);
                        Toast.makeText(LevelMaker.this, "Saved!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    public void deselectAll() {
        model.deselectAll();
    }
}