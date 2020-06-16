package nl.saxion.playground.orbisrunner.sprite;

import nl.saxion.playground.orbisrunner.R;
import nl.saxion.playground.orbisrunner.game.OrbisRunnerModel;
import nl.saxion.playground.orbisrunner.lib.Entity;

/**
 * Portal sprite
 */
public class Portal extends Sprite {
    @Override
    public int getBitmapRes() {
        return R.drawable.sprite_portal;
    }

    @Override
    public String getName() {
        return "Portal";
    }

    /**
     * When hit, finish the level
     *
     * @param e entity needed for dimensions
     * @return true if dead
     */
    @Override
    public boolean inHitBox(Entity e) {
        if (super.inHitBox(e) && game instanceof OrbisRunnerModel) {
            ((OrbisRunnerModel) game).finish();
        }

        return false;
    }
}
