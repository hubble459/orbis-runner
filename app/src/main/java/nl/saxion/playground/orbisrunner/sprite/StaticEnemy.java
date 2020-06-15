package nl.saxion.playground.orbisrunner.sprite;

import nl.saxion.playground.orbisrunner.R;

public class StaticEnemy extends Sprite {
    public StaticEnemy() {
        setMargin(-20f);
    }

    @Override
    public int getBitmapRes() {
        return R.drawable.sprite_dino;
    }

    @Override
    public String getName() {
        return "Static Enemy";
    }
}
