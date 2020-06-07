package nl.saxion.playground.orbisrunner.levelmaker;

import android.graphics.Canvas;

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
        this.player.setScale(2);
        this.player.setMargin(55f);
        this.player.setEnabled(false);
    }

    @Override
    public void start(Canvas canvas) {
        addEntities();
    }

    private void addEntities() {
        addEntity(player);
        addEntity(circle);
    }

    @Override
    public void started(Canvas canvas) {
        for (Entity entity : level.getEntities()) {
            entity.setLevelMaker(levelMaker);
            entity.setXYValues(getXYFromDegrees(entity.getAngle(), 0, entity));
            entity.setSelected(true);
            addEntity(entity);
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