package nl.saxion.playground.orbisrunner.model.game.sprite;

import nl.saxion.playground.orbisrunner.R;

/**
 * Bird Sprite
 */
public class FlyingEnemy extends Sprite {
    @Override
    public int getBitmapRes() {
        return R.drawable.sprite_bird;
    }

    @Override
    public String getName() {
        return "Bird";
    }
}
