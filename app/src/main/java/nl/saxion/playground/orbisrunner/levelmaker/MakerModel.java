package nl.saxion.playground.orbisrunner.levelmaker;

import android.graphics.Canvas;
import android.graphics.Color;

import nl.saxion.playground.orbisrunner.game.Level;
import nl.saxion.playground.orbisrunner.lib.Entity;
import nl.saxion.playground.orbisrunner.lib.GameModel;
import nl.saxion.playground.orbisrunner.sprite.Circle;
import nl.saxion.playground.orbisrunner.sprite.Player;
import nl.saxion.playground.orbisrunner.ui.LevelMaker;

public class MakerModel extends GameModel {
    private Circle circle;
    private Player player;
    private Level level;
    private LevelMaker levelMaker;

    /**
     * Constructor resetting most values to set-up a maker screen
     *
     * @param levelMaker Activity
     * @param level      is GameProvider#getCurrentGame();
     */
    public MakerModel(LevelMaker levelMaker, Level level) {
        this.levelMaker = levelMaker;
        this.level = level;

        this.circle = new Circle(true, false);
        this.circle.setStrokeWidth(50f);

        this.player = new Player();
        this.player.setGame(this);
        this.player.setColor(Color.BLACK);
        this.player.setEnabled(false);
        this.player.setMargin(-5f);

        Entity.setScale(.5f);
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
            entity.setXYValues(getXYFromDegrees(entity.getStartAngle(), entity.getStartJump(), entity));
            addEntity(entity);
            levelMaker.select(entity);
        }
    }

    @Override
    public float[] getXYFromDegrees(float degrees, float margin, Entity e) {
        return circle.getXYFromDegrees(degrees, margin, e);
    }

    /**
     * When an entity is clicked it will first deselect all other entities
     */
    public void deselectAll() {
        for (Entity entity : getEntities()) {
            entity.setSelected(false);
        }
    }

    public Circle getCircle() {
        return circle;
    }
}