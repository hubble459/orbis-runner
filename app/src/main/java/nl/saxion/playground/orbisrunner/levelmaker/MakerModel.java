package nl.saxion.playground.orbisrunner.levelmaker;

import android.graphics.Canvas;
import android.graphics.Color;

import nl.saxion.playground.orbisrunner.game.Level;
import nl.saxion.playground.orbisrunner.game.entity.Circle;
import nl.saxion.playground.orbisrunner.game.entity.Player;
import nl.saxion.playground.orbisrunner.lib.Entity;
import nl.saxion.playground.orbisrunner.lib.GameModel;
import nl.saxion.playground.orbisrunner.ui.LevelMaker;

public class MakerModel extends GameModel {
    private Circle circle;
    private Player player;
    private Level level;
    private LevelMaker levelMaker;

    public MakerModel(LevelMaker levelMaker, Level level) {
        this.levelMaker = levelMaker;
        this.level = level;

        this.circle = new Circle(true, false);
        this.circle.setStrokeWidth(50f);

        this.player = new Player();
        this.player.setGame(this);
        this.player.setColor(Color.BLACK);
        this.player.setMargin(45f);
        this.player.setScale(.5f);
        this.player.setEnabled(false);
    }

    @Override
    public void start(Canvas canvas) {
        addEntities();
    }

    private void addEntities() {
        addEntity(circle);
        addEntity(player);
    }

    @Override
    public void started(Canvas canvas) {
        for (Entity entity : level.getEntities()) {
            entity.setLevelMaker(levelMaker);
            entity.setScale(.5f);
            entity.setXYValues(getXYFromDegrees(entity.getStartAngle(), entity.getHeight(), entity));
            addEntity(entity);
            levelMaker.select(entity);
        }
    }

    @Override
    public float[] getXYFromDegrees(float degrees, float margin, Entity e) {
        return circle.getXYFromDegrees(degrees, margin, e);
    }

    public void deselectAll() {
        for (Entity entity : getEntities()) {
            entity.setSelected(false);
        }
    }
}