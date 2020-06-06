package nl.saxion.playground.orbisrunner.game;

import android.graphics.Canvas;

import nl.saxion.playground.orbisrunner.game.entity.Circle;
import nl.saxion.playground.orbisrunner.game.entity.Player;
import nl.saxion.playground.orbisrunner.lib.Entity;
import nl.saxion.playground.orbisrunner.lib.GameModel;

public class OrbisRunnerModel extends GameModel {
    private Circle circle;
    private Player player;
    private Level level;

    public OrbisRunnerModel(Player player, Level level) {
        this.level = level;

        this.circle = new Circle(true);
        this.circle.setSize(Circle.SIZE_DOUBLE);
        this.circle.setMargin(Circle.STROKE_WIDTH);

        this.player = player;
        this.player.setGame(this);
    }

    @Override
    public void start(Canvas canvas) {
        addEntities();
    }

    private void addEntities() {
        addEntity(player);
        addEntity(circle);

        for (Entity entity : level.getEntities()) {
            addEntity(entity);
        }
    }

    @Override
    public void dead() {

    }

    @Override
    public float[] getXYFromDegrees(float degrees, float margin) {
        return circle.getXYFromDegrees(degrees, margin);
    }
}
