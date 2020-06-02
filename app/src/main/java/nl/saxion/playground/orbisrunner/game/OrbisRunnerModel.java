package nl.saxion.playground.orbisrunner.game;

import android.graphics.Canvas;

import nl.saxion.playground.orbisrunner.game.entity.Circle;
import nl.saxion.playground.orbisrunner.lib.Entity;
import nl.saxion.playground.orbisrunner.lib.GameModel;

public class OrbisRunnerModel extends GameModel {
    private Entity circle;
    private Entity player;
    private Level level;

    public OrbisRunnerModel(Entity player, Level level) {
        this.player = player;
        this.level = level;
        this.circle = new Circle(false);
    }

    @Override
    public void start(Canvas canvas) {
        super.start(canvas);
    }



}
