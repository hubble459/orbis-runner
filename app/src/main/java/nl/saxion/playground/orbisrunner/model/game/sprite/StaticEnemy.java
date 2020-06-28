package nl.saxion.playground.orbisrunner.model.game.sprite;

import nl.saxion.playground.orbisrunner.R;

/**
 * Dino sprite
 */
public class StaticEnemy extends Sprite {
    @Override
    public int getBitmapRes() {
        return R.drawable.sprite_dino;
    }

    @Override
    public String getName() {
        return "Dino";
    }
}
