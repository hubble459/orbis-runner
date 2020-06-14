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
import nl.saxion.playground.orbisrunner.levelmaker.MakerModel;
import nl.saxion.playground.orbisrunner.lib.Entity;
import nl.saxion.playground.orbisrunner.lib.GameView;
import nl.saxion.playground.orbisrunner.singleton.GameProvider;
import nl.saxion.playground.orbisrunner.sprite.Circle;
import nl.saxion.playground.orbisrunner.sprite.Coin;
import nl.saxion.playground.orbisrunner.sprite.FlyingEnemy;
import nl.saxion.playground.orbisrunner.sprite.JumpingEnemy;
import nl.saxion.playground.orbisrunner.sprite.Player;
import nl.saxion.playground.orbisrunner.sprite.Portal;
import nl.saxion.playground.orbisrunner.sprite.Sprite;
import nl.saxion.playground.orbisrunner.sprite.StaticEnemy;

public class LevelMaker extends AppCompatActivity {
    private ArrayList<Sprite> sprites;

    private SeekBar sizeBar, posBar, heightBar;
    private GameView gameView;
    private GridView entityList;
    private Level level;
    private MakerModel model;

    private void addSprites() {
        sprites.add(new StaticEnemy());
        sprites.add(new FlyingEnemy());
        sprites.add(new JumpingEnemy());
        sprites.add(new Coin());
        sprites.add(new Portal());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_maker);

        sizeBar = findViewById(R.id.sizeBar);
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
                .setTitle(R.string.level_maker)
                .setMessage(R.string.make_level_question)
                .setPositiveButton(R.string.new_, listener)
                .setNegativeButton(R.string.edit, listener)
                .setNeutralButton(R.string.cancel, listener)
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

        sizeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float scale = 1 / (progress + 2f);
                Entity.setScale(scale);
                Circle c = model.getCircle();
                c.setMargin(c.getMargin() / scale);

                for (Entity entity : model.getEntities()) {
                    entity.resize();
                    if (entity instanceof Player) {
                        float[] xy = getXYFromDegrees(110, entity.getMargin(), entity);
                        xy[2] -= 90;
                        entity.setXYValues(xy);
                    } else {
                        entity.setXYValues(getXYFromDegrees(entity.getAngle(), entity.getJump(), entity));
                    }
                }
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

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

        heightBar.setMax(200);
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
                tv.setText(String.format(Locale.ENGLISH, getString(R.string.level_and_number), Objects.requireNonNull(getItem(position)).getNumber()));
                return view;
            }
        };

        new AlertDialog.Builder(this)
                .setTitle(R.string.edit_level)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        level = GameProvider.getLevels().get(which);
                        init();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
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
                    .setTitle(R.string.level)
                    .setView(np)
                    .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int number = np.getValue() - 1;
                            GameProvider.getLevels().add(number, level);
                            shiftNumbers(number);
                            saveFinish();
                        }
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .show();
        }
    }

    private void saveFinish() {
        GameProvider.saveData(this);
        Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
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