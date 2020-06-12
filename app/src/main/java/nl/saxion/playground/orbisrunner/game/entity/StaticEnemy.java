package nl.saxion.playground.orbisrunner.game.entity;

import nl.saxion.playground.orbisrunner.R;
import nl.saxion.playground.orbisrunner.lib.Entity;

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

    @Override
    public boolean inHitbox(Entity e) {
        boolean hit = super.inHitbox(e);

        if (hit) {

        }

        return hit;
    }
}
