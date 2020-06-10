package nl.saxion.playground.orbisrunner.game.entity;

import nl.saxion.playground.orbisrunner.R;

public class StaticEnemy extends Sprite {
    @Override
    public int getBitmapRes() {
        return R.drawable.walk_0;
    }

    @Override
    public String getName() {
        return "Static Enemy";
    }
}
