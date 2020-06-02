package nl.saxion.playground.orbisrunner.game;

import android.graphics.Canvas;

import nl.saxion.playground.orbisrunner.game.entity.Circle;
import nl.saxion.playground.orbisrunner.game.entity.Player;
import nl.saxion.playground.orbisrunner.lib.Entity;
import nl.saxion.playground.orbisrunner.lib.GameModel;

import static nl.saxion.playground.orbisrunner.game.entity.Circle.SIZE_DOUBLE;
import static nl.saxion.playground.orbisrunner.game.entity.Circle.STROKE_WIDTH;

public class OrbisRunnerModel extends GameModel {
    private Circle bgCircle;
    private Circle circle;
    private Player player;
    private Level level;

    public OrbisRunnerModel(Player player, Level level) {
        this.level = level;

        this.circle = new Circle(true);
        this.circle.setMargin(STROKE_WIDTH - 2f);

        this.bgCircle = new Circle(true);
        this.bgCircle.setSize(SIZE_DOUBLE);

        this.player = player;
        this.player.setGame(this);
        this.player.setDegrees(level.getPlayerDegrees());
    }

    @Override
    public void start(Canvas canvas) {
        addEntities();
    }

    private void addEntities() {
        addEntity(player);
        addEntity(bgCircle);
        addEntity(circle);

        for (Entity entity : level.getEntities()) {
            addEntity(entity);
        }
    }

    public void dead() {

    }

    public float[] getXYFromDegrees(float degrees, float margin) {
        return circle.getXYFromDegrees(degrees, margin);
    }
}
