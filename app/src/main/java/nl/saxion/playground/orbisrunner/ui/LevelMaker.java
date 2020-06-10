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
import nl.saxion.playground.orbisrunner.game.entity.Sprite;
import nl.saxion.playground.orbisrunner.game.entity.StaticEnemy;
import nl.saxion.playground.orbisrunner.levelmaker.MakerModel;
import nl.saxion.playground.orbisrunner.lib.Entity;
import nl.saxion.playground.orbisrunner.lib.GameView;
import nl.saxion.playground.orbisrunner.singleton.GameProvider;
import nl.saxion.playground.orbisrunner.ui.demo.entities.DemoEnemy;

public class LevelMaker extends AppCompatActivity {
    private ArrayList<Sprite> sprites;

    private SeekBar posBar, heightBar;
    private GameView gameView;
    private GridView entityList;
    private Level level;
    private MakerModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_maker);

        posBar = findViewById(R.id.posBar);
        heightBar = findViewById(R.id.heightBar);
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

        sprites = new ArrayList<>();
        addSprites();

        EntityGridAdapter adapter = new EntityGridAdapter(this, sprites);

        entityList.setAdapter(adapter);
        entityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Entity e = sprites.get(position).newInstance();
                    e.setLevelMaker(LevelMaker.this);
                    select(e);
                    model.addEntity(e);
                    level.addEntity(e);
                } catch (InstantiationException | IllegalAccessException ex) {
                    ex.printStackTrace();
                }
            }
        });

        posBar.setMax(360);
        posBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Entity e = getSelected();
                if (e == null) return;
                e.setXYValues(model.getXYFromDegrees(360 - progress, 0, e));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        heightBar.setMax(100);
        heightBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Entity e = getSelected();
                if (e == null) return;
                e.setStartJump((float) progress);
                e.setXYValues(model.getXYFromDegrees(e.getAngle(), 0, e));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void addSprites() {
        sprites.add(new DemoEnemy());
        sprites.add(new StaticEnemy());
    }

    public void select(Entity e) {
        deselectAll();
        e.setSelected(true);
        posBar.setProgress(360 - (int) e.getAngle());
        heightBar.setProgress((int) e.getStartJump());
    }

    private Entity getSelected() {
        for (Entity entity : model.getEntities()) {
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
        for (Entity entity : level.getEntities()) {
            entity.setStartAngle(entity.getAngle());
        }

        if (GameProvider.hasLevel(level)) {
            GameProvider.getLevels().set(level.getNumber() - 1, level);
            saveFinish();
        } else {
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
                            int number = np.getValue() - 1;
                            GameProvider.getLevels().add(number, level);
                            shiftNumbers(number);
                            saveFinish();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
    }

    private void saveFinish() {
        GameProvider.saveData(this);
        Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void shiftNumbers(int number) {
        ArrayList<Level> levels = GameProvider.getLevels();
        int max = levels.size();
        if (number != max) {
            for (int i = number; i < max; i++) {
                Level l = levels.get(i);
                l.setNumber(i + 1);
            }
        }
    }

    public void deselectAll() {
        model.deselectAll();
    }

    public float[] getXYFromDegrees(float angle, float margin, Entity e) {
        return model.getXYFromDegrees(angle, margin, e);
    }
}